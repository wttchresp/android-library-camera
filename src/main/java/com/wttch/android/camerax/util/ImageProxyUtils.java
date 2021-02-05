package com.wttch.android.camerax.util;

import static androidx.camera.core.internal.utils.ImageUtil.imageToJpegByteArray;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.ImageProxy.PlaneProxy;
import androidx.camera.core.internal.utils.ImageUtil.CodecFailedException;
import java.nio.ByteBuffer;
import timber.log.Timber;

/**
 * ImageProxy 相关的工具类
 *
 * @see androidx.camera.core.ImageProxy
 */
public class ImageProxyUtils {

  /**
   * 将 ImageProxy 转换为 Bitmap
   */
  @Nullable
  public static Bitmap toBitmap(@NonNull ImageProxy image) {
    try {
      @SuppressLint("RestrictedApi")
      byte[] bytes = imageToJpegByteArray(image);
      if (bytes == null) {
        return null;
      }
      return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    } catch (CodecFailedException e) {
      Timber.d("ImageProxyUtils.toBitmap error: %s", e.getLocalizedMessage());
      return null;
    }
  }

  /**
   * 将 ImageProxy 转换为 nv21 字节数据
   */
  public static byte[] yuv_420_888toNv21(@NonNull ImageProxy image) {
    PlaneProxy yPlane = image.getPlanes()[0];
    PlaneProxy uPlane = image.getPlanes()[1];
    PlaneProxy vPlane = image.getPlanes()[2];
    ByteBuffer yBuffer = yPlane.getBuffer();
    ByteBuffer uBuffer = uPlane.getBuffer();
    ByteBuffer vBuffer = vPlane.getBuffer();
    yBuffer.rewind();
    uBuffer.rewind();
    vBuffer.rewind();
    int ySize = yBuffer.remaining();
    int position = 0;
    byte[] nv21 = new byte[ySize + image.getWidth() * image.getHeight() / 2];
    for (int row = 0; row < image.getHeight(); ++row) {
      yBuffer.get(nv21, position, image.getWidth());
      position += image.getWidth();
      yBuffer.position(Math.min(ySize,
          yBuffer.position() - image.getWidth() + yPlane.getRowStride()));
    }
    int chromaHeight = image.getHeight() / 2;
    int chromaWidth = image.getWidth() / 2;
    int vRowStride = vPlane.getRowStride();
    int uRowStride = uPlane.getRowStride();
    int vPixelStride = vPlane.getPixelStride();
    int uPixelStride = uPlane.getPixelStride();
    byte[] vLineBuffer = new byte[vRowStride];
    byte[] uLineBuffer = new byte[uRowStride];
    for (int row = 0; row < chromaHeight; ++row) {
      vBuffer.get(vLineBuffer, 0, Math.min(vRowStride, vBuffer.remaining()));
      uBuffer.get(uLineBuffer, 0, Math.min(uRowStride, uBuffer.remaining()));
      int vLineBufferPosition = 0;
      int uLineBufferPosition = 0;
      for (int col = 0; col < chromaWidth; ++col) {
        nv21[position++] = vLineBuffer[vLineBufferPosition];
        nv21[position++] = uLineBuffer[uLineBufferPosition];
        vLineBufferPosition += vPixelStride;
        uLineBufferPosition += uPixelStride;
      }
    }
    return nv21;
  }
}
