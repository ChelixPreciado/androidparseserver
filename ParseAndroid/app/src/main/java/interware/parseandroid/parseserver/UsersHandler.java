package interware.parseandroid.parseserver;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import interware.parseandroid.models.User;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class UsersHandler {

    public interface logInUserCallback{public void isLogguedIn(boolean loggedIn); public void onError(String errorMsg);}
    public interface registerUserCallback{public void isRegistered(boolean registered);}

    public static void logInUser(final User user, final logInUserCallback callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("users").whereMatches("username", user.getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.isEmpty()) {
                    callback.isLogguedIn(false);
                    callback.onError("Usuario no registrado");
                    return;
                }
                if (!(user.getPassword().equals(objects.get(0).getString("password"))))
                    callback.onError("La contraseña que ha introducido es incorrecta");
                callback.isLogguedIn((user.getPassword().equals(objects.get(0).getString("password"))));
            }
        });
    }

    public static void registerUser(User user, final registerUserCallback callback){
        ParseObject userPObject = new ParseObject("users");
        userPObject.put("provider", user.getProvider());
        userPObject.put("username", user.getUsername());
        userPObject.put("password", user.getPassword());
        userPObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.isRegistered(e!=null);
            }
        });
    }

}
