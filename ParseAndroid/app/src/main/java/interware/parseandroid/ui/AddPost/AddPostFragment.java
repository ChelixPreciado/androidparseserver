package interware.parseandroid.ui.AddPost;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import interware.parseandroid.R;
import interware.parseandroid.Requests.UploadImageRequest;
import interware.parseandroid.Utils.ImageUtils;
import interware.parseandroid.Utils.LoaderUtils;
import interware.parseandroid.Utils.PicassoUtils;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddPostFragment extends DialogFragment implements View.OnClickListener {

    private TextView txtPost;
    private EditText edPostText;
    private ViewGroup btnAddPick, vgTakenPicture;
    private ImageView ivPicture;
    private String mPath;
    private static String MEDIA_DIRECTORY = "parseserver/pictures";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static int MY_PERMISSIONS = 200;
    private LoaderUtils loaderUtils;
    private String postImageUrl = null;
    private AddPostFragmentListener listener;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (AddPostFragmentListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddPostFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
                if (edPostText.getText().toString().length()>0) {
                    listener.onPostWrited(edPostText.getText().toString().trim(), postImageUrl);
                    dismiss();
                }else {
                    Toast.makeText(getActivity().getApplicationContext(), "Debes de escribir una publicacion", Toast.LENGTH_SHORT).show();
                    getLoaderUtils().showLoader(false);
                }break;
            case R.id.btn_add_pick:
                openCamera();
                break;
        }
    }

    private void uploadImage(File imageFile){
        UploadImageRequest uploadImageRequest = new UploadImageRequest(new UploadImageRequest.UploadImageRequestListener() {
            @Override
            public void onImageUploaded(String imageUrl) {
                getLoaderUtils().showLoader(false);
                vgTakenPicture.setVisibility(View.VISIBLE);
                btnAddPick.setVisibility(View.GONE);
                PicassoUtils.loadImage(getActivity().getApplicationContext(), imageUrl, ivPicture);
                postImageUrl = imageUrl;
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                getLoaderUtils().showLoader(false);
            }
        });
        uploadImageRequest.uploadImage(imageFile);
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

            uploadImage(ImageUtils.saveImageInPath(mPath));
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

    private LoaderUtils getLoaderUtils(){
        if (loaderUtils==null)
            loaderUtils = new LoaderUtils(getActivity());
        return loaderUtils;
    }

}
