package com.cmpe277.sjsu.ishare;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by ryan_vo on 4/27/15.
 */
@ParseClassName("IshareItem")
public class IShareItem extends ParseObject {
    public IShareItem() {
        // A default constructor is required.
    }

    public ParseFile getPhotoFile() {
        return getParseFile("photo");
    }

    public void setPhotoFile(ParseFile file) {
        put("photo", file);
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getCategory() {
        return getString("description");
    }

    public void setCategory(String category) {
        put("category", category);
    }

    public ParseUser getAuthor() {
        return getParseUser("owner");
    }

    public void setAuthor(ParseUser user) {
        put("owner", user);
    }


    //santanu new add
    public String getRequested() {
        return getString("requested");
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public void setRequested(String requested) {
        put("requested", requested);
    }
}