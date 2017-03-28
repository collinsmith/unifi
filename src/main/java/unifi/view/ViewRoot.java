package unifi.view;

import com.google.common.base.MoreObjects;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import unifi.content.Context;
import unifi.graphics.Canvas;
import unifi.graphics.Rect;
import unifi.runtime.AbstractHandler;
import unifi.runtime.Handler;
import unifi.runtime.Message;
import unifi.runtime.MessageQueue;
import unifi.util.FocusDirection;
import unifi.util.Log;
import unifi.util.UnifiRuntimeException;

import static unifi.view.View.VIEW_LOG_TAG;

public class ViewRoot implements ViewParent, Disposable, InputProcessor {
  private static final String TAG = "ViewRoot";

  private static final boolean DEBUG_LAYOUT = true;
  private static final boolean DEBUG_DRAW = true;
  private static final boolean DEBUG_FOCUS = true;

  @NonNull private final Vector2 mTmpCoords = new Vector2();

  private boolean mFirst = true;
  private boolean mLayoutRequested = false;
  private boolean mLayoutScheduled = false;
  private boolean mInLayout = false;
  @NonNull Collection<View> mLayoutRequesters = new ArrayList<>();
  private boolean mHandlingLayoutInLayoutRequest = false;

  @NonNull Thread mThread;
  @NonNull Context mContext;

  @NonNull Canvas mCanvas;
  @NonNull private Viewport mViewport;
  @NonNull private Camera mCamera;

  @NonNull private ShapeRenderer mDebugRenderer;

  @Nullable View mView;

  @NonNull View.AttachInfo mAttachInfo;

  @NonNull final MessageQueue mMessageQueue;
  @NonNull final ViewRootHandler mHandler;

  @Nullable LayoutParams mWindowAttributes;

  int mWidth = -1;
  int mHeight = -1;
  @NonNull Rect mDirty;

  //region Constructors
  /**
   * Constructs a view root with a {@link ScalingViewport}.
   */
  public ViewRoot() {
    this(new ScalingViewport(Scaling.fit,
        Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
        new OrthographicCamera()));
  }

  /**
   * Constructs a view root with the specified {@link Viewport}.
   */
  public ViewRoot(@NonNull Viewport viewport) {
    this(new Canvas(viewport));
  }

  /**
   * Constructs a view root with the specified {@link Viewport} and
   * {@link Batch} onto which this view root is drawn.
   */
  public ViewRoot(@NonNull Viewport viewport, @NonNull Batch batch) {
    this(new Canvas(viewport, batch));
  }

  private ViewRoot(@NonNull Canvas canvas) {
    if (canvas == null) {
      throw new IllegalArgumentException("Cannot create a ViewRoot with a null Canvas");
    }

    mThread = Thread.currentThread();
    mCanvas = canvas;
    mViewport = canvas.getViewport();
    mContext = new Context();
    mAttachInfo = new View.AttachInfo(this);
    mMessageQueue = new MessageQueue();
    mHandler = new ViewRootHandler();
    mDirty = new Rect();

    // FIXME: This seems redundant, maybe grap from mViewport.getWorld*()
    mWidth = Gdx.graphics.getWidth();
    mHeight = Gdx.graphics.getHeight();

    mViewport.update(mWidth, mHeight, true);
    mCamera = mViewport.getCamera();
    if (!(mCamera instanceof OrthographicCamera)) {
      throw new IllegalArgumentException(
          "The camera for the specified Viewport must be a OrthographicCamera");
    }

    ((OrthographicCamera) mCamera).setToOrtho(true);
  }
  //endregion

  void checkThread() {
    if (mThread != Thread.currentThread()) {
      throw new UnifiRuntimeException(
          "Only the original thread that created a view hierarchy can touch its views.");
    }
  }

  @Override
  public void dispose() {
    mContext.dispose();
    mContext = null;
    mCanvas.dispose();
    mCanvas = null;
  }

  //region Viewport
  /**
   * Resizes the {@link Viewport} to the specified area, in pixels.
   */
  public void update(int width, int height) {
    // This updates mViewport as well
    mCanvas.update(width, height);
    //invalidate();
  }
  //endregion

  //region setView
  @Nullable
  public View getView() {
    return mView;
  }

  public void setView(@NonNull View view) {
    setView(view, null);
  }

  public void setView(@NonNull View view, @Nullable LayoutParams attrs) {
    synchronized (this) {
      if (mView == null) {
        mView = view;
        if (DEBUG_LAYOUT) {
          Log.d(TAG, "WindowLayout in setView:" + attrs);
        }

        mAttachInfo.mRootView = view;
        view.assignParent(this);
        mWindowAttributes = MoreObjects.firstNonNull(attrs, new LayoutParams());
        requestLayout();
      }
    }
  }
  //endregion

