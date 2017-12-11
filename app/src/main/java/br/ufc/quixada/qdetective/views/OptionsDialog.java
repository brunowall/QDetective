package br.ufc.quixada.qdetective.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import br.ufc.quixada.qdetective.R;

/**
 * Created by darkbyte on 11/12/17.
 */
public class OptionsDialog extends DialogFragment {

    private OptionsDialogListener listener;

    public interface OptionsDialogListener{
        public void onClickEditar();
        public void onClickRemover();
        public void onClickDetalhes();
        public void onClickCompartilhar();

    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        this.listener=(OptionsDialogListener)this.getActivity();

        final CharSequence[] items = {
               "Editar",
                "Compartilhar",
                "Ver detalhes",
                "Apagar"};
        builder.setTitle("Opções");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        listener.onClickEditar();
                        break;
                    case 1:
                        listener.onClickCompartilhar();
                        break;
                    case 2:
                        listener.onClickDetalhes();
                        break;
                    case 3:
                        listener.onClickRemover();
                        break;
                }
            }
        });
        return builder.create();
    }
}