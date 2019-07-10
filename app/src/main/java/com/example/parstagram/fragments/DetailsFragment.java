package com.example.parstagram.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;

public class DetailsFragment extends Fragment {

    private Post post;
    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvTime;

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

        // populate the views based on post details
        String username = post.getUser().getUsername();
        tvHandle.setText(username);
        String sourceString = "<b>" + username + "</b> " + post.getDescription();
        tvDescription.setText(Html.fromHtml(sourceString));
        tvTime.setText(post.getTime());

        // if there is an image with the post, load it into the image view
        if(post.getImage() != null) {
            Glide.with(this)
                    .load(post.getImage().getUrl())
                    .into(ivImage);
        }
    }
}
