package deprecated.drawable;

import com.badlogic.gdx.graphics.Texture;
import deprecated.unifi.Unifi;
import deprecated.unifi.content.res.AttributeDecl;

public class TextureDrawable {

  public static final int src = 0;
  
  public static final AttributeDecl<?>[] attrs = new AttributeDecl<?>[] {
    new AttributeDecl<Texture>(src, Unifi.NAMESPACE, "src", Texture.class)
  };
  
}
