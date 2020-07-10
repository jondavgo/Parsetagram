package com.example.parsetagram.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.Date;

@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESC = "description";
    public static final String KEY_IMG = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_DATE = "createdAt";
    public static final String KEY_PFP = "profilePic";
    public static final String KEY_LIKES = "likes";

    public Post(){ }

    public String getDescription(){
        return getString(KEY_DESC);
    }

    public void setDescription(String description){
        put(KEY_DESC, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMG);
    }

    public void setImage(ParseFile image){
        put(KEY_IMG, image);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseFile getPfp(){
        ParseUser user = getUser();
        return user.getParseFile(KEY_PFP);
    }

    public JSONArray getLikes(){
        return getJSONArray(KEY_LIKES);
    }

    public void setLikes(JSONArray array){
        put(KEY_LIKES, array);
    }

    public void addLike(){
        setLikes(getLikes().put(ParseUser.getCurrentUser()));
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Post", getLikes().toString());
            }
        });
    }

    public void removeLike() throws JSONException {
        JSONArray likes = getLikes();
        int pos = -1;
        for(int i = 0; i < likes.length(); i++){
            if(likes.get(i).toString().contains(ParseUser.getCurrentUser().getObjectId())){
                pos = i;
                break;
            }
        }
        if(pos != -1) {
            likes.remove(pos);
        }
        setLikes(likes);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i("Post", getLikes().toString());
            }
        });
    }
}
