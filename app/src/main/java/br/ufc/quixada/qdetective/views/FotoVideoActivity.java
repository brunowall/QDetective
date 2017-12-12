package br.ufc.quixada.qdetective.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.File;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


/**
 * Created by darkbyte on 26/11/17.
 */

public class FotoVideoActivity extends Activity {
    boolean possuiCartao;
    boolean suportaCartao;
    private final int CAPTURAR_IMAGEM = 1;
    private final int CAPTURAR_VIDEO = 2;
    private Uri uri;
    private Denuncia denuncia;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;
    private DenunciaDao dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.foto_video);
        possuiCartao = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        suportaCartao = Environment.isExternalStorageRemovable();
        dao = new DenunciaDao(this);
        this.denuncia =(Denuncia)getIntent().getExtras().get("denuncia");
        getLocationManager();
    }

    public void tirarFotoClicked(View view){
        if(android.os.Build.VERSION.SDK_INT >= 23){
            getPermissoes();
        }else {
            capturarFoto();
        }
    }
    public void capturarFoto(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.getPathSalvamento(".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent, CAPTURAR_IMAGEM);
    }


    protected void getPermissoes(){
        String CAMERA = Manifest.permission.CAMERA;
        String READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        String WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean camera = ActivityCompat.checkSelfPermission(this,CAMERA) == PERMISSION_GRANTED;
        boolean read = ActivityCompat.checkSelfPermission(this,READ) == PERMISSION_GRANTED;
        boolean write = ActivityCompat.checkSelfPermission(this,WRITE)==PERMISSION_GRANTED;
        if(camera && read && write){
             capturarFoto();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{CAMERA,READ,WRITE},1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){

            this.denuncia.setUriMidia(uri.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == 1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults [1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED){
                capturarFoto();
            }else{
                Toast.makeText(this,"O aplicativo não possui as permissoes necessárias",Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void getLocationManager(){
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    2);

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                FotoVideoActivity.this.latitude = location.getLatitude();
                FotoVideoActivity.this.longitude = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }
   public void gravarVideoClick(View view){
       Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
       this.getPathSalvamento(".mp4");
       intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
       intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
       startActivityForResult(intent, 2);
    }
    private void getPathSalvamento(String formato){
        String nomearq = System.currentTimeMillis()+formato;
        File diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(possuiCartao && suportaCartao){
            diretorio =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        File arquivo = new File(diretorio,nomearq);
        uri = Uri.fromFile(arquivo);

    }


    public void continueClick(View view){
        this.denuncia.setLongitude(this.longitude);
        this.denuncia.setLatitude(this.latitude);
        this.dao.addDenuncia(denuncia);
        Toast.makeText(this, "Denuncia adicionada com sucesso", Toast.LENGTH_SHORT).show();
        finish();
    }
}