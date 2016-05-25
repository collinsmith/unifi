package com.gmail.collinsmith70.unifi.content;

import com.badlogic.gdx.assets.AssetManager;
import com.gmail.collinsmith70.unifi.content.res.Resources;
import com.gmail.collinsmith70.unifi.content.res.Resources.Theme;

import android.support.annotation.NonNull;

public interface Context {

  @NonNull
  AssetManager getAssets();
  
  @NonNull
  String getPackageName();
  
  @NonNull
  Resources getResources();
  
  @NonNull
  Theme getTheme();
  
  void setTheme(@NonNull Theme theme);
  
}
