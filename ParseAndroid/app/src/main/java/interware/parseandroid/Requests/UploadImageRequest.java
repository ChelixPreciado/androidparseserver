package interware.parseandroid.Requests;

import android.util.Log;

import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chelixpreciado on 7/25/16.
 */
public class UploadImageRequest extends FatherRequest {

    private UploadImageRequestListener listener;

    public UploadImageRequest(UploadImageRequestListener listener) {
        this.listener = listener;
    }

    public void uploadImage(File file){

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part fileBody =
                MultipartBody.Part.createFormData("fileupload", file.getName(), requestFile);
        RequestBody formatBody = RequestBody.create(MediaType.parse("text/plain"), "json");

        Call<ResponseBody> call = getApi().uploadImage("12DJKPSU5fc3afbd01b1630cc718cae3043220f3",
                formatBody, fileBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("Upload", "success");
                try {
                    String responseString = new String(response.body().bytes());
                    listener.onImageUploaded(getImageUrl(responseString));
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                listener.onError(t.getMessage());
            }
        });
    }

    public interface UploadImageRequestListener{
        public void onImageUploaded(String imageUrl);
        public void onError(String errorMsg);
    }

    private String getImageUrl(String jsonResponse){
        JsonReader reader = new JsonReader(new StringReader(jsonResponse));
        try {
            String name = "";
            reader.beginObject();
            while (reader.hasNext()){
                name = reader.nextName();
                if (name.equals("links")){
                    reader.beginObject();
                    while (reader.hasNext()){
                        name = reader.nextName();
                        if (name.equals("image_link"))
                            return reader.nextString();
                        else
                            reader.skipValue();
                    }
                    reader.endObject();
                }else
                    reader.skipValue();
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
