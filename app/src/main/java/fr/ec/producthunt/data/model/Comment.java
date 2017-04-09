package fr.ec.producthunt.data.model;

/**
 * Created by gterral on 14/03/2017.
 */

import android.content.ContentValues;
import fr.ec.producthunt.data.database.DataBaseContract;

public class Comment {

    private int id;
    private String content;
    private String createdAt;
    private String postId;
    private String userName;
    private String userUsername;
    private String userImageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CommentTable.ID_COLUMN,id);
        contentValues.put(DataBaseContract.CommentTable.CONTENT_COLUMN,content);
        contentValues.put(DataBaseContract.CommentTable.CREATED_AT_COLUMN,createdAt);
        contentValues.put(DataBaseContract.CommentTable.POST_ID_COLUMN,postId);
        contentValues.put(DataBaseContract.CommentTable.USER_NAME_COLUMN,userName);
        contentValues.put(DataBaseContract.CommentTable.USER_USERNAME_COLUMN,userUsername);
        contentValues.put(DataBaseContract.CommentTable.USER_IMAGE_URL_COLUMN,userImageUrl);
        return contentValues;
    }

}
