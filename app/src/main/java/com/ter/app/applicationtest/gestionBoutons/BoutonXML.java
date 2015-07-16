package com.ter.app.applicationtest.gestionBoutons;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.FenetresActivity.Exercice_Groupement;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Grandes_Boites;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Simple;
import com.ter.app.applicationtest.Parser.Parser;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.parametresExercices.Config;

import java.io.File;

/**
 * Created by myriam on 09/07/15.
 */
public class BoutonXML {
    public Button b;
    public File xml;
    public String label;
    public Activity activity;

    public BoutonXML(Activity activity, Button b,String label,  File xml){
        this.activity = activity;
        this.b = b;
        this.label = label;
        b.setText(label);
        this.xml = xml;
        b.setOnClickListener(clickListener);
    }

    Button.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Parser p = new Parser(xml);
            Traces.addExercice(label);
            if(Config.utilisation_zone_groupements){
                Intent intent = new Intent(activity, Exercice_Groupement.class);
                // démarre l'activité suivante
                activity.startActivity(intent);
            }
            else{
                if(Config.exercice_grandes_boites){
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
