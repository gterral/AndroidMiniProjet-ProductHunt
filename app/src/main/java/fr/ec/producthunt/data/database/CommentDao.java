package fr.ec.producthunt.data.database;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import fr.ec.producthunt.data.model.Comment;

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

    public List<Comment> retrieveComments() {

        Cursor cursor = productHuntDbHelper.getReadableDatabase()
                .query(DataBaseContract.CommentTable.TABLE_NAME,
                        DataBaseContract.CommentTable.PROJECTION_COMMENTS,
                        null, null, null,null,null);

        List<Comment> comments = new ArrayList<>(cursor.getCount());

        if (cursor.moveToFirst()) {
            do {

                Comment comment = new Comment();

                comment.setId(cursor.getInt(0));
                comment.setContent(cursor.getString(1));
                comment.setCreatedAt(cursor.getString(2));

                comments.add(comment);


            } while (cursor.moveToNext());
        }

        return comments;
    }
}

