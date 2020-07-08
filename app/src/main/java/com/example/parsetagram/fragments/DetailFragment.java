package com.example.parsetagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parsetagram.R;
import com.example.parsetagram.models.Post;
import com.parse.ParseFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    ImageView ivPic;
    TextView tvUser;
    TextView tvDesc;
    TextView tvDate;
    Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPic = view.findViewById(R.id.ivPhoto);
        tvUser = view.findViewById(R.id.tvUser);
        tvDesc = view.findViewById(R.id.tvDesc);
        //post = getArguments().get
        tvUser.setText(post.getUser().getUsername());
        tvDesc.setText(post.getDescription());
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(getContext()).load(image.getUrl()).into(ivPic);
        }
    }
}