  /**
   * Draws the contents of the view root onto its buffer and performs any update
   * actions, such as, animations or input events.
   */
  public void draw() {
    if (!mMessageQueue.isEmpty()) {
      Collection<Message> expiredMessages = new ArrayList<>(mMessageQueue.size());
      mMessageQueue.drainTo(expiredMessages);
      for (Message msg : expiredMessages) {
        Handler target = msg.getTarget();
        if (target != null) {
          target.handleMessage(msg);
          continue;
        }

        Runnable callback = msg.getCallback();
        if (callback != null) {
          callback.run();
        }
      }
    }

    synchronized (this) {
      if (mView == null) {
        return;
      }

      if (mFirst || mLayoutRequested) {
        mFirst = false;
        mLayoutScheduled = true;
        doLayout();
      }
    }

    Canvas canvas = mCanvas;
    canvas.begin();
    mView.draw(canvas);
    canvas.end();
  }

  //region Layout
  /**
   * Invalidates the entire contents of the view root and requests that another
   * layout be performed.
   */
  void invalidate() {
    mDirty.set(0, 0, mWidth, mHeight);
    requestLayout();
  }

  /**
   * Indicates whether this view root is currently undergoing a layout pass.
   */
  boolean isInLayout() {
    return mInLayout;
  }

  /**
   * Called by {@link unifi.view.View#requestLayout()} if the view hierarchy is currently
   * undergoing a layout pass. {@code requestLayout()} should not generally be called during
   * layout, unless the container hierarchy knows what it is doing (i.e., it is fine as long
   * as all children in that container hierarchy are measured and laid out at the end of the
   * layout pass for that container). If {@code requestLayout()} is called anyway, we handle
   * it correctly by registering all requesters during a frame as it proceeds. At the end of
   * the frame, we check all of those views to see if any still have pending layout requests,
   * which indicates that they were not correctly handled by their container hierarchy.
   * If that is the case, we clear all such flags in the tree, to remove the buggy flag
   * state that leads to blank containers, and force a second request/measure/layout pass in
   * this frame. If more {@code requestLayout()} calls are received during that second
   * layout pass, we post those requests to the next frame to avoid possible infinite loops.
   *
   * <p>The return value from this method indicates whether the request should proceed (if
   * it is a request during the first layout pass) or should be skipped and posted to the
   * next frame (if it is a request during the second layout pass).
   *
   * @param view The view that requested the layout
   *
   * @return {@code true} if the request should proceed, {@code false} otherwise.
   */
  boolean requestLayoutDuringLayout(@NonNull View view) {
    if (view.mParent == null || view.mAttachInfo == null) {
      return true;
    }

    if (!mLayoutRequesters.contains(view)) {
      mLayoutRequesters.add(view);
    }

    if (!mHandlingLayoutInLayoutRequest) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void requestLayout() {
    if (!mHandlingLayoutInLayoutRequest) {
      checkThread();
      mLayoutRequested = true;
      scheduleLayout();
    }
  }

  @Override
  public boolean isLayoutRequested() {
    return mLayoutRequested;
  }

  /**
   * Schedules a layout to be performed before the next frame.
   */
  private void scheduleLayout() {
    if (!mLayoutScheduled) {
      return;
    }

    mLayoutScheduled = true;
    mHandler.sendEmptyMessage(MSG_DO_LAYOUT);
  }

  /**
   * Unschedules any scheduled layouts.
   */
  private void unscheduleLayout() {
    if (!mLayoutScheduled) {
      return;
    }

    mLayoutScheduled = false;
    mHandler.removeMessages(MSG_DO_LAYOUT);
  }

  /**
   * Lays out the components of this view root which is done in two phases:
   * <ul>
   *   <li>Measuring the components using provided layout parameters.
   *   <li>Laying out and resizing those components based on the measurements.
   * </ul>
   */
  private void doLayout() {
    if (DEBUG_LAYOUT) {
      Log.d(TAG, "Performing layout...");
    }

    final int width = (int) mViewport.getWorldWidth();
    final int height = (int) mViewport.getWorldHeight();
    final LayoutParams params = mWindowAttributes;
    measureHierarchy(mView, params, width, height);
    performLayout(params, width, height);
  }

  private void measureHierarchy(@NonNull View host, @NonNull LayoutParams params,
                                int desiredWindowWidth, int desiredWindowHeight) {
    if (DEBUG_LAYOUT) {
      Log.v(TAG, "Measuring " + host + " in display "
          + desiredWindowWidth + "x" + desiredWindowHeight + "...");
    }

    int childWidthMeasureSpec = getRootMeasureSpec(desiredWindowWidth, params.width);
    int childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, params.height);
    host.measure(childWidthMeasureSpec, childHeightMeasureSpec);
  }

