package br.ufc.quixada.qdetective.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import br.ufc.quixada.qdetective.R;

/**
 * Created by darkbyte on 10/12/17.
 */

public class OptionScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_screen);
    }
    public void criarDenuncia(View view){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("usuario",getIntent().getExtras().getString("usuario"));
        startActivity(intent);

    }
    public void verDenuncias(View view){

    }
    public void verDenunciasLocais(View view){
        Intent intent = new Intent(this,ShowDenuncias.class);
        intent.putExtra("local",true);
        startActivity(intent);

    }
}
