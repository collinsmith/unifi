package deprecated.drawable;

import com.badlogic.gdx.graphics.Color;
import deprecated.unifi.Unifi;
import deprecated.unifi.content.res.AttributeDecl;

public class ColorDrawable {

  public static final int color = 0;
  
  public static final AttributeDecl<?>[] attrs = new AttributeDecl<?>[] {
    new AttributeDecl<Color>(color, Unifi.NAMESPACE, "color", Color.class)
  };
  
}
