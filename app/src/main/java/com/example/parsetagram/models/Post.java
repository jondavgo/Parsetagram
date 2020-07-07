package com.example.parsetagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Post")
public class Post extends ParseObject {


    public static final String KEY_DESC = "description";
    public static final String KEY_IMG = "image";
    public static final String KEY_USER = "user";
}
