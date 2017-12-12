package br.ufc.quixada.qdetective.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.ufc.quixada.qdetective.Persistence.DenunciaDao;
import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

public class MainActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener {

    private EditText descricao;
    private Spinner categorySpinner;
    private Button dataButton;
    private Date data;
    Denuncia denuncia;
    private DenunciaDao dao;
    private boolean update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Date();

        dao = new DenunciaDao(this);
        setContentView(R.layout.activity_main);
        this.descricao = (EditText)findViewById(R.id.description);
        this.dataButton =(Button) findViewById(R.id.dataButton);
        this.categorySpinner =(Spinner) findViewById(R.id.categorySpinner);
        Calendar calendar = Calendar.getInstance();
        int dia =  calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        this.dataButton.setText(makeData(dia,mes,ano));
        String []values  = new String[]{"Vias públicas de acesso","Equipamentos comunitários","Limpeza urbana e saneamento"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,values);
        categorySpinner.setAdapter(adapter);
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("denuncia")!=null){
            this.denuncia = (Denuncia)  getIntent().getExtras().get("denuncia");
            buildScreenByDenuncia(this.denuncia);
            update = true;
        }else{
            this.denuncia = new Denuncia();
            update = false;
        }
    }

    private String makeData(int dia, int mes, int ano){
        return dia+"/"+(mes+1)+"/"+ano;
    }

    public void continueClick(View view) throws JSONException {
        Intent intent = new Intent(this,FotoVideoActivity.class);
        denuncia.setData(data);
        denuncia.setDescricao(descricao.getText().toString());
        denuncia.setCategoria(categorySpinner.getSelectedItem().toString());
        if(!this.update){
            denuncia.setUsuario(getIntent().getExtras().getString("usuario"));
        }
        intent.putExtra("denuncia",this.denuncia);
        startActivity(intent);
        this.finish();
    }

    public void dataButtonClicked(View view){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(this.getFragmentManager(),"Data Denuncia");

    }

    @Override
    public void datePickerChosed(int dia, int mes, int ano) {
        Calendar calendar =  Calendar.getInstance();
        calendar.set(Calendar.MONTH,mes);
        calendar.set(Calendar.YEAR,ano);
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        this.dataButton.setText(dia+"/"+(mes+1)+"/"+ano);
        data.setTime(calendar.getTimeInMillis());
    }

    private void buildScreenByDenuncia(Denuncia denuncia){
        this.descricao.setText(denuncia.getDescricao());
        this.data = denuncia.getData();
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyyy");
        this.dataButton.setText(sfd.format(denuncia.getData()));
        String categoria = denuncia.getCategoria();
        for(int i = 0;i<categorySpinner.getCount(); i++){
            if(categorySpinner.getItemAtPosition(i).toString().equals(categoria)){
                this.categorySpinner.setSelection(i);
                break;
            }
        }


    }

}
