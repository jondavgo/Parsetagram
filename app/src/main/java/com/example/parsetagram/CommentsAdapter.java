package com.example.parsetagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.models.Comment;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvUser;
        private TextView tvText;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            tvUser = itemView.findViewById(R.id.tvUser);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }

        public void bind(Comment comment) {
            tvUser.setText(comment.getUser().getUsername());
            tvText.setText(comment.getText());
            ParseFile pfp = comment.getPfp();
            if(pfp != null) {
                Log.i("PostsAdapter", "Profile picture: " + pfp.toString());
                Glide.with(context).load(pfp.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            }
        }
    }
}
