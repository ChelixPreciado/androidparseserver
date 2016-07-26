package interware.parseandroid.persistencia;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chelixpreciado on 7/22/16.
 */
public class UserSession {

    private SharedPreferences manager;
    private SharedPreferences.Editor editor;

    private static final String SESSION_TAG = "user_session_tag";

    private static final String TAG_USER_NAME = "user_username_tag";

    public UserSession(Context context) {
        manager = context.getSharedPreferences(SESSION_TAG, 0);
        editor = manager.edit();
    }

    public boolean isLoggedIn(){
        return getUserName()!=null;
    }

    public void saveUserName(String username){
        editor.putString(TAG_USER_NAME, username);
        editor.commit();
    }

    public String getUserName(){
        return manager.getString(TAG_USER_NAME, null);
    }

    public void logOut(){
        editor.remove(TAG_USER_NAME);
        editor.commit();
    }

}
