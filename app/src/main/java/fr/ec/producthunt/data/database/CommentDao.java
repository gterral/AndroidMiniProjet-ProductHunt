package fr.ec.producthunt.data.database;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import fr.ec.producthunt.data.model.Comment;

import static java.lang.Integer.parseInt;

/**
 * Created by gterral on 14/03/2017.
 */

public class CommentDao {
    private final ProductHuntDbHelper productHuntDbHelper;

    public CommentDao(ProductHuntDbHelper productHuntDbHelper) {
        this.productHuntDbHelper = productHuntDbHelper;
    }

    public long save(Comment comment) {
        return productHuntDbHelper.getWritableDatabase()
                .insert(DataBaseContract.CommentTable.TABLE_NAME, null, comment.toContentValues());
    }

    public List<Comment> retrieveComments(String postId) {

        String[] postIdArray = {postId};
        // .query(String table, String[] columns, String selection, String[] selectionArgs,
        // String groupBy, String having, String orderBy)
        //Query the given table, returning a Cursor over the result set.

        Cursor cursor = productHuntDbHelper.getReadableDatabase()
                .query(DataBaseContract.CommentTable.TABLE_NAME,
                        DataBaseContract.CommentTable.PROJECTION_COMMENTS,
                        DataBaseContract.CommentTable.WHERE_POSTID, postIdArray, null,null,DataBaseContract.CommentTable.ORDER_BY_DATE);

        List<Comment> comments = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                Comment comment = new Comment();

                comment.setId(cursor.getInt(0));
                comment.setContent(cursor.getString(1));
                comment.setCreatedAt(cursor.getString(2));
                comment.setPostId(postId);
                comments.add(comment);


            } while (cursor.moveToNext());
        }

        System.out.println(comments);
        return comments;
    }
}

