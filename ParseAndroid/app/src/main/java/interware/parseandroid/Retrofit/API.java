package interware.parseandroid.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by chelixpreciado on 7/25/16.
 */
public interface API {

    @Multipart
    @POST("upload_api.php")
    Call<ResponseBody> uploadImage(@Query("key") String key,
                                   @Part("format")RequestBody format,
                                   @Part("fileupload")MultipartBody.Part file);

}
