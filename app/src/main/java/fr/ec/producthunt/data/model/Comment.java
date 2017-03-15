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

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.CommentTable.ID_COLUMN,id);
        contentValues.put(DataBaseContract.CommentTable.CONTENT_COLUMN,content);
        contentValues.put(DataBaseContract.CommentTable.CREATED_AT_COLUMN,createdAt);
        contentValues.put(DataBaseContract.CommentTable.POST_ID_COLUMN,postId);
        return contentValues;
    }


}
