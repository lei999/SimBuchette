package com.ter.app.applicationtest.gestionBoutons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.FenetresActivity.Exercice_Groupement;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Grandes_Boites;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Simple;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.Parser.Parser;
import com.ter.app.applicationtest.feedbacks.Traces;

/**
 * Classe représentant le bouton de sélection d'un exercice
 */
public class BoutonExercice extends Bouton{
    /**
     * L'activité à laquelle est associée le bouton ( permet de changer d'activité)
     */
    Activity activity;

    /**
     * Exercice sélectionné
     */
    public static int choixsenario;

    /**
     * Exercice associé au bouton
     */
    public int choix;

    /**
     * Constructeur du bouton Suivant
     * @param a L'activité à laquelle est associée le bouton
     * @param b le Button dans le xml, représentant le bouton
     */
    public BoutonExercice(Activity a, Button b, int choix){
        super();
        bouton = b;
        activity = a;
        this.choix = choix;
        this.choixsenario = choix;
        bouton.setOnClickListener(clickListener);
    }


    /**
     * Le clickListener du bouton Suivant
     */
    protected View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            choixsenario = choix;
            // créer l'activité suivante (FenetreExercice)
            Traces.addExercice("" + choixsenario);
            Parser p = new Parser(activity, BoutonExercice.choixsenario);

            if(Config.utilisation_zone_groupements){
                Intent intent = new Intent(activity, Exercice_Groupement.class);
                // démarre l'activité suivante
                activity.startActivity(intent);
            }
            else{
                if(Config.exercice_complement){
                    Intent intent = new Intent(activity, Exercice_Grandes_Boites.class);
                    // démarre l'activité suivante
                    activity.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(activity, Exercice_Simple.class);
                    // démarre l'activité suivante
                    activity.startActivity(intent);
                }
            }

        }
    };
}
