package com.example.screentofacedistance;

import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.hardware.Camera.Size;
import android.graphics.YuvImage;
import android.util.Log;



public class IdentifyFace extends Thread {

    public static final String ACTIVITY_NAME = "IdentifyFace";
    private byte[] d;
    private Size size;
    private Face face;
    private Bitmap currFrame;
    private Boolean checker;
    private Bitmap.Config PREFERRED_CONFIG = Bitmap.Config.RGB_565;

    public IdentifyFace(byte[] d, Size size) {
        this.d = d;
        this.size = size;
    }

    public Face faceGet() {
        return face;
    }

    @Override
    public void run() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        YuvImage yI = new YuvImage(d, ImageFormat.NV21, size.width, size.height, null);
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.postRotate(90);
        rotationMatrix.preScale(-1, 1);
        BitmapFactory.Options bitMapFactory = new BitmapFactory.Options();
        bitMapFactory.inPreferredConfig = PREFERRED_CONFIG;
        checker = yI.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
        if (!checker) {

            Log.e("Compression", "Failure of compression to JPEG");

        }



        currFrame = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()), null, bitMapFactory);

        currFrame = Bitmap.createBitmap(currFrame, 0, 0,
                size.width, size.height, rotationMatrix, false);

        if (currFrame == null) {
            Log.e(ACTIVITY_NAME, "Image Decoding Failure");
            return;
        }

        FaceDetector faceDetector = new FaceDetector(currFrame.getWidth(), currFrame.getHeight(), 1);

        Face[] faceArray = new Face[1];
        faceDetector.findFaces(currFrame, faceArray);

        face = faceArray[0];
        Log.d(ACTIVITY_NAME, "Number of faces found are: " + faceArray[0]);
    }

}
