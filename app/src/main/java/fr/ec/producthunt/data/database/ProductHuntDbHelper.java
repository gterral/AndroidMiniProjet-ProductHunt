package fr.ec.producthunt.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Mohammed Boukadir  @:mohammed.boukadir@gmail.com
 */
public class ProductHuntDbHelper extends SQLiteOpenHelper {

  public ProductHuntDbHelper(Context context) {
    super(context, DataBaseContract.DATABASE_NAME, null, DataBaseContract.DATABASE_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(DataBaseContract.PostTable.SQL_CREATE_POST_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
    db.execSQL(DataBaseContract.PostTable.SQL_DROP_POST_TABLE);
    onCreate(db);
  }
}
