package br.ufc.quixada.qdetective.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import br.ufc.quixada.qdetective.R;

/**
 * Created by darkbyte on 10/12/17.
 */

public class TelaInicial extends AppCompatActivity {
    public EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicial_layout);
        editText = (EditText) findViewById(R.id.editTextUsuario);
    }

    public void entrar(View view){
        Intent intent = new Intent(this,OptionScreen.class);
        intent.putExtra("usuario",editText.getText().toString());
        startActivity(intent);
    }
}
