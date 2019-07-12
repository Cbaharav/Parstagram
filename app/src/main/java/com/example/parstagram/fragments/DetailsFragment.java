package com.example.parstagram.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DetailsFragment extends Fragment {

    private Post post;
    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTime;
    private ImageButton btnLike;
    private TextView tvLikes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get and unwrap the parceled post from the intent (the clicked-on post)
        post = (Post) getArguments().get("post");

        // lookup and assign views using findViewById
        tvHandle = view.findViewById(R.id.tvHandle);
        ivImage = view.findViewById(R.id.ivImage);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvTime = view.findViewById(R.id.tvTime);
        btnLike = view.findViewById(R.id.btnLike);
        tvLikes = view.findViewById(R.id.tvLikes);

        // populate the views based on post details
        String username = post.getUser().getUsername();
        tvHandle.setText(username);
        String sourceString = "<b>" + username + "</b> " + post.getDescription();
        tvDescription.setText(Html.fromHtml(sourceString));
        tvTime.setText(post.getTime());
        tvLikes.setText(Integer.toString(post.getLikes().size()));

        // set initial image resource based on liked state
        if (post.hasLiked(ParseUser.getCurrentUser())) {
            btnLike.setImageResource(R.drawable.ufi_heart_active);
            btnLike.setColorFilter(Color.RED);
        } else {
            btnLike.setImageResource(R.drawable.ufi_heart);
            btnLike.setColorFilter(Color.BLACK);
        }

        // if there is an image with the post, load it into the image view
        if(post.getImage() != null) {
            Glide.with(this)
                    .load(post.getImage().getUrl())
                    .into(ivImage);
        }

        // set onClick listener for like button
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser user = ParseUser.getCurrentUser();
                // either like or unlike based on current like state
                if (post.hasLiked(user)) {
                    post.unLike(user);
                } else {
                    post.addLike(user);
                }
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("likes", "liking done");
                        // set image button to appropriate resource (filled/unfilled heart) based on liked state
                        if (post.hasLiked(ParseUser.getCurrentUser())) {
                            btnLike.setImageResource(R.drawable.ufi_heart_active);
                            btnLike.setColorFilter(Color.RED);
                        } else {
                            btnLike.setImageResource(R.drawable.ufi_heart);
                            btnLike.setColorFilter(Color.BLACK);
                        }
                        tvLikes.setText(Integer.toString(post.getLikes().size()));
                    }
                });
            }
        });
    }
}
