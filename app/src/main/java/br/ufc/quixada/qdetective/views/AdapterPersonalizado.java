package br.ufc.quixada.qdetective.views;

import android.app.Activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.List;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

/**
 * Created by darkbyte on 10/12/17.
 */

public class AdapterPersonalizado extends BaseAdapter{
    private List<Denuncia> denuncias;
    private Activity activity;
    AdapterPersonalizado(Activity activity, List<Denuncia> denuncias){
        this.activity = activity;
        this.denuncias = denuncias;
    }

    @Override
    public int getCount() {
        return denuncias.size();
    }

    @Override
    public Object getItem(int i) {
        return denuncias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return denuncias.get(i).getId();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v  = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.item_list,viewGroup,false);
        TextView categoria = v.findViewById(R.id.categoria);
        TextView descricao = v.findViewById(R.id.descricao);
        TextView data = v.findViewById(R.id.data);
        TextView usuario = v.findViewById(R.id.usuario);

        categoria.setText(denuncias.get(i).getCategoria());
        descricao.setText(denuncias.get(i).getDescricao());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        data.setText(sdf.format(denuncias.get(i).getData()));
        usuario.setText(denuncias.get(i).getUsuario());
        descricao.setText(denuncias.get(i).getDescricao());
        return v;
    }
}
