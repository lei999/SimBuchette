package com.ter.app.applicationtest.FenetresActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.MQTT.MQTT;
import com.ter.app.applicationtest.R;


/**
 * Classe représentant la fenêtre d'accueil de l'application. C'est sur cette fenêtre que l'utilisateur peut choisir
 * entre utiliser l'application de manière autonome ou en collaboration avec l'outil d'orchestration.
 */
public class Accueil extends ActionBarActivity {

    public static boolean quit = false;

    /**
     * Méthode appellée lors de la création de la fenêtre
     * @param savedInstanceState l'état de l'instance sauvegardé
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // appel à la méthode de la classe mère
        super.onCreate(savedInstanceState);
        // on affiche le contenu de l'xml correspondant
        setContentView(R.layout.activity_accueil);

        // on met à jour l'activité courante de MQTT
        MQTT.getInstance().setCurrentActivity(Accueil.this);

        // on récupère le bouton permettant de choisir que l'application fonctionne en autonomie
        Button autonome = (Button) findViewById(R.id.autonome);
        // on récupère le bouton permettant de choisir que l'application fonctionne en collaboration avec l'outil d'orchestration
        Button comunication = (Button) findViewById(R.id.comunication);

        // le clicListener du bouton autonome
        Button.OnClickListener clicAutonome = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on précise que l'application doit fonctionner de façon autonome
                Nom_Eleve.autonome = true;
                // on démarre l'activtité correpondant à la saisie du nom de l'élève
                Intent activity = new Intent(Accueil.this, Nom_Eleve.class);
                startActivity(activity);
            }
        };

        // le clicListener du bouton permettant la collaboration avec l'outil d'orchestration
        Button.OnClickListener clicComunication = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on précise que l'application ne fonctionnera pas de façon autonome
                Nom_Eleve.autonome = false;
                // on démarre l'activtité correpondant à la saisie du nom de l'élève
                Intent activity = new Intent(Accueil.this, Nom_Eleve.class);
                startActivity(activity);
            }
        };

        // on ajoute le clicListener au bouton autonome
        autonome.setOnClickListener(clicAutonome);
        // on ajoute le clicListener au bouton permettant la collaboration avec l'outil d'orchestration
        comunication.setOnClickListener(clicComunication);

        // on précise à toutes les activités que l'on ne veut pas quitter l'application
        Choix_Exercice.quit = false;
        Exercice_Grandes_Boites.quit = false;
        Exercice_Groupement.quit = false;
        Exercice_Simple.quit = false;
        Nom_Eleve.quit = false;
        Exercice_Terminer.quit = false;

    }

    @Override
    /**
     * Méthode appellée lors de la création des menus de la fenêtre
     * @param menu le menu de la fenêtre
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accueil, menu);
        return true;
    }

    /**
     * Méthode appellée lors de la sélection d'un item du menu
     * @param item l'item sélectionné
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Méthode appellée lors de la fermeture de la fenêtre
     */
    protected void onPause(){
        super.onPause();
    }

    /**
     * Méthode appélée lors que la fenêtre est affichée de nouveau
     */
    protected void onResume(){
        super.onResume();
        // si l'on est dans le processus pour quitter l'application
        if(quit){
            // on quitte cette fenêtre
            finish();
        }
    }
}
