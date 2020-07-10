package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private boolean liked;
    ImageView ivPic;
    TextView tvUser;
    TextView tvDesc;
    TextView tvDate;
    ImageView ivProfilePic;
    ImageView ivHeart;
    Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivPic = findViewById(R.id.ivPhoto);
        tvUser = findViewById(R.id.tvUser);
        tvDesc = findViewById(R.id.tvDesc);
        tvDate = findViewById(R.id.tvDate);
        ivHeart = findViewById(R.id.ivHeart);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));

        tvUser.setText(post.getUser().getUsername());
        tvDesc.setText(post.getDescription());
        Date date = post.getCreatedAt();
        String pattern = "'Created' dd/MM/yyyy 'at' hh:mm a";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        tvDate.setText(format.format(date));
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(this).load(image.getUrl()).into(ivPic);
        }
        ParseFile pfp = post.getPfp();
        if(pfp != null) {
            Log.i("PostsAdapter", "Profile picture: " + pfp.toString());
            Glide.with(this).load(pfp.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
        }
        updateLikes();

        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!liked){
                    post.addLike();
                } else {
                    try {
                        post.removeLike();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                updateLikes();
            }
        });
    }

    private void updateLikes() {
        liked = post.getLikes().toString().contains(ParseUser.getCurrentUser().getObjectId());
        if(liked){
            ivHeart.setImageDrawable(getDrawable(R.drawable.ufi_heart_active));
            ivHeart.setColorFilter(Color.argb(255, 255, 0,0));
            Log.i("Detail", "Liked!");
        } else{
            ivHeart.setImageDrawable(getDrawable(R.drawable.ufi_heart));
            ivHeart.setColorFilter(Color.argb(255, 0, 0,0));
            Log.i("Detail", "NOT Liked!");
        }
    }
}