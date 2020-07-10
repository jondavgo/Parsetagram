package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    ImageView ivPic;
    TextView tvUser;
    TextView tvDesc;
    TextView tvDate;
    Post post;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ivPic = findViewById(R.id.ivPhoto);
        tvUser = findViewById(R.id.tvUser);
        tvDesc = findViewById(R.id.tvDesc);
        tvDate = findViewById(R.id.tvDate);
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
    }
}