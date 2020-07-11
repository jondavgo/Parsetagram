package com.example.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    protected Context context;
    protected List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private boolean liked;
        ImageView ivPic;
        TextView tvUser;
        TextView tvDesc;
        ImageView ivProfilePic;
        TextView tvDate;
        ImageView ivHeart;
        LinearLayout llUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.ivPhoto);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            llUser = itemView.findViewById(R.id.llUser);
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            tvUser.setText(post.getUser().getUsername());
            tvDesc.setText(post.getDescription());
            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivPic);
            }
            ParseFile pfp = post.getPfp();
            if(pfp != null) {
                Log.i("PostsAdapter", "Profile picture: " + pfp.toString());
                Glide.with(context).load(pfp.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            }
            Date date = post.getCreatedAt();
            String pattern = "'Created' dd/MM/yyyy 'at' hh:mm a";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            tvDate.setText(format.format(date));
            updateLikes(post);

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
                    updateLikes(post);
                }
            });
            llUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Post post = posts.get(pos);
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("post", Parcels.wrap(post));
                        context.startActivity(intent);
                    }
                }
            });
        }

        private void updateLikes(Post post) {
            liked = post.getLikes().toString().contains(ParseUser.getCurrentUser().getObjectId());
            if(liked){
                ivHeart.setImageDrawable(context.getDrawable(R.drawable.ufi_heart_active));
                ivHeart.setColorFilter(Color.argb(255, 255, 0,0));
            } else{
                ivHeart.setImageDrawable(context.getDrawable(R.drawable.ufi_heart));
                ivHeart.setColorFilter(Color.argb(255, 0, 0,0));
            }
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION){
                Post post = posts.get(pos);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("post", Parcels.wrap(post));
                context.startActivity(intent);
            }
        }
    }
}
