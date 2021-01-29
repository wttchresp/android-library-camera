package cn.techrecycle.android.camerax;

import androidx.camera.core.Camera;
import androidx.camera.core.ImageProxy;

public interface CameraListener {

  default void onCameraOpened(Camera camera) {
  }

  default void onPreview(ImageProxy image, Camera camera) {
  }

  default void onCameraClosed() {
  }

  default void onCameraError(Exception e) {
  }
}
