package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.parstagram.EndlessRecyclerViewScrollListener;
import com.example.parstagram.ProfilePostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView rvPosts;
    private ProfilePostsAdapter adapter;
    private List<Post> mPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    private ImageView ivProfPic;
    private TextView tvHandle;

    // onCreateView to inflate the view
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    // onViewCreated is after the view has been inflated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvPosts = view.findViewById(R.id.rvPosts);
        ivProfPic = view.findViewById(R.id.ivProfPic);
        tvHandle = view.findViewById(R.id.tvHandle);

        // set up profile picture & handle
        tvHandle.setText(ParseUser.getCurrentUser().getUsername());
        Glide.with(getContext())
                .load(ParseUser.getCurrentUser().getParseFile("profilePic").getUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfPic);

        // create the data source
        mPosts = new ArrayList<>();
        // create the adapter
        adapter = new ProfilePostsAdapter(getContext(), mPosts);
        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvPosts.setLayoutManager(gridLayoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getContext(), "infinite scrolling", Toast.LENGTH_LONG).show();
                Log.d("Carmel", "Loading more");
                queryPosts();
            }
        };

        rvPosts.addOnScrollListener(scrollListener);

        // lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // set up refresh listener, triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                mPosts.clear();
                queryPosts();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);

        // only want 20 posts back
        postQuery.setLimit(20);
        // **difference from queryPosts in PostsFragment** only want posts by current user
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // posts will be returned in order from most recent to oldest
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        // if not refreshing or loading initially, returns posts older than the last post currently loaded
        if(mPosts.size() > 0) {
            postQuery.whereLessThan(Post.KEY_CREATED_AT, mPosts.get(mPosts.size() - 1).getCreatedAt());
        }

        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e("querying posts", "error with query");
                    e.printStackTrace();
                    return;
                }

                mPosts.addAll(posts);
                adapter.notifyDataSetChanged();

                // Logging for debugging purposes
                for (int i = 0; i < posts.size(); i++) {
                    Log.d("querying posts", "Post: " + posts.get(i).getDescription() + " username: " + posts.get(i).getUser().getUsername());
                }

                swipeContainer.setRefreshing(false);
            }
        });
    }

}
