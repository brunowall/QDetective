package br.ufc.quixada.qdetective.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 07/12/17.
 */

public class DenunciaDao {
    private SQLiteDatabase database;
    PersistenceHelper persistenceHelper;
    public DenunciaDao(Context context){
        persistenceHelper = new PersistenceHelper(context);
    }

    public void addDenuncia(Denuncia denuncia){
        database = persistenceHelper.getWritableDatabase();
        database.insert("denuncia",null,getValues(denuncia));
    }



    private ContentValues getValues(Denuncia denuncia){
        ContentValues contentValues = new ContentValues();
        contentValues.put("descricao",denuncia.getDescricao());
        contentValues.put("categoria",denuncia.getCategoria());
        contentValues.put("latitude",denuncia.getLatitude());
        contentValues.put("longitude", denuncia.getLongitude());
        contentValues.put("uri",denuncia.getUriMidia());
        contentValues.put("usuario",denuncia.getUsuario());
        contentValues.put("data",denuncia.getData().getTime());

        return contentValues;
    }

}
