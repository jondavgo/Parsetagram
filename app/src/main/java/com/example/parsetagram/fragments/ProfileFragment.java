package com.example.parsetagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parsetagram.R;
import com.example.parsetagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends PostsFragment {

    public String photoFileName = "photo.jpg";
    public File photoFile;

    private TextView tvUser;
    private ImageView ivPfp;
    private ParseUser user;
    private Button changePfp;

    public ProfileFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivPfp = view.findViewById(R.id.ivProfilePic);
        tvUser = view.findViewById(R.id.tvUser);
        changePfp = view.findViewById(R.id.btnChangePfp);
        user = ParseUser.getCurrentUser();

        ParseFile pfp = user.getParseFile(Post.KEY_PFP);
        if(pfp != null) {
            Log.i("PostsAdapter", "Profile picture: " + pfp.toString());
            Glide.with(getContext()).load(pfp.getUrl()).transform(new CircleCrop()).into(ivPfp);
        }
        tvUser.setText(user.getUsername());
        changePfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, ComposeFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ComposeFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //Bitmap takenImage = ComposeFragment.rotateBitmapOrientation(photoFile.getAbsolutePath());
                Glide.with(getContext()).load(photoFile.getAbsolutePath()).transform(new CircleCrop()).into(ivPfp);
                user.put(Post.KEY_PFP, new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.i(TAG,"saved!");
                        refreshQuery();
                    }
                });
                Log.i("ProfileFragment", "Current pfp: " + user.getParseFile(Post.KEY_PFP));
            } else { // Result was a failure
                Toast.makeText(getContext(), "Profile Picture cannot be changed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with Post Query", e);
                    return;
                }
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void loadMoreData(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(MAX_POSTS);
        query.addDescendingOrder(Post.KEY_DATE);
        query.setSkip(MAX_POSTS * page);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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