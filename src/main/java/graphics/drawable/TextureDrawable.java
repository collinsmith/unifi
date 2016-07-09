package graphics.drawable;

import com.badlogic.gdx.graphics.Texture;
import com.gmail.collinsmith70.unifi.Unifi;
import com.gmail.collinsmith70.unifi.content.res.AttributeDecl;

public class TextureDrawable {

  public static final int src = 0;
  
  public static final AttributeDecl<?>[] attrs = new AttributeDecl<?>[] {
    new AttributeDecl<Texture>(src, Unifi.NAMESPACE, "src", Texture.class)
  };
}
