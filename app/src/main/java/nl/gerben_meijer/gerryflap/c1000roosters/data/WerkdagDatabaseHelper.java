package nl.gerben_meijer.gerryflap.c1000roosters.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gerryflap on 2015-06-03.
 */
public class WerkdagDatabaseHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "C1000mwApp.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseSchema.DatabaseWerkdag.TABLE_NAME + " (" +
                    DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DAG + TEXT_TYPE + COMMA_SEP +
                    DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DATUM + TEXT_TYPE + COMMA_SEP +
                    DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_BEGIN + TEXT_TYPE + COMMA_SEP +
                    DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_EIND + TEXT_TYPE + COMMA_SEP +
                    DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_PAUZE + TEXT_TYPE + COMMA_SEP +
                    "PRIMARY KEY("+
                        DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DAG + COMMA_SEP +
                        DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DATUM + ")" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseSchema.DatabaseWerkdag.TABLE_NAME;

    public WerkdagDatabaseHelper(Context context ){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);


    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
