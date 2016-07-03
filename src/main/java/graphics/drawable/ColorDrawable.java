package graphics.drawable;

import com.badlogic.gdx.graphics.Color;
import com.gmail.collinsmith70.unifi.Unifi;
import com.gmail.collinsmith70.unifi.content.res.AttributeDecl;

public class ColorDrawable {

  public static final int color = 0;
  
  public static final AttributeDecl<?>[] attrs = new AttributeDecl<?>[] {
    new AttributeDecl<Color>(color, Unifi.NAMESPACE, "color", Color.class)
  };
  
}
