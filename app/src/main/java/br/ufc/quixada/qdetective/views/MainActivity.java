package br.ufc.quixada.qdetective.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.ufc.quixada.qdetective.R;
import br.ufc.quixada.qdetective.models.Denuncia;

public class MainActivity extends Activity implements DatePickerFragment.DatePickerListener{
    private EditText descricao;
    private Spinner categorySpinner;
    private Button dataButton;
    private Date data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new Date();
        setContentView(R.layout.activity_main);
        this.descricao = findViewById(R.id.description);
        this.dataButton = findViewById(R.id.dataButton);
        this.categorySpinner = findViewById(R.id.categorySpinner);
        Calendar calendar = Calendar.getInstance();
        int dia =  calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int ano = calendar.get(Calendar.YEAR);
        this.dataButton.setText(makeData(dia,mes,ano));
        String []values  = new String[]{"Vias públicas de acesso","Equipamentos comunitários","Limpeza urbana e saneamento"};
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,values);
        categorySpinner.setAdapter(adapter);
    }

    private String makeData(int dia, int mes, int ano){
        return dia+"/"+(mes+1)+"/"+ano;
    }

    public void continueClik(View view){
        Intent intent = new Intent(this,FotoVideoActivity.class);
        Bundle bundle = new Bundle();
        Denuncia denuncia = new Denuncia();
        denuncia.setData(data);
        denuncia.setDescricao(descricao.getText().toString());
        Denuncia.Categoria categoria = Denuncia.Categoria.VIAS_PUBLICAS;
        categoria.setValor(this.categorySpinner.getSelectedItemPosition());
        denuncia.setCategoria(categoria);
        intent.putExtra("denuncia",denuncia);
        startActivity(intent);
    }

    public void dataButtonClicked(View view){
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(this.getFragmentManager(),"Data Denuncia");

    }

    @Override
    public void datePickerChosed(String datpicker, int dia, int mes, int ano) {
        Calendar calendar =  Calendar.getInstance();
        calendar.set(Calendar.MONTH,mes);
        calendar.set(Calendar.YEAR,ano);
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        data.setTime(calendar.getTimeInMillis());
    }
}
