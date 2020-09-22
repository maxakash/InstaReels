package com.weaponoid.instareels.persistance;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Document {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int documentId;

    @NonNull
    private String caption;

    @NonNull
    private String hashtag;

    @NonNull
    private String shareUrl;

    @NonNull
    private String videoUri;

    private String postUrl;

    @NonNull
    private String handle;

    @NonNull
    private String dpUrl;

    @NonNull
    private String isVideo;

    private ArrayList<String> imagesUri;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    @NonNull
    public String getCaption() {
        return caption;
    }

    public void setCaption(@NonNull String caption) {
        this.caption = caption;
    }

    @NonNull
    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(@NonNull String hashtag) {
        this.hashtag = hashtag;
    }

    @NonNull
    public ArrayList<String> getImagesUri() {
        return imagesUri;
    }

    public void setImagesUri(@NonNull ArrayList<String> imagesName) {
        this.imagesUri = imagesName;
    }

    @NonNull
    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(@NonNull String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @NonNull
    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(@NonNull String dpUrl) {
        this.dpUrl = dpUrl;
    }

    @NonNull
    public String getVideoUri() {
        return videoUri;
    }

    public void setVideoUri(@NonNull String videoName) {
        this.videoUri = videoName;
    }


    @NonNull
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    @NonNull
    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @NonNull
    public String getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(String isVideo) {
        this.isVideo = isVideo;
    }

}
