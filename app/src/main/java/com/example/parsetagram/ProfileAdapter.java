package com.example.parsetagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;

import com.bumptech.glide.Glide;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends PostsAdapter {

    public ProfileAdapter(Context context, List<Post> posts) {
        super(context, posts);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends PostsAdapter.ViewHolder{

        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }

        @Override
        public void bind(Post post) {
            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivPhoto);
            }
        }
    }
}
