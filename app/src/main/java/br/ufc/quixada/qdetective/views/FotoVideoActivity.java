package br.ufc.quixada.qdetective.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import br.ufc.quixada.qdetective.R;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * Created by darkbyte on 26/11/17.
 */

public class FotoVideoActivity extends Activity {
    boolean possuiCartao;
    boolean suportaCartao;
    private final int CAPTURAR_IMAGEM = 1;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.foto_video);
        possuiCartao = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        suportaCartao = Environment.isExternalStorageRemovable();
    }

    public void tirarFotoClicked(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,this.getPathSalvamento());
        startActivityForResult(intent,CAPTURAR_IMAGEM);

    }


    protected void getPermissoes(){
        String CAMERA = Manifest.permission.CAMERA;
        String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean camera = ActivityCompat.checkSelfPermission(this,CAMERA) == PERMISSION_GRANTED;
        boolean read = ActivityCompat.checkSelfPermission(this,READ) == PERMISSION_GRANTED;
        boolean write = ActivityCompat.checkSelfPermission(this,WRITE)==PERMISSION_GRANTED;
        if(camera && read && write){
             tirarFotoClicked(null);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{CAMERA,READ,WRITE},1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults[0]==PERMISSION_GRANTED && grantResults [1]==PERMISSION_GRANTED && grantResults[2]==PERMISSION_GRANTED){
                tirarFotoClicked(null);
            }else{
                Toast.makeText(this,"O aplicativo não possui as permissoes necessárias",Toast.LENGTH_SHORT);
            }
        }
    }

    private Uri getPathSalvamento(){
        String nomearq = System.currentTimeMillis()+".jpg";
        File diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(possuiCartao && suportaCartao){
            diretorio =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        File arquivo = new File(diretorio,nomearq);
        uri = Uri.fromFile(arquivo);
        return uri;
    }
}