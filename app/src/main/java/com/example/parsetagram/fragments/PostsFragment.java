package com.example.parsetagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.parsetagram.EndlessRecyclerViewScrollListener;
import com.example.parsetagram.PostsAdapter;
import com.example.parsetagram.R;
import com.example.parsetagram.databinding.FragmentPostsBinding;
import com.example.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {

    public static final int MAX_POSTS = 20;
    public static final String TAG = PostsFragment.class.getSimpleName();
    private RecyclerView rvPosts;
    protected List<Post> posts;
    protected PostsAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        posts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), posts);

        // Set up Recycler View
        rvPosts.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(rvPosts.getContext(), manager.getOrientation());
        rvPosts.addItemDecoration(decoration);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.i(TAG, "Loading page: " + page);
                loadMoreData(page);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts();

        // Add swipe-to-refresh
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshQuery();
            }
        });
    }

    protected void loadMoreData(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.setSkip(MAX_POSTS * page);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with Post Query", e);
                    return;
                }
                adapter.addAll(objects);
            }
        });
    }

    private void refreshQuery() {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with Post Query", e);
                    return;
                }
                adapter.addAll(objects);
            }
        });
    }
}