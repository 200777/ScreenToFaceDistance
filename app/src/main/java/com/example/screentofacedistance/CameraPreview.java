package com.example.screentofacedistance;


import android.hardware.Camera;
import android.graphics.Rect;
import java.io.IOException;
import java.util.ArrayList;
import android.hardware.Camera.Size;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;
import com.example.screentofacedistance.MessageActionProducer;
import com.example.screentofacedistance.IMessageActionListener;
import com.example.screentofacedistance.Measurement;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.annotation.SuppressLint;

import android.graphics.Paint;
import android.media.FaceDetector.Face;
import android.util.Log;

import android.graphics.PointF;


import java.util.List;

import static android.content.ContentValues.TAG;



public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private Face faceFound = null;
    private Size size;
    private float calibrationPointDistance = -1;
    protected final Paint colorMarker = new Paint();
    private List<ScreenPoints> screenPoints;
    private int threshold = 10;
    public float distToFaceCurr = -1;
    private float eyeDistanceAverage = -1;
    private boolean operationInProgress = false;
    private int operationsLeft = -1;
    private Camera cameraUsed;
    private IdentifyFace currentIdentifyFaceThread;
    private final SurfaceHolder surfaceHolder;
    private boolean operationCompleted = false;
    private PointF mP = new PointF();
    private Rect tR = new Rect();

    public void operate() {
        if (!operationInProgress || !operationCompleted) {
            screenPoints = new ArrayList<ScreenPoints>();
            operationInProgress = true;
            operationsLeft = 10;
            threshold = 10;
        }
    }

    public void reset() {
        calibrationPointDistance = -1;
        eyeDistanceAverage = -1;
        operationCompleted = false;
        operationInProgress = false;
        operationsLeft = -1;
    }

    public CameraPreview(Context ctx, Camera c) {
        super(ctx);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        colorMarker.setARGB(100, 159, 90, 253);
        colorMarker.setStyle(Paint.Style.FILL);
        colorMarker.setStrokeWidth(3);
    }

    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(1);
        }
        catch (Exception e){
            // Camera not available
        }
        return c;
    }


    public void setCamera(final Camera camera) {
        cameraUsed = camera;
        if (cameraUsed != null) {
            requestLayout();
            Parameters params = cameraUsed.getParameters();
            camera.setDisplayOrientation(90);
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                cameraUsed.setParameters(params);
            }
        }
    }

    private void calculationUpdate(Face currFace) {
        if (currFace == null) {
            return;
        }

        faceFound = currentIdentifyFaceThread.faceGet();
        screenPoints.add(new ScreenPoints(faceFound.eyesDistance(),
                294 * (calibrationPointDistance / faceFound.eyesDistance())));

        while (screenPoints.size() > threshold) {
            screenPoints.remove(0);
        }

        float calcSum = 0;
        for (ScreenPoints pts : screenPoints) {
            calcSum += pts.distanceEyesGet();
        }

        eyeDistanceAverage = calcSum / screenPoints.size();

        distToFaceCurr = 294 * (calibrationPointDistance / eyeDistanceAverage);
        distToFaceCurr = distToFaceCurr / 10;
        Measurement msg = new Measurement();
        msg.setConf(currFace.confidence());
        msg.setAverageDistance(eyeDistanceAverage);
        msg.distanceToFaceSet(distToFaceCurr);
        msg.distanceToEyeSet(currFace.eyesDistance());
        msg.leftMeasurementsSet(operationsLeft);

        MessageActionProducer.get().messageSend(MessageActionProducer.MEASUREMENT_STEP, msg);

    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas cnv) {
        if (faceFound != null) {
            faceFound.getMidPoint(mP);
            float height = cnv.getHeight() / (float) size.width;
            float width = cnv.getWidth() / (float) size.height;
            int xReal = (int) (mP.x * height);
            int yReal = (int) (mP.y * width);
            tR.left = xReal - 20;
            tR.top = yReal - 20;
            tR.right = xReal + 20;
            tR.bottom = yReal + 20;
            cnv.drawRect(tR, colorMarker);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.setWillNotDraw(false);
        try {
            cameraUsed.setPreviewDisplay(surfaceHolder);
            cameraUsed.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraUsed.release();
        cameraUsed = null;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int f, int w, int h) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            cameraUsed.stopPreview();
        } catch (Exception e) {
        }
        Parameters params = cameraUsed.getParameters();
        size = params.getPreviewSize();

        try {
            cameraUsed.setPreviewDisplay(surfaceHolder);
            cameraUsed.startPreview();
            cameraUsed.setPreviewCallback(this);
        } catch (Exception e) {
            Log.d("This", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onPreviewFrame(byte[] d, Camera cam) {
        if (operationsLeft == -1)
            return;

        if(operationsLeft > 0) {
            if (currentIdentifyFaceThread != null && currentIdentifyFaceThread.isAlive()) {
                return;
            }

            if (currentIdentifyFaceThread != null) {
                if(operationsLeft == 10)
                {
                    operationsLeft--;
                    calculationUpdate(currentIdentifyFaceThread.faceGet());
                }
                operationsLeft--;
                calculationUpdate(currentIdentifyFaceThread.faceGet());
                if (operationsLeft == 0) {
                    doneCalibrating();

                    invalidate();
                    return;
                }
            }


            currentIdentifyFaceThread = new IdentifyFace(d, size);
            currentIdentifyFaceThread.start();
            invalidate();
        } else {
            if (currentIdentifyFaceThread != null
                    && currentIdentifyFaceThread.isAlive()) {
                return;
            }



            if (currentIdentifyFaceThread != null)
                calculationUpdate(currentIdentifyFaceThread.faceGet());

            currentIdentifyFaceThread = new IdentifyFace(d, size);
            currentIdentifyFaceThread.start();
            invalidate();
        }
    }

    private void doneCalibrating() {
        operationCompleted = true;
        operationInProgress = false;
        threshold = 5;
        currentIdentifyFaceThread = null;
        calibrationPointDistance = eyeDistanceAverage;
        MessageActionProducer.get().messageSend(MessageActionProducer.DONE_CALIBRATION, null);
    }
}
