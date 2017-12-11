package br.ufc.quixada.qdetective.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by null on 28/09/17.
 */

public class RemoveConfirmDialog extends DialogFragment {
    public interface RemoveConfirmListener{
        public void onClickSim();
        public void onClickNao();
    }
    private RemoveConfirmListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        this.listener = (RemoveConfirmListener)this.getActivity();
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClickSim();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClickNao();
            }
        });

        builder.setMessage("Deseja realmente apagar a Denuncia?");
        return builder.create();
    }
}
