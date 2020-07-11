package com.example.parsetagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.example.parsetagram.fragments.PostsFragment.MAX_POSTS;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView rvPosts;
    ParseUser user;
    ProfileAdapter adapter;
    private TextView tvUser;
    private ImageView ivPfp;
    EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        rvPosts = findViewById(R.id.rvPosts);
        swipeContainer = findViewById(R.id.swipeContainer);
        ivPfp = findViewById(R.id.ivProfilePic);
        tvUser = findViewById(R.id.tvUser);
        posts = new ArrayList<>();
        adapter = new ProfileAdapter(this, posts);
        Post post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        user = post.getUser();

        ParseFile pfp = user.getParseFile(Post.KEY_PFP);
        if(pfp != null) {
            Log.i("ProfileAdapter", "Profile picture: " + pfp.toString());
            Glide.with(this).load(pfp.getUrl()).transform(new CircleCrop()).into(ivPfp);
        }
        tvUser.setText(user.getUsername());

        rvPosts.setAdapter(adapter);
        LinearLayoutManager manager = new GridLayoutManager(this, 3);
        rvPosts.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i("ProfileActivity", "Loading page: " + page);
                loadMoreData(page);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e("ProfileActivity", "Issue with Post Query", e);
                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    protected void loadMoreData(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.setSkip(MAX_POSTS * page);
        query.whereEqualTo(Post.KEY_USER, user);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e("ProfileActivity", "Issue with Post Query", e);
                    return;
                }
                adapter.addAll(objects);
            }
        });
    }
}