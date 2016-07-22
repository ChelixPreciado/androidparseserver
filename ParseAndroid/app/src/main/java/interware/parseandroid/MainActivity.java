package interware.parseandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();*/

       getPosts();
    }

    private void getPosts(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("posts");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null){
                    for (ParseObject parseObject : objects){
                        Log.i("Chelix", "Objeto encontrado: " + parseObject.getString("description"));
                    }
                }else{
                    Log.i("Chelix", "Error al tratar de obtener posts: " + e.getMessage());
                }
            }
        });
    }
}
