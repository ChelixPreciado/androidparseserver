package interware.parseandroid.parseserver;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import interware.parseandroid.models.Publicacion;
import interware.parseandroid.models.User;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class PostsHandler {

    public interface GetPostCallback{
        public void onPostsObtained(ArrayList<Publicacion> posts);
        public void onError(String errorMsg);
    }

    public interface postedPost{public void posted(boolean posted);}

    public static void getPosts(final GetPostCallback callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    ArrayList<Publicacion> posts = new ArrayList<Publicacion>();
                    for (ParseObject parseObject : objects){
                        Log.i("Chelix", "Objeto encontrado: " + parseObject.getString("description"));
                        Publicacion p = new Publicacion(parseObject.getString("description"), parseObject.getString("imageUrl"));
                        posts.add(p);
                    }
                    callback.onPostsObtained(posts);
                }else{
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    public static void doPost(String postMsg, final postedPost callback){
        ParseObject userPObject = new ParseObject("posts");
        userPObject.put("description", postMsg);
        userPObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.posted(e!=null);
            }
        });
    }

}
