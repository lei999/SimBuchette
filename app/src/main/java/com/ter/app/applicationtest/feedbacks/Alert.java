package com.ter.app.applicationtest.feedbacks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Classe représentant les messages d'alerte
 */
public class Alert {
    public enum Action{AUTORISATION_ACTION, REFUS_ACTION, AUTORISATION_ET_MESSAGE, REFUS_ET_MESSAGE}

    /**
     * Le builder de la fenêtre du message d'alerte
     */
    AlertDialog.Builder builder;

    /**
     * La fenêtre du message d'alerte
     */
    AlertDialog alertDialog;


    /**
     * Constructeur de la fenêtre du message d'alerte
     * @param c le contexte dans lequel est instancié le message d'alerte
     * @param message le message à afficher
     */
    public Alert(Context c, String message){
        builder= new AlertDialog.Builder(c);
        builder.setMessage(message)

                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        alertDialog = builder.create();
    }

    /**
     * Méthode permettant d'afficher la fenêtre du message d'alerte
     */
    public void show(){
        alertDialog.show();
    }


}
