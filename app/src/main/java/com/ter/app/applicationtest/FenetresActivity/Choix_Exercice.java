package com.ter.app.applicationtest.FenetresActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.gestionBoutons.BoutonXML;
import com.ter.app.applicationtest.parametresExercices.ComparateurNomFichier;
import com.ter.app.applicationtest.parametresExercices.FichierXML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant l'écran permettant de choisir un exerice à résoudre
 */
public class Choix_Exercice extends ActionBarActivity {
    public static boolean quit = false;

    /**
     * Le nombre de colonne de la table, c'est à dire le nombre de boutons que l'on veut afficher sur une ligne
     */
    private final int nombreColonnes = 3;
    /**
     * La Table regroupant les boutons d'exercice
     */
    private TableLayout tableLayout;


    @Override
    /**
     * Méthode appellée lors de la création de la fenêtre
     * @param savedInstanceState l'état de l'instance sauvegardé
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_exercice);
        // on récupère la table contenant les boutons d'exercice
        tableLayout = (TableLayout) findViewById(R.id.choixExerciceTable);
        // on lit les fichiers qui sont dans le répertoire spécifique
        readFilesFromDirectory();
        // on récupère le bouton permettant de saisir un nouvel élève
        Button button = (Button) findViewById(R.id.nouvelEleve);
        // on précise le clicklistener du bouton
        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          // lorsque l'on clic sur le bouton, on démarre l'activité correspondant à l'écran de saisie d'un élève
                                          Intent activity = new Intent(Choix_Exercice.this, Nom_Eleve.class);
                                          startActivity(activity);
                                      }
                                  }
        );

    }

    private void readFilesFromDirectory() {
        // on récupère le dossier qui est dans Android/data/com.ter.app.applicationtest
        File directory = getExternalFilesDir(null);
        try {
            // on récupère l'ensemebles des fichiers du répertoire
            File[] files = directory.listFiles();
            // pour tous
            List<FichierXML> fichierXMLs = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                fichierXMLs.add(new FichierXML(files[i].getName().toLowerCase(), files[i]));


                Log.d("FILES", files[i].getName());
//                StringBuilder stringBuilder = new StringBuilder();
//                BufferedReader br = new BufferedReader(new FileReader(files[i]));
//                String line = br.readLine();
//                while (line != null) {
//                    stringBuilder.append(line)
//                            .append("\n");
//                    line = br.readLine();
//                }
                //addBoutonToTableLayout(i, files[i].getName(),files[i]);
            }
            Collections.sort(fichierXMLs, new ComparateurNomFichier());

            for (int i = 0; i < fichierXMLs.size(); i++) {
                addBoutonToTableLayout(i, fichierXMLs.get(i).getNom(), fichierXMLs.get(i).getFichier());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void addBoutonToTableLayout(int i, final String label, File xml) {
        // la ligne de la table
        TableRow tableRow;
        // on récuère les paramètres du layout de la table
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        // si le bouton est dépasse le nombre de colonne on passe à la ligne
        if (i % nombreColonnes == 0) {
            // on créer une nouvelle ligne
            tableRow = new TableRow(Choix_Exercice.this);
        }
        // sinon
        else {
            // on ajoute le bouton à la ligne (après les précédents
            tableRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
        }
        // on créer les paramètres de layout de la ligne. On précise que le bouton aura un poids de 1
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        // on précise que le poids total des objets sur la ligne est de 3
        tableRow.setWeightSum(3f);
        // on parse le nom du fichier qui a l'extension (.xml)
        String[] s = label.split(".x");
        String nom = "";
        // on récupère le nom du fichier sans l'extension (.xml)
        if(s.length >0) nom = s[0];
        // on créer le bouton permettant de séléctionner l'exercice
        BoutonXML button = new BoutonXML(Choix_Exercice.this, new Button(Choix_Exercice.this),nom, xml);
        // on ajoute le bouton à la ligne
        tableRow.addView(button.b, rowParams);
        // si on a crée une nouvelle ligne
        if (i % nombreColonnes == 0) {
            // on ajoute cette ligne à la table
            tableLayout.addView(tableRow, layoutParams);
        }
    }

    @Override
    /**
     * Méthode appellée lors de la création des menus de la fenêtre
     * @param menu le menu de la fenêtre
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choix_exercice, menu);

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
