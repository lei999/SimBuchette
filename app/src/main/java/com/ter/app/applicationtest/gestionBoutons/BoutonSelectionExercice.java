package com.ter.app.applicationtest.gestionBoutons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.FenetresActivity.Choix_Exercice;

/**
 * Classe représentant le bouton permettant de retourner à l'écran de sélection des exercices
 */
public class BoutonSelectionExercice extends Bouton{
    /**
     * L'activité à laquelle est associée le bouton ( permet de changer d'activité)
     */
    Activity activity;

    /**
     * Constructeur du bouton Suivant
     * @param a L'activité à laquelle est associée le bouton
     * @param b le Button dans le xml, représentant le bouton
     */
    public BoutonSelectionExercice(Activity a, Button b){
        super();
        bouton = b;
        activity = a;
        bouton.setOnClickListener(clickListener);
    }


    /**
     * Le clickListener du bouton Suivant
     */
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // créer l'activité suivante (FenetreExercice)
            Intent intent = new Intent(activity, Choix_Exercice.class);
            // démarre l'activité suivante
            activity.startActivity(intent);
        }
    };

    public void setClickListener(View.OnClickListener clickListener){
        bouton.setOnClickListener(clickListener);
    }

    public void setText(String text){
        bouton.setText(text);
    }
}
