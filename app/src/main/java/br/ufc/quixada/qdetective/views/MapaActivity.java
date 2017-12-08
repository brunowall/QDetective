package br.ufc.quixada.qdetective.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 07/12/17.
 */

public class MapaActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private String urlBase = "http://maps.googleapis.com/maps/api/staticmap" +
            "?size=400x400&sensor=true&markers=color:red|%s,%s";
    private WebView mWebView;
    private DenunciaDao denunciaDao;
    private TextView latitudeView;
    private double latitude;
    private double longitude;
    private TextView longitudeView;

    public MapaActivity() {


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        this.mWebView = (WebView) findViewById(R.id.webView);
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(false);
        denunciaDao = new DenunciaDao(this);
        this.latitudeView = (TextView)findViewById(R.id.latitude);
        this.longitudeView = (TextView)findViewById(R.id.longitude);
        executaLocalization();
    }


    public void executaLocalization(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    1);
            return;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                MapaActivity.this.latitude = location.getLatitude();
                MapaActivity.this.longitude = location.getLongitude();

                MapaActivity.this.latitudeView.setText(location.getLatitude()+"");
                MapaActivity.this.longitudeView.setText(location.getLongitude()+"");

                Log.e( "onActivityResult: ",""+location.getLatitude());
                String url = String.format(urlBase, location.getLatitude()+"", location.getLongitude()+"");
                mWebView.loadUrl(url);
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
   public  void  salvarClicked(View view){
       this.getIntent();
       String json = this.getIntent().getExtras().getString("denuncia");
       Denuncia denuncia = new Denuncia();
       denuncia.setLatitude(latitude);
       denuncia.setLongitude(longitude);
       denunciaDao.addDenuncia(denuncia);

   }
}
