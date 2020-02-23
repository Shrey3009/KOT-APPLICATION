package com.alfredposclient.zxing.camera.open;//package com.example.zun.zunzxing.camera.open;
//
//import android.content.Context;
//import android.graphics.Point;
//import android.graphics.Rect;
//import android.hardware.Camera;
//import android.util.Log;
//import android.view.Surface;
//import android.view.SurfaceHolder;
//import android.view.WindowManager;
//
//import com.example.zun.zunzxing.camera.CameraConfigurationManager;
//import com.example.zun.zunzxing.camera.CameraManager;
//import com.google.zxing.PlanarYUVLuminanceSource;
//
//import java.io.IOException;
//
///**
// * Created by
// *
// */
//
//public class WrapperCameraManager extends CameraManager{
//
//    private static final String TAG = WrapperCameraManager.class.getSimpleName();
//    private boolean isPortrait = false;  //是否竖屏
//    private boolean isFrontCamera = false; //前置true/后置摄像头false
//    private final Context context;
//    private SurfaceHolder holder;
//    private Camera camera;
//    private boolean initialized;
//
//
//    public WrapperCameraManager(Context context) {
//        super(context);
//        this.context = context;
//    }
//
//    private void openCamera(int face, SurfaceHolder holder) throws IOException {
//        this.holder = holder;
//        Camera theCamera = camera;
//        if (theCamera == null) {
//            theCamera = open(face);
//            if (theCamera == null) {
//                throw new IOException();
//            }
//            camera = theCamera;
//        }
//        theCamera.setPreviewDisplay(holder);
//        if (!initialized) {
//            initialized = true;
//            configManager.initFromCameraParameters(theCamera);
//            if (requestedFramingRectWidth > 0 && requestedFramingRectHeight > 0) {
//                setManualFramingRect(requestedFramingRectWidth, requestedFramingRectHeight);
//                requestedFramingRectWidth = 0;
//                requestedFramingRectHeight = 0;
//            }
//        }
//
//        Camera.Parameters parameters = theCamera.getParameters();
//        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
//        try {
//            configManager.setDesiredCameraParameters(theCamera, false);
//        } catch (RuntimeException re) {
//            // Driver failed
//            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
//            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
//            // Reset:
//            if (parametersFlattened != null) {
//                parameters = theCamera.getParameters();
//                parameters.unflatten(parametersFlattened);
//                try {
//                    theCamera.setParameters(parameters);
//                    configManager.setDesiredCameraParameters(theCamera, true);
//                } catch (RuntimeException re2) {
//                    // Well, darn. Give up
//                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
//                }
//            }
//        }
//    }
//
//
//    public synchronized void openFrontCamera(SurfaceHolder holder) throws IOException {
//        openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT, holder);
//        isFrontCamera = true;
//    }
//
//
//    public synchronized void openBackCamera(SurfaceHolder holder) throws IOException {
//
//        openCamera(Camera.CameraInfo.CAMERA_FACING_BACK, holder);
//        isFrontCamera = false;
//
//
//    }
//
//    /**
//     * 打开相机
//     *
//     * @param face
//     * @return null 没有符合条件的或没有相机
//     */
//    private Camera open(int face) {
//        int numCameras = Camera.getNumberOfCameras();
//        if (numCameras == 0) {
//            Log.w(TAG, "No cameras!");
//            return null;
//        }
//        // Select a camera if no explicit camera requested
//        int index = 0;
//        while (index < numCameras) {
//            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//            Camera.getCameraInfo(index, cameraInfo);
//            if (cameraInfo.facing == face) {
//                break;
//            }
//            index++;
//        }
//        Camera camera = null;
//        if (index < numCameras) {
//            Log.i(TAG, "Opening camera #" + index);
//            camera = Camera.open(index);
//        } else {
//            Log.i(TAG, "No camera facing back; returning camera #0");
//        }
//        return camera;
//    }
//
//
//    /**
//     *
//     * @param holder The surface object which the camera will draw preview frames into.
//     * @throws IOException
//     * @deprecated 使用 {@link #openBackCamera(SurfaceHolder)}打开前置摄像头 或 {@link #openFrontCamera(SurfaceHolder)}打开后置摄像头
//     */
//    @Deprecated
//    @Override
//    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
//        openBackCamera(holder);
//    }
//
//    @Override
//    public Point getCameraResolution() {
//        Point cameraResolution = super.getCameraResolution();
//        if (isPortrait) return new Point(cameraResolution.y, cameraResolution.x);
//        return cameraResolution;
//    }
//
//    @Override
//    public synchronized Rect getFramingRectInPreview() {
//        if (framingRectInPreview == null) {
//            Rect framingRect = getFramingRect();
//            if (framingRect == null) {
//                return null;
//            }
//            Rect rect = new Rect(framingRect);
//            Point cameraResolution = configManager.getCameraResolution();
//            Point screenResolution = configManager.getScreenResolution();
//            if (cameraResolution == null || screenResolution == null) {
//                // Called early, before init even finished
//                return null;
//            }
//            //-----竖屏修改----------
//            if (isPortrait) {
//                rect.left = rect.left * cameraResolution.y / screenResolution.x;
//                rect.right = rect.right * cameraResolution.y / screenResolution.x;
//                rect.top = rect.top * cameraResolution.x / screenResolution.y;
//                rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
//                //---------------
//            } else {
//                rect.left = rect.left * cameraResolution.x / screenResolution.x;
//                rect.right = rect.right * cameraResolution.x / screenResolution.x;
//                rect.top = rect.top * cameraResolution.y / screenResolution.y;
//                rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
//            }
//            framingRectInPreview = rect;
//        }
//        return framingRectInPreview;
//
//    }
//
//    /**
//     * A factory method to build the appropriate LuminanceSource object based on the format
//     * of the preview buffers, as described by Camera.Parameters.
//     *
//     * @param data   A preview frame.
//     * @param width  The width of the image.
//     * @param height The height of the image.
//     * @return A PlanarYUVLuminanceSource instance.
//     */
//    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
//        Rect rect = getFramingRectInPreview();
//        if (rect == null) {
//            return null;
//        }
//
//        byte[] rotate = new byte[data.length];
//
//        if (isPortrait) {    //竖屏
//            if (isFrontCamera) {
//                //前置摄像头竖屏顺旋转90度,垂直翻转<上下>
//                for (int y = 0; y < height; y++) {
//                    for (int x = 0; x < width; x++) {
//                        rotate[(width - 1 - x) * height + (height - 1 - y)] = data[x + y * width];
//                    }
//                }
//            } else {
//                //后置摄像头竖屏变换,顺旋转90度
//                for (int y = 0; y < height; y++) {
//                    for (int x = 0; x < width; x++)
//                        rotate[x * height + height - y - 1] = data[x + y * width];
//                }
//            }
//            int tmp = width;
//            width = height;
//            height = tmp;
//        } else {
//            if (isFrontCamera) {
//                //前置横屏水平翻转<左右对换>
//                for (int y = 0; y < height; y++) {
//                    for (int x = 0; x < width; x++) {
//                        rotate[width - 1 - x + y * width] = data[x + y * width];
//                    }
//                }
//            } else {
//                System.arraycopy(data, 0, rotate, 0, data.length);
//            }
//        }
//        // Go ahead and assume it's YUV rather than die.
//        return new PlanarYUVLuminanceSource(rotate, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
//    }
//
//
//
//
//    /**
//     * 设置相机预览旋转角度
//     *
//     * @param context
//     * @param cameraId
//     * @param camera
//     */
//    public void setCameraDisplayOrientation(Context context,
//                                            int cameraId, android.hardware.Camera camera) {
//        android.hardware.Camera.CameraInfo info =
//                new android.hardware.Camera.CameraInfo();
//        android.hardware.Camera.getCameraInfo(cameraId, info);
//        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        int rotation = manager.getDefaultDisplay().getRotation();
//        int degrees = 0;
//        switch (rotation) {
//            case Surface.ROTATION_0:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_90:
//                degrees = 90;
//                break;
//            case Surface.ROTATION_180:
//                degrees = 0;
//                break;
//            case Surface.ROTATION_270:
//                degrees = 90;
//                break;
//        }
//
//        isPortrait = degrees == 0;
//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }
//        camera.setDisplayOrientation(result);
//    }
//
//}
