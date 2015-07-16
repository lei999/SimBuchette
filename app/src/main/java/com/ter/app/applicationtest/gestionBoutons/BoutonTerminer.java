package com.ter.app.applicationtest.gestionBoutons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.FenetresActivity.Exercice_Terminer;

/**
 * Classe représentant le bouton Terminer
 */
public class BoutonTerminer extends Bouton {

    public static enum Action{TERMINER, SUIVANT}




    /**
     * L'activité à laquelle est associée le bouton ( permet de changer d'activité)
     */
    static Activity activity;

    /**
     * Constructeur du bouton Terminer
     * @param a L'activité à laquelle est associée le bouton
     * @param b le Button dans le xml, représentant le bouton
     */
    public BoutonTerminer(Activity a, Button b){
        super();
        bouton = b;
        activity = a;
        bouton.setOnClickListener(clickListener);
    }


    public static void setActivity (Activity a){
        activity = a;
    }



    public static void action(){
        Intent intent = new Intent(activity, Exercice_Terminer.class);
        // démarre l'activité suivante
        activity.startActivity(intent);
    }
}
