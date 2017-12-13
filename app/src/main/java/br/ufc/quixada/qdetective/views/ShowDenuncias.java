package br.ufc.quixada.qdetective.views;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;
import br.ufc.quixada.qdetective.services.DenunciaServiceWeb;

/**
 * Created by darkbyte on 10/12/17.
 */

public class ShowDenuncias extends AppCompatActivity implements OptionsDialog.OptionsDialogListener, RemoveConfirmDialog.RemoveConfirmListener{
    private ListView listView;
    private DenunciaDao dao;
    private int itemclicked;
    private AdapterPersonalizado adp;
    private Denuncia auxiliar;
    private boolean retorno;
    String url = "http://35.193.98.124/QDetective/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_denuncias);
        listView = (ListView)findViewById(R.id.lista);
        dao = new DenunciaDao(this);
        if(this.getIntent().getExtras().getBoolean("local")){
            adp = new AdapterPersonalizado(this,dao.getAllDenuncias());
        }else{

        }
        listView.setAdapter(adp);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DialogFragment dialog =  new OptionsDialog();
                ShowDenuncias.this.itemclicked = i;
                dialog.show(ShowDenuncias.this.getFragmentManager(),"Options");
            }
        });
    }


    @Override
    public void onClickEditar() {
        Intent intent = new Intent(this,MainActivity.class);
        Denuncia denuncia = (Denuncia)listView.getItemAtPosition(itemclicked);
        Denuncia denuncia1 = dao.getDenunciaByID(denuncia.getId());
        intent.putExtra("denuncia",denuncia);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode == RESULT_OK){
            this.finish();
            startActivity(this.getIntent());
        }
    }

    @Override
    public void onClickRemover() {
        RemoveConfirmDialog rcd = new RemoveConfirmDialog();
        rcd.show(this.getFragmentManager(),"Confirmação");

    }

    @Override
    public void onClickDetalhes() {
        Denuncia denuncia = (Denuncia) listView.getItemAtPosition(itemclicked);
        Intent intent = new Intent(this,DetalheActivity.class);
        intent.putExtra("denuncia",new Gson().toJson(denuncia,Denuncia.class));
        startActivity(intent);
    }

    @Override
    public void onClickCompartilhar() {
        UploadJson uploadJson = new UploadJson();
        uploadJson.execute((Denuncia)listView.getItemAtPosition(itemclicked));
        this.auxiliar = (Denuncia) listView.getItemAtPosition(itemclicked);
    }

    @Override
    public void onClickSim() {
        Denuncia denuncia = (Denuncia) listView.getItemAtPosition(itemclicked);
        this.dao.removeDenuncia(denuncia.getId());
        this.finish();
        startActivity(getIntent());
    }

    @Override
    public void onClickNao() {
        return;
    }

    private class UploadJson extends AsyncTask<Denuncia, Void,DenunciaServiceWeb>

    {   @Override
    protected void onPreExecute(){

    }

        @Override
        protected DenunciaServiceWeb doInBackground(Denuncia... denuncias) {
            DenunciaServiceWeb denunciaServiceWeb = new DenunciaServiceWeb();
            String envio = ShowDenuncias.this.url+"rest/denuncias";
            Denuncia denuncia = denuncias[0];
            try {
                if(denunciaServiceWeb.sendDenuncia(envio,denuncia)){
                    String urlData = ShowDenuncias.this.url+"rest/arquivos/postFotoBase64";
                  if(denunciaServiceWeb.sendMidiaToserver(urlData,getDiretorioDeSalvamento(denuncia.getUriMidia()))){
                      Log.e("doInBackground: ",denunciaServiceWeb.getRespostaServidor());
                      ShowDenuncias.this.dao.removeDenuncia(ShowDenuncias.this.auxiliar.getId());
                      ShowDenuncias.this.finish();
                      ShowDenuncias.this.startActivity(ShowDenuncias.this.getIntent());

                  }else{
                      Toast.makeText(ShowDenuncias.this, "Erro ao enviar os dados para o servidor", Toast.LENGTH_SHORT).show();
                  }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
          return denunciaServiceWeb;
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
