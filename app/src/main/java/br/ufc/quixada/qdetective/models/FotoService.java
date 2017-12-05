package br.ufc.quixada.qdetective.models;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by darkbyte on 04/12/17.
 */

public class FotoService {
    private Context context;
    private boolean possuiCartao;
    private boolean suportaCartao;
    public FotoService(Context context){
        this.context = context;
        possuiCartao = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        suportaCartao = Environment.isExternalStorageRemovable();
    }
    public void getPermissoes(){
        String CAMERA = Manifest.permission.CAMERA;
        String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean camera = ActivityCompat.checkSelfPermission(context,CAMERA) == PERMISSION_GRANTED;
        boolean read = ActivityCompat.checkSelfPermission(context,READ) == PERMISSION_GRANTED;
        boolean write = ActivityCompat.checkSelfPermission(context,WRITE)==PERMISSION_GRANTED;
        
    }
}
