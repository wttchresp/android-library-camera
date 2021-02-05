package com.wttch.android.camerax;

import androidx.camera.core.Camera;
import androidx.camera.core.ImageProxy;

public abstract class AbstractCameraListener implements CameraListener {

  @Override
  public void onCameraOpened(Camera camera) {

  }

  @Override
  public void onPreview(ImageProxy image, Camera camera) {

  }

  @Override
  public void onCameraError(Exception e) {

  }
}
