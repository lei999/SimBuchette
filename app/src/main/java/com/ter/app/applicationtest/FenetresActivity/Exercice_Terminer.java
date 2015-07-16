package com.ter.app.applicationtest.FenetresActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.MQTT.MQTT;
import com.ter.app.applicationtest.Parser.Parser;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.feedbacks.Fichier;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.gestionBoutons.BoutonSelectionExercice;
import com.ter.app.applicationtest.parametresExercices.Config_Attendue;

/**
 * Classe représentant la fenêtre d'exercice terminé
 */
public class Exercice_Terminer extends ActionBarActivity {
    /**
     * TextView dans le xml, correspondant à l'affichage du résultat de l'exercice
     */
    TextView resultat;

    /**
     * True si l'exerice est réussi, false sinon
     */
    boolean result = false ;

    /**
     * Le bouton permettant de continuer, sortir ou d'aller sélectionner un nouvel élève
     */
    BoutonSelectionExercice b;

    /**
     * Le popup de la fenêtre
     */
    public static AlertDialog dialog;
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
        setContentView(R.layout.activity_exercice__terminer);

        // on récupère le TextView correspondant à l'affichage du résultat de l'exercice
        resultat = (TextView) findViewById(R.id.resultat);
        // on récupère le bouton
        b = new BoutonSelectionExercice(Exercice_Terminer.this,(Button)findViewById(R.id.selectionExercice));
        // si l'application n'est pas autonome

