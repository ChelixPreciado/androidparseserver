package interware.parseandroid.models;

import interware.parseandroid.Utils.CryptoUtil;

/**
 * Created by chelixpreciado on 7/21/16.
 */
public class User {

    private String username;
    private String password;
    private String provider;

    public User() {
    }

    public User(String username, String password, String provider) {
        this.username = username;
        this.password = password;
        this.provider = provider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return CryptoUtil.md5(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
