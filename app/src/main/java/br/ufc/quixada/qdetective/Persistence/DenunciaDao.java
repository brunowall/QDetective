package br.ufc.quixada.qdetective.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        Cursor cursor = database.rawQuery("SELECT * FROM denuncia WHERE id= ?", new String[] { String.valueOf(denuncia.getId())});
        if(cursor.getCount()==0){
            database.insert("denuncia",null,getValues(denuncia));

        }
        else{
            database.update("denuncia",this.getValues(denuncia),"id=?",new String[] { String.valueOf(denuncia.getId())});
        }

        database.close();
    }


    public List<Denuncia> getAllDenuncias(){
        database = persistenceHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from denuncia",null);
        List<Denuncia> result = new LinkedList<Denuncia>();
        while (cursor.moveToNext()){
            result.add(buildDenuncia(cursor));
        }
        cursor.close();
        database.close();
        return result;
    }

    private Denuncia buildDenuncia(Cursor cursor){
        Denuncia denuncia = new Denuncia();
        denuncia.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        denuncia.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
        denuncia.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
        Date date = new Date();
        date.setTime(cursor.getLong(cursor.getColumnIndex("data")));
        denuncia.setData(date);
        denuncia.setUsuario(cursor.getString(cursor.getColumnIndex("usuario")));
        denuncia.setId(cursor.getLong(cursor.getColumnIndex("id")));
        denuncia.setCategoria(cursor.getString(cursor.getColumnIndex("categoria")));
        denuncia.setUriMidia(cursor.getString(cursor.getColumnIndex("uri")));

        return denuncia;
    }

    private ContentValues getValues(Denuncia denuncia){
        ContentValues contentValues = new ContentValues();
        contentValues.put("descricao",denuncia.getDescricao());
        contentValues.put("latitude",denuncia.getLatitude());
        contentValues.put("longitude", denuncia.getLongitude());
        contentValues.put("uri",denuncia.getUriMidia());
        contentValues.put("categoria",denuncia.getCategoria());
        contentValues.put("usuario",denuncia.getUsuario());
        contentValues.put("data",denuncia.getData().getTime());

        return contentValues;
    }

    public void removeDenuncia(long id){
        this.database = this.persistenceHelper.getWritableDatabase();
        database.delete("denuncia","id=?",new String[]{String.valueOf(id)});
        this.database.close();
    }

    public Denuncia getDenunciaByID(long id){
        this.database = this.persistenceHelper.getWritableDatabase();
        Cursor cursor = this.database.rawQuery("select * from denuncia where id=?",new String[]{String.valueOf(id)});
        Denuncia denuncia = null;
        while(cursor.moveToNext()){
            denuncia = buildDenuncia(cursor);
        }
        return denuncia;
    }
}
