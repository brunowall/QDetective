package br.ufc.quixada.qdetective.views;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 10/12/17.
 */

public class ShowDenuncias extends AppCompatActivity implements OptionsDialog.OptionsDialogListener, RemoveConfirmDialog.RemoveConfirmListener{
    private ListView listView;
    private DenunciaDao dao;
    private int itemclicked;
    private AdapterPersonalizado adp;
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
                dialog.show(ShowDenuncias.this.getFragmentManager(),"Options");
            }
        });
    }


    @Override
    public void onClickEditar() {

    }

    @Override
    public void onClickRemover() {
        RemoveConfirmDialog rcd = new RemoveConfirmDialog();
        rcd.show(this.getFragmentManager(),"Confirmação");
        this.listView.invalidateViews();
        this.listView.refreshDrawableState();
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

    }

    @Override
    public void onClickSim() {
        Denuncia denuncia = (Denuncia) listView.getItemAtPosition(itemclicked);
        this.dao.removeDenuncia(denuncia.getId());
        this.adp.notifyDataSetChanged();
    }

    @Override
    public void onClickNao() {
        return;
    }
}
