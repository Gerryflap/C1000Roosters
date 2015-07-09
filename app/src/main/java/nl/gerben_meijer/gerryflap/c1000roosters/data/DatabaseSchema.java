package nl.gerben_meijer.gerryflap.c1000roosters.data;

import android.provider.BaseColumns;

/**
 * Created by Gerryflap on 2015-06-03.
 */
public final class DatabaseSchema{
    public DatabaseSchema(){}

    public static abstract class DatabaseWerkdag implements BaseColumns{
        public static final String TABLE_NAME = "werkdagen";
        public static final String COLUMN_NAME_DAG = "dag";
        public static final String COLUMN_NAME_DATUM = "datum";
        public static final String COLUMN_NAME_BEGIN = "begin";
        public static final String COLUMN_NAME_EIND = "eind";
        public static final String COLUMN_NAME_PAUZE= "pauze";
        public static final String COLUMN_NAME_TOTAAL= "totaal";
    }
}
