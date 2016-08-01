package interware.parseandroid.main;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseInstallation;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import interware.parseandroid.Utils.ParseUtils;

/**
 * Created by chelixpreciado on 7/20/16.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        parseInit();
        picassoInit();
    }

    private void parseInit(){
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(ParseUtils.PARSE_APP_ID)
                .server(ParseUtils.PARSE_SERVER_URL)
                .clientKey(ParseUtils.PARSE_CLIENT_KEY)
                .build());

        HashMap<String, String> test = new HashMap<>();
        test.put("channel", "testing");

        ParseCloud.callFunctionInBackground("pushChannelTest", test);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void picassoInit(){
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
