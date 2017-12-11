package br.ufc.quixada.qdetective.Persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by darkbyte on 07/12/17.
 */

public class PersistenceHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "qdetective";
    private static final int version = 1;

    public PersistenceHelper(Context context) {
        super(context,DB_NAME, null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table denuncia(id INTEGER PRIMARY KEY AUTOINCREMENT,descricao Text, data DATE, latitude REAL,longitude REAL,uri TEXT,usuario Text,categoria TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
