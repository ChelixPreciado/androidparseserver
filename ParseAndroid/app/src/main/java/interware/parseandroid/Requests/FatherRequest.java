package interware.parseandroid.Requests;

import interware.parseandroid.Retrofit.API;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chelixpreciado on 7/25/16.
 */
public class FatherRequest {

    private static final String baseUrl = "https://post.imageshack.us";
    private Retrofit retrofit;
    private API api;

    public FatherRequest() {
    }

    protected Retrofit getRetrofitInstance(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    protected API getApi() {
        if (api==null)
            api = getRetrofitInstance().create(API.class);
        return api;
    }
}
