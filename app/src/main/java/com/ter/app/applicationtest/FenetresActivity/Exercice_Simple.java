package com.ter.app.applicationtest.FenetresActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ter.app.applicationtest.Contenants.Boite;
import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.Contenants.Parametres;
import com.ter.app.applicationtest.Contenants.Reserve;
import com.ter.app.applicationtest.Contenants.ZoneDupliquer;
import com.ter.app.applicationtest.MQTT.MQTT;
import com.ter.app.applicationtest.feedbacks.Etat_simulation;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.Groupement100;
import com.ter.app.applicationtest.objets.Groupement100B;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.gestionBoutons.BoutonAnnuler;
import com.ter.app.applicationtest.gestionBoutons.BoutonTerminer;
import com.ter.app.applicationtest.gestionBoutons.BoutonVider;
import com.ter.app.applicationtest.objets.Buchette;
import com.ter.app.applicationtest.objets.Groupement10;
import com.ter.app.applicationtest.objets.Groupement3;
import com.ter.app.applicationtest.parametresExercices.Consigne;
import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.Contenants.Table;
import com.ter.app.applicationtest.gestionBoutons.BoutonReset;


/**
 * Classe représentant la fenêtre d'exercice (avec des "petites" boites, sans zone de groupement)
 */
public class Exercice_Simple extends ActionBarActivity {

    public static AlertDialog attente;
    public static boolean quit = false;
    public static boolean onPause = false;
    public static boolean changed = false;



    /**
     * Méthode appellée lors de la création de la fenêtre
     * @param savedInstanceState l'état de l'instance sauvegardé
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // appel à la méthode de la classe mère
        super.onCreate(savedInstanceState);

        // on affiche le contenu de l'xml correspondant
        setContentView(R.layout.activity_exercice_simple);

        // on met à jour l'activité courante de MQTT
        MQTT.getInstance().setCurrentActivity(Exercice_Simple.this);

        if(changed){
            changed = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attention")
                    .setMessage("L'exercice à changé")


                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }


                    })
                    .setCancelable(false);
            attente = builder.create();
            attente.show();
        }

        // on initialise l'état de la simulation
        Etat_simulation.init();

        // on met à jour le nom de la fenêtre pour qu'elle corresponde à l'exercice
        setTitle(Traces.nomExercice);

        // on récupère l'affichage de la consigne de la fenêtre
        Consigne.consigne = (TextView) findViewById(R.id.consigne);
        // on met à jour l'affichage avec la consigne configurée
        Consigne.consigne.setText(Config.consigne);

        BoutonVider v = new BoutonVider((RelativeLayout) findViewById(R.id.zoneVider));


        // on récupère la réserve des buchettes
        ListContenant.reserveBuchette = new Reserve((RelativeLayout) findViewById(R.id.reserveBuchette));
        // on récupère la réserve des groupements
        ListContenant.reserveGroupement = new Reserve((RelativeLayout) findViewById(R.id.reserveGroupement));;


        // on cache l'affichage du nom de la réserve de buchettes si il n'y a pas de buchettes disponible initialement pour l'exercice
        TextView textBuchette;
        textBuchette = (TextView) findViewById(R.id.buchette);
        if(Config.nb_buchettes == 0){
            textBuchette.setVisibility(View.INVISIBLE);
        }

        // on cache l'affichage du nom de la réserve de groupement si il n'y a pas de grouepement disponible initialement pour l'exercice
        TextView textGroupement;
        textGroupement = (TextView) findViewById(R.id.groupement);
        if(Config.nb_groupement3 == 0){
            textGroupement.setVisibility(View.INVISIBLE);
        }


        // on associe le contexte de l'exercice aux boutons d'intéraction
        BoutonAnnuler.setContetxt(Exercice_Simple.this);
        BoutonReset.setActivity(Exercice_Simple.this);
        BoutonTerminer.setActivity(Exercice_Simple.this);

        // on récupères les contenants de la fenêtre
        ListContenant.table = new Table((RelativeLayout) findViewById(R.id.table), Exercice_Simple.this);
        ListContenant.boite0 = new Boite((RelativeLayout) findViewById(R.id.contenant), 0, Exercice_Simple.this, (RelativeLayout) findViewById(R.id.elements_boite0), (TextView) findViewById(R.id.buchette_boite0), (TextView) findViewById(R.id.groupement10_boite0),(TextView) findViewById(R.id.groupement100_boite0), (TextView) findViewById(R.id.groupement100b_boite0));
        ListContenant.boite1 = new Boite((RelativeLayout) findViewById(R.id.contenant2), 1, Exercice_Simple.this, (RelativeLayout) findViewById(R.id.elements_boite0), (TextView) findViewById(R.id.buchette_boite0), (TextView) findViewById(R.id.groupement10_boite0),(TextView) findViewById(R.id.groupement100_boite0), (TextView) findViewById(R.id.groupement100b_boite0));
        ListContenant.boite2 = new Boite((RelativeLayout) findViewById(R.id.contenant3), 2, Exercice_Simple.this, (RelativeLayout) findViewById(R.id.elements_boite0), (TextView) findViewById(R.id.buchette_boite0), (TextView) findViewById(R.id.groupement10_boite0),(TextView) findViewById(R.id.groupement100_boite0), (TextView) findViewById(R.id.groupement100b_boite0));
        ListContenant.boite3 = new Boite((RelativeLayout) findViewById(R.id.contenant4), 3, Exercice_Simple.this, (RelativeLayout) findViewById(R.id.elements_boite0), (TextView) findViewById(R.id.buchette_boite0), (TextView) findViewById(R.id.groupement10_boite0),(TextView) findViewById(R.id.groupement100_boite0), (TextView) findViewById(R.id.groupement100b_boite0));


        // GESTION DES BUCHETTES DE LA RÉSERVE
        for(int i = 0; i < Config.nb_buchettes; i++){
            Buchette b = new Buchette(Exercice_Simple.this);
            b.getImageView().setTranslationX(Parametres.X);
            ListContenant.reserveBuchette.addView(b.getImageView());
        }

/*        // GESTION DES GROUPEMENTS 3 DE LA RÉSERVE
        for(int i = 0; i < Config.nb_groupement3; i++){
            Groupement3 g = new Groupement3(Exercice_Simple.this);
            g.getImageView().setTranslationX(40f);
            ListContenant.reserveGroupement.addView(g.getImageView());
        }
        */

