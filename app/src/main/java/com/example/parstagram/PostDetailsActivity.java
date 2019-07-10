package com.example.parstagram;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.parstagram.model.Post;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    private Post post;
    private TextView tvHandle;
    private ImageView ivImage;
    private TextView tvSmallHandle;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        // get and unwrap the parceled post from the intent (the clicked-on post)
        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        // lookup and assign views using findViewById
        tvHandle = findViewById(R.id.tvHandle);
        ivImage = findViewById(R.id.ivImage);
        tvSmallHandle = findViewById(R.id.tvSmallHandle);
        tvDescription = findViewById(R.id.tvDescription);

        // populate the views based on post details
        String username = post.getUser().getUsername();
        tvHandle.setText(username);
        tvSmallHandle.setText(username);
        tvDescription.setText(post.getDescription());

        // if there is an image with the post, load it into the image view
        if(post.getImage() != null) {
            Glide.with(this)
                    .load(post.getImage().getUrl())
                    .into(ivImage);
        }
    }
}
