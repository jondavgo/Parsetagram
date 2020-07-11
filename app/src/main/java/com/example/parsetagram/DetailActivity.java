package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private boolean liked;
    private List<Comment> comments;
    private CommentsAdapter adapter;
    private Post post;
    ImageView ivPic;
    TextView tvUser;
    TextView tvDesc;
    TextView tvDate;
    ImageView ivProfilePic;
    ImageView ivHeart;
    TextView tvLikes;
    RecyclerView rvComment;
    ImageView ivComment;
    TextView etComment;


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
        tvLikes = findViewById(R.id.tvLikes);
        rvComment = findViewById(R.id.rvComment);
        ivComment = findViewById(R.id.ivComment);
        etComment = findViewById(R.id.etComment);
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(this, comments);

        rvComment.setAdapter(adapter);
        rvComment.setLayoutManager(new LinearLayoutManager(this));

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
        updateComments();

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

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etComment.getText().toString();
                if(description.equals("")){
                    Toast.makeText(DetailActivity.this, "Can't comment with an empty description!", Toast.LENGTH_SHORT).show();
                    return;
                }
                postComment(ParseUser.getCurrentUser(), description);
            }
        });
    }

    private void updateComments() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_POST);
        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_POST, post);
        query.orderByDescending(Comment.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                comments.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void postComment(ParseUser currentUser, String description) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(currentUser);
        comment.setText(description);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e("DetailActivity", "Error while saving comment", e);
                    Toast.makeText(DetailActivity.this, "Error while commenting!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(DetailActivity.this, "Post saved!", Toast.LENGTH_SHORT).show();
                etComment.setText("");
            }
        });
        comments.add(0, comment);
        adapter.notifyItemInserted(0);
    }

    private void updateLikes() {
        liked = post.getLikes().toString().contains(ParseUser.getCurrentUser().getObjectId());
        int likeCount = post.getLikes().length();
        String likeWord = " Like";
        if(likeCount != 1){
            likeWord += "s";
        }
        tvLikes.setText(likeCount + likeWord);
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