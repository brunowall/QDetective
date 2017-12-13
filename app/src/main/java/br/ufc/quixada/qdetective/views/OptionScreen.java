package br.ufc.quixada.qdetective.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;
import br.ufc.quixada.qdetective.services.DenunciaServiceWeb;

/**
 * Created by darkbyte on 10/12/17.
 */

public class OptionScreen extends AppCompatActivity {
    String url = "http://35.193.98.124/QDetective/";
    private List<Denuncia> denuncias;
    private DenunciaDao denunciaDao;
    private ProgressDialog load;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);
        denunciaDao= new DenunciaDao(this);
    }
    public void criarDenuncia(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
        startActivity(intent);

    }
    public void verDenuncias(View view){
            DownloadJson downloadJson = new DownloadJson();
            downloadJson.execute();

    }
    public void verDenunciasLocais(View view){
        Intent intent = new Intent(this,ShowDenuncias.class);
        intent.putExtra("local",true);
        startActivity(intent);
    }

    private class DownloadJson extends AsyncTask<Long, Void, DenunciaServiceWeb> {

        @Override
        protected void onPreExecute() {
           /// load = ProgressDialog.show(OptionScreen.this, "Por favor Aguarde ...", "Recuperando Informações do Servidor...");
        }

        @Override
        protected DenunciaServiceWeb doInBackground(Long... ids) {
           DenunciaServiceWeb webService = new DenunciaServiceWeb();
            String id = (ids != null && ids.length == 1) ? ids[0].toString() : "";
            List<Denuncia> denuncias = webService.getListaDenunciaJson(url, "rest/denuncias", id);

            for (Denuncia denuncia1: denuncias) {
                String path = getDiretorioDeSalvamento(denuncia1.getUriMidia()).getPath();
                webService.downloadMidiaBase64(url + "arquivos", path, denuncia1.getId());
                denuncia1.setUriMidia(path);
            }
            return webService;
        }

        @Override
        protected void onPostExecute(DenunciaServiceWeb webService) {
            for (Denuncia denuncia : webService.getDenuncias()) {
                Denuncia d = OptionScreen.this.denunciaDao.getDenunciaByID(denuncia.getId());
                OptionScreen.this.denunciaDao.addDenuncia(d);
            }
            load.dismiss();
            Toast.makeText(getApplicationContext(), webService.getRespostaServidor(), Toast.LENGTH_LONG).show();

        }
    }
    private File getDiretorioDeSalvamento(String nomeArquivo) {
        if (nomeArquivo.contains("/")) {
            int beginIndex = nomeArquivo.lastIndexOf("/") + 1;
            nomeArquivo = nomeArquivo.substring(beginIndex);
        }
        File diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File pathDaImagem = new File(diretorio, nomeArquivo);
        return pathDaImagem;
    }

}
