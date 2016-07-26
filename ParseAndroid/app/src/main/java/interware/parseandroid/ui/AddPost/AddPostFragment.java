package interware.parseandroid.ui.AddPost;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import interware.parseandroid.R;
import interware.parseandroid.Requests.UploadImageRequest;
import interware.parseandroid.Utils.LoaderUtils;
import interware.parseandroid.parseserver.PostsHandler;
import interware.parseandroid.ui.ParseappActivity;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddPostFragment extends DialogFragment implements View.OnClickListener {

    private TextView txtPost;
    private EditText edPostText;
    private ViewGroup btnAddPick, vgTakenPicture;
    private ImageView ivPicture;
    private File mImageFile;
    private String mPath;
    private static String MEDIA_DIRECTORY = "parseserver/pictures";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int MY_PERMISSIONS = 200;

    public AddPostFragment() {
        // Required empty public constructor
    }

    public static AddPostFragment newInstance() {
        AddPostFragment fragment = new AddPostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog_Transparents);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) {
            return;
        }
        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_enterup_exitbotton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.fragment_add_post, container, false);
        txtPost = (TextView)dialogView.findViewById(R.id.btn_post);
        edPostText = (EditText)dialogView.findViewById(R.id.ed_posttext);
        btnAddPick = (ViewGroup)dialogView.findViewById(R.id.btn_add_pick);
        ivPicture = (ImageView)dialogView.findViewById(R.id.iv_picture);
        vgTakenPicture = (ViewGroup)dialogView.findViewById(R.id.vg_taken_picture);
        return dialogView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPost.setOnClickListener(this);
        btnAddPick.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_post:
                getLoaderUtils().showLoader(true);
                if (edPostText.getText().toString().length()>0)
                    doPost(edPostText.getText().toString().trim());
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "Debes de escribir una publicacion", Toast.LENGTH_SHORT).show();
                    getLoaderUtils().showLoader(false);
                }break;
            case R.id.btn_add_pick:
                openCamera();
                break;
        }
    }

    private void doPost(String postMessage){
        if (mImageFile==null){
            PostsHandler.doPost(postMessage, new PostsHandler.postedPost() {
                @Override
                public void posted(boolean posted) {
                    getLoaderUtils().showLoader(false);
                    dismiss();
                }
            });
        }else{
            UploadImageRequest uploadImageRequest = new UploadImageRequest(new UploadImageRequest.UploadImageRequestListener() {
                @Override
                public void onImageUploaded() {
                    getLoaderUtils().showLoader(false);
                    dismiss();
                }

                @Override
                public void onError(String errorMsg) {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    getLoaderUtils().showLoader(false);
                    dismiss();
                }
            });
            uploadImageRequest.uploadImage(mImageFile);
        }
    }

    private LoaderUtils loaderUtils;

    private LoaderUtils getLoaderUtils(){
        if (loaderUtils==null)
            loaderUtils = new LoaderUtils(getActivity());
        return loaderUtils;
    }

    private void openCamera(){
        if(mayRequestPermission()){
            File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
            boolean isDirectoryCreated = file.exists();

            if (!isDirectoryCreated)
                isDirectoryCreated = file.mkdirs();

            if (isDirectoryCreated) {
                Long timestamp = System.currentTimeMillis() / 1000;
                String imageName = timestamp.toString() + ".png";

                mPath = Environment.getExternalStorageDirectory() + File.separator + MEDIA_DIRECTORY
                        + File.separator + imageName;

                File newFile = new File(mPath);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }else
            Toast.makeText(getActivity(), "Para poder tomar una foto necesitas tener " +
                    "habilitados los permisos para usar la cámara ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{mPath}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;

            //mBitmap = BitmapFactory.decodeFile(mPath);
            //mImageFile = new File(mPath);

            lowerResolution(mPath);
        }
    }

    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if (getActivity().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED)
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            alertView("Necesitas otorgar permisos para usar la camara");
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity().getApplicationContext());

        dialog.setTitle( "No Autorizado" )
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialoginterface, int i) {
                        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                    }
                }).show();

    }

    private void lowerResolution(String path){
        Log.i("Chelix", "lowerResolution: path " + path);
        mImageFile = new File(path);

        Bitmap croppedBmp = null;
        try {
            croppedBmp = Bitmap.createScaledBitmap(fixImageOrientation(path), 600, 600, false);
            FileOutputStream fOut = new FileOutputStream(mImageFile);
            croppedBmp.compress(Bitmap.CompressFormat.PNG, 50, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            Log.i("Chelix", "No se encontro el path a la imagen: " + '\n' + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Chelix", "Error: " + e.getMessage() + '\n');
        }

        ivPicture.setImageBitmap(croppedBmp);

    }

    public Bitmap fixImageOrientation(String path) throws IOException {
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
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                bmp.getHeight(), mat, true);
    }
}
