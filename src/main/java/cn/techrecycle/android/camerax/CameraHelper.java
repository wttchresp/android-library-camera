package cn.techrecycle.android.camerax;

import android.content.Context;
import android.util.Size;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.Preview.SurfaceProvider;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * CameraX 相机帮助工具类
 */
public class CameraHelper {

  private final Context context;
  // 相机
  private CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
  // 图像预览
  private final SurfaceProvider surfaceProvider;
  // 相机监听
  private final CameraListener cameraListener;
  // 相机实例
  private Camera camera;
  // 视频分析帧宽度
  private final int width;
  // 视频分析帧高度
  private final int height;

  private CameraHelper(Builder builder) {
    this.context = builder.context;
    if (builder.cameraSelector != null) {
      this.cameraSelector = builder.cameraSelector;
    }
    this.surfaceProvider = builder.surfaceProvider;
    this.cameraListener = builder.cameraListener;
    this.width = builder.width;
    this.height = builder.height;
  }

  public static Builder builder(Context context) {
    return new Builder((context));
  }


  /**
   * 启动相机,绑定相机到指定的 context 的生命周期中.
   */
  public void start() {
    ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider
        .getInstance(context);
    cameraProviderListenableFuture.addListener(() -> {
      try {
        ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
        Preview preview = new Preview.Builder()
            .build();
        preview.setSurfaceProvider(surfaceProvider);

        ImageAnalysis analysis = new ImageAnalysis.Builder()
            .setImageQueueDepth(1)
            .setTargetResolution(new Size(width, height))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build();
        analysis.setAnalyzer(Executors.newSingleThreadExecutor(),
            image -> {
              cameraListener.onPreview(image, camera);
              image.close();
            });
        cameraProvider.unbindAll();
        camera = cameraProvider
            .bindToLifecycle((LifecycleOwner) context, cameraSelector, preview, analysis);
        cameraListener.onCameraOpened(camera);
      } catch (ExecutionException | InterruptedException e) {
        cameraListener.onCameraError(e);
      }
    }, ContextCompat.getMainExecutor(context));
  }

  public static class Builder {

    private final Context context;
    // 相机选择
    private CameraSelector cameraSelector;
    // 预览
    private SurfaceProvider surfaceProvider;
    // 相机监听器
    private CameraListener cameraListener = new CameraListener() {
    };
    // 视频帧分析宽度
    private int width;
    // 视频帧分析高度
    private int height;


    public Builder(Context context) {
      this.context = context;
    }

    public Builder cameraSelector(CameraSelector cameraSelector) {
      this.cameraSelector = cameraSelector;
      return this;
    }

    public Builder preview(SurfaceProvider surfaceProvider) {
      this.surfaceProvider = surfaceProvider;
      return this;
    }

    public Builder listener(CameraListener listener) {
      if (listener != null) {
        this.cameraListener = listener;
      }
      return this;
    }

    public Builder width(int width) {
      this.width = width;
      return this;
    }

    public Builder height(int height) {
      this.height = height;
      return this;
    }

    public CameraHelper build() {
      return new CameraHelper(this);
    }
  }
}
