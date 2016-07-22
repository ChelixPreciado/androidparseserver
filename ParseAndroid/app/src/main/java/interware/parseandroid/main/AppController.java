package interware.parseandroid.main;

import android.app.Application;

import com.parse.Parse;

import interware.parseandroid.Utils.ParseUtils;

/**
 * Created by chelixpreciado on 7/20/16.
 */
public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(ParseUtils.PARSE_APP_ID)
                .server(ParseUtils.PARSE_SERVER_URL)
                .clientKey(ParseUtils.PARSE_CLIENT_KEY)
                .build());
    }
}
