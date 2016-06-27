package io.github.tadoya.din9talk.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    private String userName;
    private String token;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    // [START users_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", userName);
        result.put("token", token);

        return result;
    }
    // [END users_to_map]
    public String getUserName() { return userName; }

    public String getToken() { return token; }

}
// [END blog_user_class]
