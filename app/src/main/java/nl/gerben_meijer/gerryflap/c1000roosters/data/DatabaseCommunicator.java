package nl.gerben_meijer.gerryflap.c1000roosters.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;

/**
 * Created by Gerryflap on 2015-06-03.
 */
public class DatabaseCommunicator {

    private static String[] projection = new String[]{
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DAG,
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DATUM,
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_BEGIN,
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_EIND,
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_PAUZE,
            DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_TOTAAL
    };
    private static DatabaseCommunicator instance;

    public WerkdagDatabaseHelper helper;


    public static DatabaseCommunicator getInstance(){
        return instance;
    }

    public DatabaseCommunicator(Context context){
        instance = this;
        helper = new WerkdagDatabaseHelper(context);
    }

    public void insertWerkdag(Werkdag werkdag){
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DAG, werkdag.getDag());
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_DATUM, werkdag.getDatum());
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_BEGIN, werkdag.getStart());
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_EIND, werkdag.getEind());
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_PAUZE, werkdag.getPauze());
        values.put(DatabaseSchema.DatabaseWerkdag.COLUMN_NAME_TOTAAL, werkdag.getTotaal());

        helper.getWritableDatabase().insert(DatabaseSchema.DatabaseWerkdag.TABLE_NAME,
                 null,
                values);
        helper.close();
    }

    public List<Werkdag> getWerkdagList(){
        List<Werkdag> out = new ArrayList<>();
        try {
            Cursor c = helper.getReadableDatabase().query(
                    DatabaseSchema.DatabaseWerkdag.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            while (c.moveToNext()){
                try {
                    out.add(new Werkdag(
                            c.getString(0),
                            c.getString(1),
                            c.getString(2),
                            c.getString(3),
                            c.getString(4),
                            c.getString(5)
                    ));
                } catch (IllegalStateException e){
                    this.clear();
                    break;
                }
            }
            c.close();
        } catch (SQLiteException e){
            this.clear();
        }


        helper.close();
        System.out.println(out);
        return out;
    }

    public void clear() {
        helper.getWritableDatabase().delete(DatabaseSchema.DatabaseWerkdag.TABLE_NAME, null, null);
    }
}
