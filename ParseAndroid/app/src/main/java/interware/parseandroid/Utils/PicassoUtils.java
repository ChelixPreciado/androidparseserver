package interware.parseandroid.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import interware.parseandroid.R;

/**
 * Created by chelixpreciado on 7/22/16.
 */
public class PicassoUtils {

    public static void loadImage(final Context context, final String imgUrl, final ImageView imageView){
        Picasso.with(context)
                .load(imgUrl)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .noPlaceholder()
                .resize(450,400)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(imgUrl)
                                .error(R.drawable.image_not_found)
                                .noPlaceholder()
                                .resize(450,400)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Chelix","Picasso Could not fetch image");
                                    }
                                });
                    }
                });
    }

}
