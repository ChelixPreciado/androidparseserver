package interware.parseandroid.Utils;

/**
 * Created by chelixpreciado on 7/20/16.
 */
public class ParseUtils {

    public static final String PARSE_APP_ID = "fadf15b39ca747618301e20e257c7577";
    public static final String PARSE_CLIENT_KEY = "214d0b849f6b4f82b13b94564bfa89c1";
    public static final String PARSE_SERVER_URL = "http://mysterious-mesa-18775.herokuapp.com/parse/";


    //Send push
    /*
    curl -X POST \
            -H "X-Parse-Application-Id: fadf15b39ca747618301e20e257c7577" \
            -H "X-Parse-Master-Key: 3b29f713c58946758ddb7b9baadad6d7" \
            -H "Content-Type: application/json" \
            -d '{
            "where": {
        "deviceType": {
            "$in": [
            "android"
            ]
        }
    },
            "data": {
        "title": "Pust test",
                "alert": "Notificacion a android desde Push Server."
    }
}'\   http://mysterious-mesa-18775.herokuapp.com/parse/push
*/

    //cpX9CNLEXiQ:APA91bGnM0iVVu3zPmrD_X-UaxRun-GLkxmXhX1sEam234aO8qz11TdkfqhkIOodFoVp4cThBnLdN0_b-NqksEJU4P3dLxnO_KCxWZHuT5UdqUR9P-WpTZzaRzYWq-76CNgq46Iay52N


    /*
    curl -X GET \
  -H "X-Parse-Application-Id: fadf15b39ca747618301e20e257c7577" \
  -H "X-Parse-Master-Key: 3b29f713c58946758ddb7b9baadad6d7" \
  http://mysterious-mesa-18775.herokuapp.com/parse/installations
     */

    /*
    curl -X POST -H "X-Parse-Application-Id: fadf15b39ca747618301e20e257c7577" -H "X-Parse-Master-Key: 3b29f713c58946758ddb7b9baadad6d7" \
-H "Content-Type: application/json" \
-d '{"where": {"deviceType": "android"}}' \
http://mysterious-mesa-18775.herokuapp.com/parse/push/
     */

    //https://github.com/InterwareMoviles/Android.git

    //parse-dashboard --appId fadf15b39ca747618301e20e257c7577 --masterKey 3b29f713c58946758ddb7b9baadad6d7 --serverURL "http://mysterious-mesa-18775.herokuapp.com/parse"
}
