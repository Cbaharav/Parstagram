package com.example.parstagram;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.fragments.DetailsFragment;
import com.example.parstagram.model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;


    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate view into parent, not attaching to root because the viewHolder does this
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    // bind the data into the viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get post at the current position
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size(); // number of items in data set
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvHandle;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfPic;
        private ImageButton btnLike;
        private TextView tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvSmallHandle);
            ivProfPic = itemView.findViewById(R.id.ivProfPic);
            btnLike = itemView.findViewById(R.id.btnLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    Post post = posts.get(position);
                    ParseUser user = ParseUser.getCurrentUser();
                    if (post.hasLiked(user)) {
                        post.unLike(user);
                        notifyItemChanged(position);
                    } else {
                        post.addLike(user);
                        notifyItemChanged(position);
                    }
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("likes", "liking done");
//                            notifyItemChanged(position);
                        }
                    });
                }
            });
            // setting onClickListener on the view (so items can have detail view)
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            String username = post.getUser().getUsername();
            tvHandle.setText(username);
            String sourceString = "<b>" + username + "</b> " + post.getDescription();
            tvDescription.setText(Html.fromHtml(sourceString));
            tvLikes.setText(Integer.toString(post.getLikes().size()));

            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .into(ivImage);
            }

            // loading in profile picture if you have one, if not loads in the default image
            ParseFile profPic = post.getProfilePic();
            if (profPic != null) {
                Glide.with(context)
                        .load(profPic.getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfPic);
            } else {
                Glide.with(context)
                        .load(R.drawable.default_profile)
                        .apply(RequestOptions.circleCropTransform())
                        .into(ivProfPic);
            }

            if (post.hasLiked(ParseUser.getCurrentUser())) {
                btnLike.setImageResource(R.drawable.ufi_heart_active);
                btnLike.setColorFilter(Color.RED);
            } else {
                btnLike.setImageResource(R.drawable.ufi_heart);
                btnLike.setColorFilter(Color.BLACK);
            }
        }

        @Override
        public void onClick(View view) {
            // get position of clicked post
            int position = getAdapterPosition();
            // make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position
                Post post = posts.get(position);

                // use parceler to put the post as an intent extra
                Bundle bundle = new Bundle();
                bundle.putSerializable("post", post);

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Fragment fragment = new DetailsFragment();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        }
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
}
