package fr.ec.producthunt.data.database;

import android.provider.BaseColumns;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public final class DataBaseContract {

  public static final String DATABASE_NAME = "database";
  public static final int DATABASE_VERSION = 1;

  public static final String TEXT_TYPE = " TEXT";
  public static final String COMM_SPA = ",";
  public static final String INTEGER_TYPE = " INTEGER";

  /** Description de la table des Posts **/
  public static final class PostTable implements BaseColumns {

    public static final String TABLE_NAME = "post";

    public static final String ID_COLUMN = "id";
    public static final String TITLE_COLUMN = "title";
    public static final String SUBTITLE_COLUMN ="subtitle";
    public static final String COMMENT_COUNT_COLUMN ="commentCount";
    public static final String IMAGE_URL_COLUMN ="imageurl";
    public static final String POST_URL_COLUMN  ="postUrl";
    public static final String POST_CREATED_AT_COLUMN ="created_at";

    public static final String ORDER_BY_DATE = PostTable.POST_CREATED_AT_COLUMN + " DESC";

    public static final String SQL_CREATE_POST_TABLE =
        "CREATE TABLE " +PostTable.TABLE_NAME+" ("+
            PostTable.ID_COLUMN + INTEGER_TYPE+" PRIMARY KEY"+COMM_SPA+
            PostTable.TITLE_COLUMN + TEXT_TYPE +COMM_SPA+
            PostTable.SUBTITLE_COLUMN + TEXT_TYPE +COMM_SPA+
            PostTable.COMMENT_COUNT_COLUMN + INTEGER_TYPE+COMM_SPA+
            PostTable.IMAGE_URL_COLUMN + TEXT_TYPE+COMM_SPA+
            PostTable.POST_URL_COLUMN + TEXT_TYPE+COMM_SPA+
            PostTable.POST_CREATED_AT_COLUMN + TEXT_TYPE+
            ")";

    public static final String SQL_DROP_POST_TABLE =  "DROP TABLE IF EXISTS "+TABLE_NAME;

    public static String[] PROJECTIONS = new String[] {
        ID_COLUMN,
        TITLE_COLUMN,
        SUBTITLE_COLUMN,
        COMMENT_COUNT_COLUMN,
        IMAGE_URL_COLUMN,
        POST_URL_COLUMN,
        POST_CREATED_AT_COLUMN
    };
  }

  public static final class CommentTable implements BaseColumns {
    public static final String TABLE_NAME = "comment";

    public static final String ID_COLUMN = "id";
    public static final String CONTENT_COLUMN = "content";
    public static final String CREATED_AT_COLUMN ="created_at";
    public static final String POST_ID_COLUMN ="post_id";

    public static final String WHERE_POSTID = CommentTable.POST_ID_COLUMN + " = ?";
    public static final String ORDER_BY_DATE = CommentTable.CREATED_AT_COLUMN + " DESC";

    public static final String SQL_CREATE_COMMENT_TABLE =
            "CREATE TABLE " + CommentTable.TABLE_NAME+" ("+
                    CommentTable.ID_COLUMN + INTEGER_TYPE+" PRIMARY KEY"+COMM_SPA+
                    CommentTable.CONTENT_COLUMN + TEXT_TYPE +COMM_SPA+
                    CommentTable.CREATED_AT_COLUMN + TEXT_TYPE + COMM_SPA+
                    CommentTable.POST_ID_COLUMN + TEXT_TYPE+
                    ")";

    public static final String SQL_DROP_COMMENT_TABLE =  "DROP TABLE IF EXISTS "+TABLE_NAME;

    public static String[] PROJECTION_COMMENTS = new String[] {
            ID_COLUMN,
            CONTENT_COLUMN,
            CREATED_AT_COLUMN,
            POST_ID_COLUMN
    };
  }

}
