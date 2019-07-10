package com.example.parstagram.fragments;

import android.util.Log;

import com.example.parstagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostsFragment {

    @Override
    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);

        // only want 20 posts back
        postQuery.setLimit(20);
        // **difference from queryPosts in PostsFragment** only want posts by current user
        postQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // posts will be returned in order from most recent to oldest
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);

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
