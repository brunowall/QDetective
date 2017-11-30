package br.ufc.quixada.qdetective.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;

import br.ufc.quixada.qdetective.R;


/**
 * Created by darkbyte on 26/11/17.
 */

public class FotoVideoActivity extends Activity {
    boolean possuiCartao;
    boolean suportaCartao;
    private final int CAPTURAR_IMAGEM = 1;
    Uri uri;
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


//    protected void getPermissoes(){
//
//    }

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