        // GESTION DES GROUPEMENTS 10 DE LA RÉSERVE
        for(int i = 0; i < Config.nb_groupement10; i++){
            Groupement10 g = new Groupement10(Exercice_Simple.this);
            g.getImageView().setTranslationX(Parametres.X);
            ListContenant.reserveGroupement.addView(g.getImageView());
        }

        // GESTION DES GROUPEMENTS 100 DE LA RÉSERVE
        for(int i = 0; i < Config.nb_groupement100; i++){
            Groupement100 g = new Groupement100(Exercice_Simple.this);
            g.getImageView().setTranslationX(Parametres.X);
            g.getImageView().setTranslationY(Parametres.Y_4L_T3);
            ListContenant.reserveGroupement.addView(g.getImageView());
            Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.RESERVE);
        }


        // GESTION DES GROUPEMENTS 100B DE LA RÉSERVE
        for(int i = 0; i < Config.nb_groupement100b; i++){
            Groupement100B g = new Groupement100B(Exercice_Simple.this);
            g.getImageView().setTranslationX(Parametres.X *2);
            g.getImageView().setTranslationY(Parametres.Y_4L_T3);
            ListContenant.reserveGroupement.addView(g.getImageView());
            Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.RESERVE);
        }

        // si l'exercice à besoin de la zone pour dupliquer on l'initialise
        if(Config.utilisation_dupliquer)ListContenant.zoneDupliquer = new ZoneDupliquer(Exercice_Simple.this,(RelativeLayout) findViewById(R.id.zone_dupliquer));
        // sinon la cache
        else{
            TextView dupliquer = (TextView) findViewById(R.id.dupliquer);
            dupliquer.setVisibility(View.INVISIBLE);
        }

        if(!Config.utilisation_poubelle){
            RelativeLayout poubelle = (RelativeLayout) findViewById(R.id.zoneVider);
            poubelle.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    /**
     * Méthode appellée lors de la création des menus de la fenêtre
     * @param menu le menu de la fenêtre
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exerice_simple, menu);
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
        // si l'icone du bouton terminer est sélectionné
        if(id == R.id.action_terminer){
            // on effectue l'action associée
            BoutonTerminer.action();
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
    public void onBackPressed() {
        // do nothing.
    }
}
