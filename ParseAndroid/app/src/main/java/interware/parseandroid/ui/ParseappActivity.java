package interware.parseandroid.ui;

import android.support.v7.app.AppCompatActivity;

import interware.parseandroid.Utils.LoaderUtils;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class ParseappActivity extends AppCompatActivity {

    private LoaderUtils loaderUtils;

    private LoaderUtils getLoaderUtils(){
        if (loaderUtils==null)
            loaderUtils = new LoaderUtils(this);
        return loaderUtils;
    }

    protected void showLoader(boolean showLoader){
        getLoaderUtils().showLoader(showLoader);
    }

}
