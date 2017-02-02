package unifi.util;

import com.google.common.base.Supplier;

import org.apache.commons.lang.Validate;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.json.Json;
import javax.json.JsonArrayBuilder;

/**
 * Helper class for creating pools of objects. An example use might look similar to:
 * <pre>
 * public class MyPooledClass implements Poolable {
 *   private static final Pool<MyPooledClass> pool
 *       = Pools.synchronizedPool(new SimplePool(10));
 *
 *   public static MyPooledClass obtain() {
 *     MyPooledClass instance = pool.acquire();
 *     return instance != null ? instance : new MyPooledClass();
 *   }
 *
 *   public void recycle() {
 *     // clear state if needed
 *     pool.release(this);
 *   }
 *
 *   . . .
 * }
 * </pre>
 */
public final class Pools {

  private Pools() {}

  /**
   * Returns a synchronized (thread-safe) {@link Pool} backed by the specified {@code Pool}.
   * In order to guarantee serial access, it is critical that <strong>all</strong> access to the
   * backing pool is accomplished through the returned pool.
   * <p>
   * The returned pool does <i>not</i> pass the {@link Object#hashCode} and {@link Object#equals}
   * operations through to the backing pool, but relies on {@code Object}'s {@code equals} and
   * {@code hashCode} methods.  This is necessary to preserve the contracts of these operations in
   * the case that the backing pool does not implement them in the common way.
   *
   * @param <T> class of the objects in the pool
   * @param p   pool to be "wrapped" in a synchronized pool
   * @return synchronized view of the specified pool
   */
  public static <T extends Poolable> Pool<T> synchronizedPool(@NonNull Pool<T> p) {
    return new SynchronizedPool<T>(p);
  }

  /**
   * Interface used to represent a manageable pool of objects.
   *
   * @param <T> pooled object type
   */
  public interface Pool<T extends Poolable> {

    /**
     * Attempts to acquire an instance of type {@link T} from the pool of objects.
     *
     * @return an instance from the pool, otherwise {@code null} if no objects exist within the pool
     */
    T acquire();

    /**
     * Releases an instance to the pool. This method is used to populate the pool with instances
     * that are no longer needed and may be recycled and reused elsewhere.
     *
     * @param instance instance to release
     *
     * @return {@code true} if the instance was added to the pool and eligible to be returned by
     *         {@link #acquire()}, otherwise {@code false}
     */
    boolean release(T instance);

  }

  /**
   * Interface used to represent a class that supports being {@link Pool pooled}.
   */
  public interface Poolable {

    /**
     * Recycles this object and {@link Pool#release(Poolable) releases} it back to its pool.
     */
    void recycle();

  }

  /**
   * Simple (non-synchronized) implementation of a {@link Pool} of objects.
   *
   * @param <T> {@inheritDoc}
   */
  public static class SimplePool<T extends Poolable> implements Pool<T> {

    /**
     * Default max number of instances to be pooled.
     */
    private static final int DEFAULT_MAX_SIZE = 8;

    /**
     * Object instances available.
     */
    private final Object[] pool;

    /**
     * Number of object instances available in {@link #pool}.
     */
    private int size;

    /**
     * Optional {@code Supplier} of instances of {@link T} in the case where {@link #acquire()} is
     * called when the {@link #pool} is empty.
     */
    @Nullable
    private Supplier<T> supplier;

    /**
     * Default constructor which constructs a pool with a max size of {@value #DEFAULT_MAX_SIZE}.
     */
    public SimplePool() {
      this(DEFAULT_MAX_SIZE);
    }

    /**
     * Constructs a pool with a size equal to the specified maximum number. The constructed pool
     * will initially be empty, and instances can be pooled (i.e, made available via
     * {@link #acquire()}) by calling {@link #release(Poolable)} on them.
     *
     * @param maxSize maximum size of the pool
     */
    public SimplePool(int maxSize) {
      Validate.isTrue(maxSize > 0, "maxSize must be > 0");
      this.pool = new Object[maxSize];
    }

    /**
     * Constructs a pool with a size equal to the specified maximum number. The constructed pool
     * will initially be empty, and instances can be pooled (i.e, made available via
     * {@link #acquire()}) by calling {@link #release(Poolable)} on them. In addition, if
     * {@link #acquire()} is called when the pool is empty, objects can be provided by the specified
     * {@code supplier}.
     *
     * @param supplier supplier of instances of {@link T} when the pool is empty
     * @param maxSize  maximum size of the pool
     */
    public SimplePool(@NonNull Supplier<T> supplier, int maxSize) {
      this(maxSize);
      Validate.isTrue(supplier != null, "supplier cannot be null");
      this.supplier = supplier;
    }

    /**
     * Attempts to acquire an instance of type {@link T} from the pool of objects. If the pool is
     * currently empty, then an object is returned by the {@link Supplier} of this pool, otherwise
     * {@code null} if no supplier has been provided.
     *
     * @return an instance from the pool, if such, otherwise an instance provided by the supplier
     *         for this pool, otherwise {@code null} if no supplier was provided when constructing
     *         this pool
     */
    @Override
    @SuppressWarnings("unchecked")
    public T acquire() {
      if (size > 0) {
        T instance = (T) pool[--size];
        pool[size] = null;
        return instance;
      }

      return supplier != null ? supplier.get() : null;
    }

    @Override
    public boolean release(T instance) {
      if (isPooled(instance)) {
        throw new IllegalStateException("Object is already in the pool!");
      }

      if (size < pool.length) {
        pool[size++] = instance;
        return true;
      }

      return false;
    }

    /**
     * Returns whether or not the specified {@code instance} is contained within the pool of
     * available objects.
     *
     * @param instance instance to check
     *
     * @return {@code true} if it is pooled, otherwise {@code false}
     */
    private boolean isPooled(T instance) {
      for (int i = 0; i < size; i++) {
        if (pool[i] == instance) {
          return true;
        }
      }

      return false;
    }

    @Override
    public String toString() {
      JsonArrayBuilder poolJsonArray = Json.createArrayBuilder();
      for (int i = 0; i < size; i++) {
        poolJsonArray.add(pool[i].toString());
      }

      return poolJsonArray.build().toString();
    }

  }

  /**
   * Synchronized implementation of a {@link Pool} of objects via the wrapping of an existing pool.
   *
   * @param <T> {@inheritDoc}
   */
  private static class SynchronizedPool<T extends Poolable> implements Pool<T> {

    /**
     * Wrapped pool (also used for synchronization)
     */
    private final Pool<T> pool;

    /**
     * Constructs a synchronized pool by wrapping the specified pool {@code p}.
     *
     * @param p pool to wrap and synchronize
     */
    private SynchronizedPool(@NonNull Pool<T> p) {
      Validate.isTrue(p != null, "p cannot be null");
      this.pool = p;
    }

    @Override
    public T acquire() {
      synchronized (pool) {
        return pool.acquire();
      }
    }

    @Override
    public boolean release(T instance) {
      synchronized (pool) {
        return pool.release(instance);
      }
    }

    @Override
    public String toString() {
      synchronized (pool) {
        return pool.toString();
      }
    }

  }

}
