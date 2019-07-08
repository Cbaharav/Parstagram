package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // notifying parse that this is a custom model
        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("spaghetti")
                .clientKey("temp123")
                .server("http://parstapound.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
