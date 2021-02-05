package com.wttch.android.camerax;

import androidx.camera.core.Camera;
import androidx.camera.core.ImageProxy;

public interface CameraListener {

  void onCameraOpened(Camera camera);

  void onPreview(ImageProxy image, Camera camera);
  
  void onCameraError(Exception e);
}
