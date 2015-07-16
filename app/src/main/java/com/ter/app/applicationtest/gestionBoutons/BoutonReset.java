package com.ter.app.applicationtest.gestionBoutons;


import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.feedbacks.Traces;

/**
 * Classe représentant le bouton Reset de la simulation
 */
public class BoutonReset extends Bouton {
    /**
     * L'activité à laquelle est associée le bouton ( permet de recharger l'activité)
     */
    static Activity activity;


    /**
     * Constructeur du bouton Reset
     * @param a L'activité à laquelle est associée le bouton
     * @param b le Button dans le xml, représentant le bouton Reset
     */
    public BoutonReset(Activity a, Button b){
        super();
        bouton = b;
        activity = a;
        bouton.setOnClickListener(clickListener);
    }

    public static void setActivity (Activity a){
        activity = a;
    }


    /**
     * Action associé au bouton Reset
     */
    public static void action(){
        // recréer l'activité associée au bouton Reset
        Traces.reset();
        activity.recreate();
    }
}
