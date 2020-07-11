package com.example.parsetagram;

import android.app.Application;

import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParsetagramApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("gomez-parsetagram") // should correspond to APP_ID env variable
                .clientKey("TheGloatingStorm0")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://gomez-parsetagram.herokuapp.com/parse/").build());
    }
}
