package interware.parseandroid.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chelixpreciado on 7/27/16.
 */
public class ImageUtils {

    /**
     * Save a lower resolution bitmap in the sended path
     * @param path the path where the file is stored
     * @return a File form the image stored in path
     */
    public static File saveImageInPath(String path){
        Log.i("Chelix", "lowerResolution: path " + path);
        File mImageFile = new File(path);

        Bitmap croppedBmp = null;
        try {
            croppedBmp = fixImageOrientation(path);
            FileOutputStream fOut = new FileOutputStream(mImageFile);
            croppedBmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            Log.i("Chelix", "No se encontro el path a la imagen: " + '\n' + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Chelix", "Error: " + e.getMessage() + '\n');
        }

        return mImageFile;

    }

    /**
     * Fiz teh image Orientation
     * @param path the path where the file is stored
     * @return the fixed bitmap
     * @throws IOException exception file not found
     */
    public static Bitmap fixImageOrientation(String path) throws IOException {
        File f = new File(path);
        ExifInterface exif = new ExifInterface(f.getPath());
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        int angle = 0;

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            angle = 90;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            angle = 180;
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            angle = 270;
        }

        Matrix mat = new Matrix();
        mat.postRotate(angle);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;

        Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
                null, options);

        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), mat, true);
        //Center crop image
        if (bmp.getWidth() >= bmp.getHeight()){

            return Bitmap.createBitmap(
                    bmp,
                    bmp.getWidth()/2 - bmp.getHeight()/2,
                    0,
                    bmp.getHeight(),
                    bmp.getHeight()
            );

        }else{
            return Bitmap.createBitmap(
                    bmp,
                    0,
                    bmp.getHeight()/2 - bmp.getWidth()/2,
                    bmp.getWidth(),
                    bmp.getWidth()
            );
        }
    }

}
