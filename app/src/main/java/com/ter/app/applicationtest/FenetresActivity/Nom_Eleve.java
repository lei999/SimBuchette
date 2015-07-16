package com.ter.app.applicationtest.FenetresActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ter.app.applicationtest.MQTT.MQTT;
import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.parametresExercices.Config;


/**
 * Classe représentant la fenêtre pour entrer le nom d'un élève
 */
public class Nom_Eleve extends ActionBarActivity {

    /**
     * L'EditText permettant d'entrer le nom de l'élève
     */
    private EditText nomEntree;
    /**
     * Le nom de l'élève
     */
    private String nom;

    /**
     * Le bouton permettant de passer à la page suivante
     */
    private Button OK;

    private EditText id;

    public static AlertDialog dialog;

    public static boolean autonome = false;
    public static boolean quit = false;
    /**
     * Le clickListener permettant de passer à la page suivante
     */
    private View.OnClickListener pageSuivante = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nom = nomEntree.getText().toString();
            Traces.id = new Integer(id.getText().toString());

            if(autonome) {
                if (!nom.equals("")) {
                    Traces.id = 0;
                    Traces.nomEleve = nom;
                    Traces.addEleve(nom);
                    // créer l'activité suivante (FenetreExercice)
                    Intent intent = new Intent(Nom_Eleve.this, Choix_Exercice.class);
                    // démarre l'activité suivante
                    startActivity(intent);
                }
            }
            else {
                if (!nom.equals("") && !id.getText().toString().equals("")) {
                    Traces.nomEleve = nom;
                    Traces.id = new Integer(id.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Nom_Eleve.this);
                    builder.setTitle("Ecran d'attente")
                            .setMessage("Attends qu'un exercice se lance sur ta tablette")
                            .setCancelable(false);
                    dialog = builder.create();
                    dialog.show();
                    MQTT.getInstance().setCurrentActivity(Nom_Eleve.this);
                }
            }


        }
    };

    public void startExercice(String exercice) {
        Traces.nomExercice = exercice;
        if(Config.utilisation_zone_groupements){
            Intent intent = new Intent(Nom_Eleve.this, Exercice_Groupement.class);
            // démarre l'activité suivante
            Nom_Eleve.this.startActivity(intent);
        }
        else{
            if(Config.exercice_complement){
                Intent intent = new Intent(Nom_Eleve.this, Exercice_Grandes_Boites.class);
                // démarre l'activité suivante
                Nom_Eleve.this.startActivity(intent);
            }
            else {
                Intent intent = new Intent(Nom_Eleve.this, Exercice_Simple.class);
                // démarre l'activité suivante
                Nom_Eleve.this.startActivity(intent);
            }
        }
    }



    /**
     * Méthode appellée lors de la création de la fenêtre
     * @param savedInstanceState l'état de l'instance sauvegardé
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nom_eleve);
        nomEntree = (EditText)findViewById(R.id.nom);
        id = (EditText) findViewById(R.id.id);
        id.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(autonome)id.setVisibility(View.INVISIBLE);
        OK = (Button)findViewById(R.id.OK);
        OK.setOnClickListener(pageSuivante);
        Traces.resetTracesEleve();
    }


    @Override
    /**
     * Méthode appellée lors de la création des menus de la fenêtre
     * @param menu le menu de la fenêtre
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nom_eleve, menu);
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