  private int getRootMeasureSpec(int windowSize, int rootDimension) {
    switch (rootDimension) {
      case LayoutParams.WRAP_CONTENT:
        return MeasureSpec.create(windowSize, MeasureSpec.AT_MOST);
      case LayoutParams.MATCH_PARENT:
        return MeasureSpec.create(windowSize, MeasureSpec.EXACTLY);
      default:
        return MeasureSpec.create(rootDimension, MeasureSpec.EXACTLY);
    }
  }

  private void performLayout(@NonNull LayoutParams params,
                             int desiredWindowWidth, int desiredWindowHeight) {
    if (mView == null) {
      throw new UnifiRuntimeException("performLayout() called with null root View");
    }

    mLayoutRequested = false;
    mInLayout = true;

    final View host = mView;
    if (DEBUG_LAYOUT) {
      Log.v(TAG, "Laying out " + host + " to (" +
          host.getMeasuredWidth() + ", " + host.getMeasuredHeight() + ")");
    }

    host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
    mInLayout = false;
    if (!mLayoutRequesters.isEmpty()) {
      Collection<View> validLayoutRequesters = getValidLayoutRequesters(mLayoutRequesters, false);
      if (!validLayoutRequesters.isEmpty()) {
        mHandlingLayoutInLayoutRequest = true;

        for (View view : validLayoutRequesters) {
          Log.e(VIEW_LOG_TAG, "requestLayout() improperly called by " + view +
              " during layout: running second layout pass");
        }

        measureHierarchy(host, params, desiredWindowWidth, desiredWindowHeight);
        mInLayout = true;
        host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());

        mHandlingLayoutInLayoutRequest = false;

        // Check the valid requests again, this time without
        // checking/clearing the
        // layout flags, since requests happening during the second pass get
        // noop'd
        validLayoutRequesters = getValidLayoutRequesters(mLayoutRequesters,
            true);
        if (validLayoutRequesters != null) {
          final Collection<View> finalRequesters = validLayoutRequesters;
          // Post second-pass requests to the next frame
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              for (View view : finalRequesters) {
                Log.w(VIEW_LOG_TAG,
                    "requestLayout() improperly called by " + view +
                    " during second layout pass: posting in next frame");
                view.requestLayout();
              }
            }
          });
        }
      }
    }

    mInLayout = false;
  }

  @NonNull
  private Collection<View> getValidLayoutRequesters(@NonNull Collection<View> layoutRequesters,
                                                    boolean secondPass) {
    Collection<View> validLayoutRequesters = null;
    for (View view : layoutRequesters) {
      if (view != null && view.mAttachInfo != null && view.mParent != null
          && (secondPass
              || (view.mPrivateFlags & View.PFLAG_WILL_LAYOUT) == View.PFLAG_WILL_LAYOUT)) {
        boolean gone = false;
        View parent = view;
        while (parent != null) {
          if ((parent.mViewFlags & View.VISIBILITY_MASK) == View.GONE) {
            gone = true;
          }

          if (parent.mParent instanceof View) {
            parent = (View) parent.mParent;
          } else {
            parent = null;
          }
        }

        if (!gone) {
          if (validLayoutRequesters == null) {
            validLayoutRequesters = new ArrayList<>();
          }

          validLayoutRequesters.add(view);
        }
      }
    }

    if (secondPass) {
      for (View view : layoutRequesters) {
        while (view != null &&
            (view.mPrivateFlags & View.PFLAG_WILL_LAYOUT) != 0) {
          view.mPrivateFlags &= ~View.PFLAG_WILL_LAYOUT;
          if (view.mParent instanceof View) {
            view = (View) view.mParent;
          } else {
            view = null;
          }
        }
      }
    }

    layoutRequesters.clear();
    return validLayoutRequesters != null ? validLayoutRequesters : Collections.<View>emptyList();
  }
  //endregion

  //region TouchMode
  /**
   * Something in the current window tells us we need to change the touch mode.
   * For example, we are not in touch mode, and the user touches the screen.
   *
   * @param inTouchMode {@code true} if we want to be in touch mode,
   *                    {@code false} otherwise
   *
   * @return {@code true} if the touch mode changed, and the focus changed as a
   *         result of this call, {@code false} otherwise
   */
  boolean ensureTouchMode(boolean inTouchMode) {
    return ensureTouchModeLocally(inTouchMode);
  }

  /**
   * Ensures that the touch mode for this window is set, and if it is changing,
   * takes the appropriate action.
   *
   * @param inTouchMode {@code true} if we want to be in touch mode,
   *                    {@code false} otherwise
   *
   * @return {@code true} if the touch mode changed, and the focus changed as a
   *         result of this call, {@code false} otherwise
   */
  private boolean ensureTouchModeLocally(boolean inTouchMode) {//TODO: implement
    return true;
  }
  //endregion

  @Nullable
  @Override
  public ViewParent getParent() {
    return null;
  }

  //region Inherited Methods
  @Override
  public void invalidateChild(@NonNull View child, @Nullable Rect dirty) {
    invalidateChildInParent(null, dirty);
  }

  @Nullable
  @Override
  public ViewParent invalidateChildInParent(@NonNull @Size(value = 2) int[] location,
                                            @NonNull Rect dirty) {
    checkThread();
    if (DEBUG_DRAW) {
      Log.v(TAG, "Invalidate child: " + dirty);
    }

    if (dirty == null) {
      invalidate();
      return null;
    } else if (dirty.isEmpty()) {
      return null;
    }

    final Rect localDirty = mDirty;
    if (!localDirty.isEmpty() && !localDirty.contains(dirty)) {
      mAttachInfo.mIgnoreDirtyState = true;
    }

    localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);
    final boolean intersected = localDirty.intersect(0, 0, mWidth, mHeight);
    if (!intersected) {
      localDirty.setEmpty();
    }

    scheduleLayout();
    return null;
  }

  @Override
  public void focusableViewAvailable(@NonNull View view) {

  }

  @Override
  public void requestChildFocus(@NonNull View child, @NonNull View focused) {

  }

  @Override
  public void clearChildFocus(@NonNull View child) {

  }

  @Nullable
  @Override
  public View focusSearch(@Nullable View v, @FocusDirection.Real int direction) {
    checkThread();
    if (!(mView instanceof ViewGroup)) {
      return null;
    }

    return null;
  }

  @Override
  public boolean canResolveLayoutDirection() {
    return true;
  }

  @Override
  public boolean isLayoutDirectionResolved() {
    return true;
  }

  @Override
  public int getLayoutDirection() {
    return View.LAYOUT_DIRECTION_RESOLVED_DEFAULT;
  }

  @Override
  public void childDrawableStateChanged(@NonNull View child) {}
  //endregion

  //region InputProcessor
  @NonNull
  private Vector2 toWindowCoords(@NonNull Vector2 coords) {
    mViewport.unproject(coords);
    return coords;
  }

  @NonNull
  private Vector2 toScreenCoords(@NonNull Vector2 coords) {
    mViewport.project(coords);
    return coords;
  }

  private boolean isInWindow(int x, int y) {
    int screenX = mViewport.getScreenX();
    int screenY = mViewport.getScreenY();
    return screenX <= x && x <= screenX + mViewport.getScreenWidth()
        && screenY <= y && y <= screenY + mViewport.getScreenHeight();
  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    if (!isInWindow(screenX, screenY) || mView == null) {
      return false;
    }

    mTmpCoords.set(screenX, screenY);
    toWindowCoords(mTmpCoords);

    View hit = mView.hitDeepestFocusableView(mTmpCoords.x, mTmpCoords.y);
    if (hit != null) {
      if (DEBUG_FOCUS) {
        Log.d(TAG, "ViewRoot.isOver on " + hit);
      }

      hit.setOver(hit.pointInView(mTmpCoords.x, mTmpCoords.y));
      return true;
    }

    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
  //endregion

  final static int MSG_DO_CALLBACK = 1;
  final static int MSG_DO_LAYOUT = 2;

  final class ViewRootHandler extends AbstractHandler {
    public ViewRootHandler() {
      super(mMessageQueue);
    }

    @NonNull
    @Override
    public String getMessageName(@NonNull Message msg) {
      switch (msg.what) {
        case MSG_DO_CALLBACK: return "MSG_DO_CALLBACK";
        case MSG_DO_LAYOUT:   return "MSG_DO_LAYOUT";
        default:              return super.getMessageName(msg);
      }
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
      switch (msg.what) {
        case MSG_DO_CALLBACK:
          msg.getCallback().run();
          break;
        case MSG_DO_LAYOUT:
          doLayout();
          break;
      }
    }
  }

  public static class LayoutParams extends ViewGroup.LayoutParams {
    public LayoutParams() {
      super(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
  }
}
