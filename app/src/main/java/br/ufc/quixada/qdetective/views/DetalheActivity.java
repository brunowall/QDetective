package br.ufc.quixada.qdetective.views;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;

import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 11/12/17.
 */

public class DetalheActivity extends AppCompatActivity {
    private TextView categoria;
    private TextView descricao;
    private TextView data;
    private TextView usuario;
    private WebView webView;
    private Denuncia denuncia;
    RelativeLayout relativeLayout;
    private String urlBase = "http://maps.googleapis.com/maps/api/staticmap" +
            "?size=400x400&sensor=true&markers=color:red|%s,%s";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_denuncia);
        String denuncia = getIntent().getExtras().getString("denuncia");
        this.denuncia = new Gson().fromJson(denuncia,Denuncia.class);

        this.categoria = (TextView) findViewById(R.id.categoria);
        this.descricao = (TextView) findViewById(R.id.descricao);
        this.data= (TextView) findViewById(R.id.data);
        this.usuario = (TextView) findViewById(R.id.usuario);
        this.webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        relativeLayout =(RelativeLayout) findViewById(R.id.relativeLayout);
        String url = String.format(urlBase,""+this.denuncia.getLatitude(),""+this.denuncia.getLongitude());
        webView.loadUrl(url);

        this.categoria.setText(this.denuncia.getCategoria());
        this.descricao.setText(this.denuncia.getDescricao());
        this.usuario.setText(this.denuncia.getUsuario());
        this.data.setText(new SimpleDateFormat("dd/MM/yyyy").format(this.denuncia.getData()));
        this.mostrar_Midia();
    }

    public void mostrar_Midia(){
        if(this.denuncia.getUriMidia().endsWith(".mp4")){
            VideoView videoView = new VideoView(this);
           this.relativeLayout.addView(videoView);
            videoView.setVideoURI(Uri.parse(denuncia.getUriMidia()));
            videoView.start();
        }else{
            ImageView iv = new ImageView(this);
            this.relativeLayout.addView(iv,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            iv.setImageURI(Uri.parse(denuncia.getUriMidia()));
            return;
        }
    }
}
