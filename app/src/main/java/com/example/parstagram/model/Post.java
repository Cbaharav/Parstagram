package com.example.parstagram.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@ParseClassName("Post")
public class Post extends ParseObject implements Serializable {
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_LIKES = "likes";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    // parsefile is a class in SDK to easily access images
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseFile getProfilePic() {
        return getUser().getParseFile(KEY_PROFILE_PIC);
    }

    public void setProfilePic(ParseFile image) {
        getUser().put(KEY_CREATED_AT, image);
    }

    // initializes likes to an empty arrayList
    public void setLikes() {
        ArrayList<ParseUser> likes = new ArrayList<>();
        put(KEY_LIKES, likes);
    }

    // returns current arrayList of likes for the post
    public ArrayList<ParseUser> getLikes() {
        ArrayList<ParseUser> likes = (ArrayList<ParseUser>) get(KEY_LIKES);
        if(likes == null) {
            setLikes();
            return (ArrayList<ParseUser>) get(KEY_LIKES);
        } else return likes;
    }

    // adds the inputted user to the list of likes
    public void addLike(ParseUser user) {
        ArrayList<ParseUser> updated = getLikes();
        updated.add(user);
        this.put("likes", updated);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("likes", "added like");
            }
        });
    }

    // removes the inputted user from the list of likes
    public void unLike(ParseUser user) {
        ArrayList<ParseUser> updated = getLikes();
        for (int i = 0; i < updated.size(); i++) {
            ParseUser currUser = updated.get(i);
            // remove user from list of likes once you find the user
            if ((currUser.getUsername()).equals(user.getUsername())) {
                updated.remove(i);
            }
        }
        this.put("likes", updated);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("likes", "removed like");
            }
        });
    }

    public boolean hasLiked(ParseUser user) {
        ArrayList<ParseUser> likes = getLikes();
        // iterate through the list of users that have liked the post
        for (int i = 0; i < likes.size(); i++) {
            ParseUser currUser = likes.get(i);
            // if username of a user in the list matches inputted user, user has liked post
            if ((currUser.getUsername()).equals(user.getUsername())) return true;
        }
        return false;
    }


    public String getTime() {
        Date date = getCreatedAt();
        String format = "hh:mm, EEE MMM dd, yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        String formattedDate = sf.format(date);

        return formattedDate;
    }
}