        // on affiche le résultat de la résolution de l'exercice
        afficheResult();
        // si l'exercice est réussi on ajoute une trace d'exercice précisant que l'exercice est réussi
        if(result)Traces.addTerminerExercice("reussi");
        // sinon, si l'élève n'a pas réussi l'exercice, on ajoute une trace d'exercice précisant que l'élève n'a pas réussi l'exercice
        else Traces.addTerminerExercice("echec");
    }


    /**
     * Le clicListener permettant d'attendre un exercice (utilisé en cas de collaboration avec l'outil d'orchestration)
     */
    public View.OnClickListener attenteExercice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // on créer le popup
            AlertDialog.Builder builder = new AlertDialog.Builder(Exercice_Terminer.this);
            // on précise son titre, le message qui est affiché, le fait que le popup ne peut pas être fermé
            builder.setTitle("Ecran d'attente")
                    .setMessage("Attends qu'un exercice se lance sur ta tablette")
                    .setCancelable(false);
            dialog = builder.create();
            // on affiche le popup
            dialog.show();
            // on met à jour l'activité courante de MQTT
            MQTT.getInstance().setCurrentActivity(Exercice_Terminer.this);
        }
    };


    /**
     * Méthode permettant de démarer un exercice
     * @param exercice le nom de l'exercice que l'on veut démarrer
     */
    public void startExercice(String exercice) {
        Fichier.write(com.ter.app.applicationtest.feedbacks.Traces.stringTraceEleve());
        Traces.resetTracesEleve();
        Traces.addExercice(exercice);
        // on ferme le popup
        dialog.cancel();
        // si l'exercice a besoin de la zone de groupement
        if(Config.utilisation_zone_groupements){
            // on démarre l'activité qui a la zone de groupement
            Intent intent = new Intent(Exercice_Terminer.this, Exercice_Groupement.class);
            Exercice_Terminer.this.startActivity(intent);
        }
        // sinon, si l'exercice n'a pas besoin de la zone de groupement
        else{
            // si l'exerice a besoin de grandes boites
            if(Config.exercice_grandes_boites){
                // on démarre l'activité qui a des grandes boites
                Intent intent = new Intent(Exercice_Terminer.this, Exercice_Grandes_Boites.class);
                Exercice_Terminer.this.startActivity(intent);
            }
            // sinon, si l'exercice n'as pas besoin de grandes boites (ni de la zone de groupement)
            else {
                // on démarre l'activité qui n'a pas de zone de groupement et qui a des petites boites
                Intent intent = new Intent(Exercice_Terminer.this, Exercice_Simple.class);
                Exercice_Terminer.this.startActivity(intent);
            }
        }
    }



    @Override
    /**
     * Méthode appellée lors de la création des menus de la fenêtre
     * @param menu le menu de la fenêtre
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercice__terminer, menu);
        return true;
    }

    @Override
    /**
     * Méthode appellée lors de la sélection d'un item du menu
     * @param item l'item sélectionné
     */
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
     * Méthode permettant d'afficher le message correspondant à la solution qu'a apporté l'élève à l'exercice
     */
    public void afficheResult(){
        // booléen permettant de savoir si une des configurations prévue a été trouvée
        boolean config_trouve = false;
        // pour toutes les configurations prévues
        Log.d("fin", "taille config[] " + Config.configurations_attendues.size());
        for(Config_Attendue config_attendue : Config.configurations_attendues) {
            // si la configuration apportée par l'élève corr
            if (isResult(config_attendue)) {
                config_trouve = true;
                if (config_attendue.nom.equals("bonne_solution")) {
                    result = true;
                }
                    switch (config_attendue.comportement_fin) {
                        case CONTINUER_ET_MESSAGE:
                            if(!Nom_Eleve.autonome){
                                // on associe le clicListener permettant d'attendre l'arrivé d'un autre exercice
                                b.setClickListener(attenteExercice);
                            }
                            else {
                                b.setClickListener(continu);
                            }
                            // on met à jour le texte qui s'affiche sur le bouton
                            b.setText("Exercice Suivant");
                            resultat.setText(config_attendue.message_continuer);
                            break;
                        case CONTINUER:
                            if(!Nom_Eleve.autonome){
                                // on associe le clicListener permettant d'attendre l'arrivé d'un autre exercice
                                b.setClickListener(attenteExercice);
                            }
                            else b.setClickListener(continu);
                            b.setText("Exercice Suivant");
                            break;
                        case SORTIE:
                            b.setClickListener(sortie);
                            b.setText("quitter");
                            break;
                        case SORTIE_ET_MESSAGE:
                            b.setClickListener(sortie);
                            b.setText("quitter");
                            resultat.setText(config_attendue.message_sortie);
                            break;
                    }
                }
            }
        if(!config_trouve){
                switch (Config.conguration_par_defaut.comportement_fin) {
                    case CONTINUER_ET_MESSAGE:
                        if(!Nom_Eleve.autonome){
                            // on associe le clicListener permettant d'attendre l'arrivé d'un autre exercice
                            b.setClickListener(attenteExercice);
                        }
                        else {
                            b.setClickListener(continu);
                        }
                        // on met à jour le texte qui s'affiche sur le bouton
                        b.setText("Exercice Suivant");
                        resultat.setText(Config.conguration_par_defaut.message_continuer);
                        break;
                    case CONTINUER:
                        if(!Nom_Eleve.autonome){
                            // on associe le clicListener permettant d'attendre l'arrivé d'un autre exercice
                            b.setClickListener(attenteExercice);
                        }
                        else {
                            b.setClickListener(continu);
                        }
                        // on met à jour le texte qui s'affiche sur le bouton
                        b.setText("Exercice Suivant");
                        break;
                    case SORTIE:
                        b.setClickListener(sortie);
                        b.setText("quitter");
                        break;
                    case SORTIE_ET_MESSAGE:
                        b.setClickListener(sortie);
                        b.setText("quitter");
                        resultat.setText(Config.conguration_par_defaut.message_sortie);
                        break;
                }
            }
    }


    public boolean isResult(Config_Attendue config_attendue){
        boolean resultat = true;
        resultat = resultat && ListContenant.boite0.nbBuchettes() == config_attendue.boites.get(0).nb_buchettes;
        resultat = resultat && ListContenant.boite0.nbGroupement10() == config_attendue.boites.get(0).nb_groupement10;
        resultat = resultat && ListContenant.boite0.nbGroupement100() == config_attendue.boites.get(0).nb_groupement100;
       resultat = resultat && ListContenant.boite0.nbGroupement100b() == config_attendue.boites.get(0).nb_groupement100b;

        resultat = resultat && ListContenant.boite1.nbBuchettes() == config_attendue.boites.get(1).nb_buchettes;
        resultat = resultat && ListContenant.boite1.nbGroupement10() == config_attendue.boites.get(1).nb_groupement10;
        resultat = resultat && ListContenant.boite1.nbGroupement100() == config_attendue.boites.get(1).nb_groupement100;
        resultat = resultat && ListContenant.boite1.nbGroupement100b() == config_attendue.boites.get(1).nb_groupement100b;

        resultat = resultat && ListContenant.boite2.nbBuchettes() == config_attendue.boites.get(2).nb_buchettes;
        resultat = resultat && ListContenant.boite2.nbGroupement10() == config_attendue.boites.get(2).nb_groupement10;
        resultat = resultat && ListContenant.boite2.nbGroupement100() == config_attendue.boites.get(2).nb_groupement100;
        resultat = resultat && ListContenant.boite2.nbGroupement100b() == config_attendue.boites.get(2).nb_groupement100b;

        resultat = resultat && ListContenant.boite3.nbBuchettes() == config_attendue.boites.get(3).nb_buchettes;
        resultat = resultat && ListContenant.boite3.nbGroupement10() == config_attendue.boites.get(3).nb_groupement10;
        resultat = resultat && ListContenant.boite3.nbGroupement100() == config_attendue.boites.get(3).nb_groupement100;
        resultat = resultat && ListContenant.boite3.nbGroupement100b() == config_attendue.boites.get(3).nb_groupement100b;

        resultat = resultat & config_attendue.table_vide == ListContenant.table.isVide();

        return resultat;
    }

    Button.OnClickListener continu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Exercice_Terminer.this, Choix_Exercice.class);
            startActivity(intent);
        }
    };

    Button.OnClickListener sortie = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fichier.write(com.ter.app.applicationtest.feedbacks.Traces.stringTraceEleve());
            Traces.resetTracesEleve();
            Choix_Exercice.quit = true;
            Exercice_Grandes_Boites.quit = true;
            Exercice_Groupement.quit = true;
            Exercice_Simple.quit = true;
            Nom_Eleve.quit = true;
            Exercice_Terminer.quit = true;
            Accueil.quit = true;
            SelectionTablette.quit = true;
            finish();
            //moveTaskToBack(true);
        }
    };

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
