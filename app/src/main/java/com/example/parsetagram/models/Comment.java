package com.example.parsetagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_TEXT = "text";
    private static final String KEY_PFP = "profilePic";

    public Comment(){ }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public Post getPost(){
        return (Post) getParseObject(KEY_POST);
    }

    public void setPost(Post post){
        put(KEY_POST, post);
    }

    public String getText(){
        return getString(KEY_TEXT);
    }

    public void setText(String text){
        put(KEY_TEXT, text);
    }

    public ParseFile getPfp(){
        ParseUser user = getUser();
        return user.getParseFile(KEY_PFP);
    }
}
