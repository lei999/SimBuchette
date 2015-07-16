package com.ter.app.applicationtest.feedbacks;


import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ter.app.applicationtest.Contenants.Boite;
import com.ter.app.applicationtest.Contenants.ListContenant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe représentant l'état de la simulation
 */
public class Etat_simulation {
    /**
     * Enum comprennant les différents contenants de l'exercice
     */
    public enum Contenants {RESERVE, TABLE, BOITE0, BOITE1, BOITE2, BOITE3, ZONE_GROUPEMENT, ZONE_DUPLIQUER}

    /**
     * Enum comprennant les différents objets de l'exercice
     */
    public enum Objets {BUCHETTE, GROUPEMENT10, GROUPEMENT100, GROUPEMENT100B}


    // etat_courant[0][..] -> reserve
    // etat_courant[1][..] -> table
    // etat_courant[2][..] -> boite0
    // etat_courant[3][..] -> boite1
    // etat_courant[4][..] -> boite2
    // etat_courant[5][..] -> boite3
    // etat_courant[6][..] -> zone_groupement
    // etat_courant[..][0] -> nb_buchettes
    // etat_courant[..][1] -> nb_groupement10
    // etat_courant[..][2] -> nb_groupement100
    // etat_courant[..][3] -> nb_groupement100b
    public static int[][] etat_courant = new int[Contenants.values().length][Objets.values().length];


    /**
     * Méthode permettant de récupérer l'état de la simulation sous la forme d'un String
     * @return un String correspondant à l'état de la simulation
     */
    public static String etatSimulation(){
        String s = "(";
        s += "(reserve," + etat_courant[Contenants.RESERVE.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.RESERVE.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.RESERVE.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.RESERVE.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(table," + etat_courant[Contenants.TABLE.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.TABLE.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.TABLE.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.TABLE.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(boite0," + etat_courant[Contenants.BOITE0.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.BOITE0.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.BOITE0.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.BOITE0.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(boite1," + etat_courant[Contenants.BOITE1.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.BOITE1.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.BOITE1.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.BOITE1.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(boite2," + etat_courant[Contenants.BOITE2.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.BOITE2.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.BOITE2.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.BOITE2.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(boite3," + etat_courant[Contenants.BOITE3.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.BOITE3.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.BOITE3.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.BOITE3.ordinal()][Objets.GROUPEMENT100B.ordinal()] +"),";
        s += "(zone_groupement," + etat_courant[Contenants.ZONE_GROUPEMENT.ordinal()][Objets.BUCHETTE.ordinal()] +","  + etat_courant[Contenants.ZONE_GROUPEMENT.ordinal()][Objets.GROUPEMENT10.ordinal()] + "," +  etat_courant[Contenants.ZONE_GROUPEMENT.ordinal()][Objets.GROUPEMENT100.ordinal()] + "," + etat_courant[Contenants.ZONE_GROUPEMENT.ordinal()][Objets.GROUPEMENT100B.ordinal()] +")";
        s+= ")";
        return s;
    }


    /**
     * Méthode permettant d'ajouter un objet dans un contenant
     * @param o l'objet que l'on veut ajouter
     * @param c le contenant dans lequel on veut ajouter l'objet
     */
    public static void ajouter(Objets o, Contenants c ){
        etat_courant[c.ordinal()][o.ordinal()]++;

    }

    /**
     * Méthode permettant d'enlever un objet d'un contenant
     * @param o l'objet que l'on veut enlever
     * @param c le contenant duquel on veut enlever l'objet
     */
    public static void enlever(Objets o, Contenants c ){
        etatSimulation();
        etat_courant[c.ordinal()][o.ordinal()]--;
    }

    /**
     * Méthode permettant de récupérer le nombre d'un objet dans un contenant
     * @param o l'objet dont on veut connaitre le nombre
     * @param c le contenant
     * @return le nombre de l'objet dans le contenant
     */
    public static int count(Objets o, Contenants c ){
       return etat_courant[c.ordinal()][o.ordinal()];
    }

    /**
     * Méthode permettant d'initialiser l'état de simulation
     */
    public static void init(){
        for(int i = 0; i<Contenants.values().length; i++){
            for(int j = 0; j<Objets.values().length; j++){
                etat_courant[i][j] = 0;
            }
        }
    }

    /**
     * Méthode permettant de récupérer le type d'objet que représente une view
     * @param v la view dont on veut connaitre l'objet qu'elle représente
     * @return l'objet que représente la view
     */
    public static Objets viewToObjet(View v){
        Objets o = Objets.BUCHETTE;
        if(v.getContentDescription().equals("buchette")) o = Objets.BUCHETTE;
        if(v.getContentDescription().equals("groupement10")) o = Objets.GROUPEMENT10;
        if(v.getContentDescription().equals("groupement100")) o = Objets.GROUPEMENT100;
        if(v.getContentDescription().equals("groupement100b")) o = Objets.GROUPEMENT100B;
        return o;
    }

    /**
     * Méthode permettant de récupérer le type d'objet que représente une String
     * @param s la String dont on veut connaitre l'objet qu'elle représente
     * @return l'objet que représente la String
     */
    public static Objets stringToObjet(String s){
        Objets o = Objets.BUCHETTE;
        if(s.equals("buchette")) o = Objets.BUCHETTE;
        if(s.equals("groupement10")) o = Objets.GROUPEMENT10;
        if(s.equals("groupement100")) o = Objets.GROUPEMENT100;
        if(s.equals("groupement100b")) o = Objets.GROUPEMENT100B;
        return o;
    }

    /**
     * Méthode permettant de récupérer le type de contenant que représente un RelativeLayout
     * @param r le RelativeLayout dont on veut connaitre le contenant qu'il représente
     * @return le contenant que représente le RelativeLayout
     */
    public static Contenants relativeLayoutToContenant(RelativeLayout r){
        Contenants c = Contenants.RESERVE;
        if(Boite.getNomStatic(r).equals(ListContenant.boite0.getNom())) c = Contenants.BOITE0;
        if(Boite.getNomStatic(r).equals(ListContenant.boite1.getNom())) c = Contenants.BOITE1;
        if(Boite.getNomStatic(r).equals(ListContenant.boite2.getNom())) c = Contenants.BOITE2;
        if(Boite.getNomStatic(r).equals(ListContenant.boite3.getNom())) c = Contenants.BOITE3;
        if(Boite.getNomStatic(r).equals(ListContenant.table.getNom())) c = Contenants.TABLE;
        if(Boite.getNomStatic(r).equals(ListContenant.reserveBuchette.getNom())) c = Contenants.RESERVE;
        if(Boite.getNomStatic(r).equals(ListContenant.zoneGroupement.getNom())) c = Contenants.ZONE_GROUPEMENT;
        if(Boite.getNomStatic(r).equals(ListContenant.zoneDupliquer.getNom())) c = Contenants.ZONE_DUPLIQUER;
        return c;
    }

    /**
     * Méthode permettant de récupérer le type de contenant que représente une String
     * @param s la String dont on veut connaitre le contenant qu'elle représente
     * @return le contenant que représente la String
     */
    public static Contenants stringToContenant(String s){
        Contenants c = Contenants.RESERVE;
        if(s.equals(ListContenant.boite0.getNom())) c = Contenants.BOITE0;
        if(s.equals(ListContenant.boite1.getNom())) c = Contenants.BOITE1;
        if(s.equals(ListContenant.boite2.getNom())) c = Contenants.BOITE2;
        if(s.equals(ListContenant.boite3.getNom())) c = Contenants.BOITE3;
        if(s.equals(ListContenant.table.getNom())) c = Contenants.TABLE;
        if(s.equals(ListContenant.reserveBuchette.getNom())) c = Contenants.RESERVE;
        if(ListContenant.zoneGroupement != null) if(s.equals(ListContenant.zoneGroupement.getNom())) c = Contenants.ZONE_GROUPEMENT;
        if(ListContenant.zoneDupliquer != null) if(s.equals(ListContenant.zoneDupliquer.getNom())) c = Contenants.ZONE_DUPLIQUER;
        return c;
    }


    /**
     * Méthode permettant de récupérer l'état de la simulation sous la forme d'un JSONObject
     * @return un JSONObject correspondant à l'état de la simulation
     */
    public static JSONObject etat_Simulation_JSON(){
        // on créer un objet global, représentant l'état de la simulation
        JSONObject etat_simulation = new JSONObject();
        try {
            // on ajoute un attribut "idEleve" ayant pour valeur "Traces.id"
            etat_simulation.put("idEleve", Traces.id);
            // on ajoute un attribut "code_exercice" ayant pour valeur "Traces.nomExercice"
            etat_simulation.put("code_exercice", Traces.nomExercice);
            etat_simulation.put("contentType", "EXERCICE_EN_COURS");
            // on créer un JSONArray permettant de regrouper les contenants
            JSONArray contenantsArray = new JSONArray();

            // on créer un JSONObject représentant la réserve
            JSONObject Reserve = new JSONObject();
            // on lui ajoute un attribut "nom", ayant pour valeur "Reserve" afin d'identifier le contenant
            Reserve.put("nom", "Reserve");
            // on ajoute des attributs pour le nombre des éléments, avec leur valeurs
            Reserve.put("nb_buchette", etat_courant[Etat_simulation.Contenants.RESERVE.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Reserve.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.RESERVE.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Reserve.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.RESERVE.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Reserve.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.RESERVE.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            // on ajoute la réserve à la liste des contenants
            contenantsArray.put(Reserve);

            // on créer un JSONObject représentant la Table
            JSONObject Table = new JSONObject();
            // on lui ajoute un attribut "nom", ayant pour valeur "Table" afin d'identifier le contenant
            Table.put("nom", "Table");
            // on ajoute des attributs pour le nombre des éléments, avec leur valeurs
            Table.put("nb_buchette", etat_courant[Etat_simulation.Contenants.TABLE.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Table.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.TABLE.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Table.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.TABLE.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Table.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.TABLE.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            // on ajoute la réserve à la liste des contenants
            contenantsArray.put(Table);

            // même principe que précédemment
            JSONObject Boite1 = new JSONObject();
            Boite1.put("nom", "Boite1");
            Boite1.put("nb_buchette", etat_courant[Etat_simulation.Contenants.BOITE0.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Boite1.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.BOITE0.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Boite1.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.BOITE0.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Boite1.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.BOITE0.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            contenantsArray.put(Boite1);

            // même principe que précédemment
            JSONObject Boite2 = new JSONObject();
            Boite2.put("nom", "Boite2");
            Boite2.put("nb_buchette", etat_courant[Etat_simulation.Contenants.BOITE1.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Boite2.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.BOITE1.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Boite2.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.BOITE1.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Boite2.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.BOITE1.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            contenantsArray.put(Boite2);

            // même principe que précédemment
            JSONObject Boite3 = new JSONObject();
            Boite3.put("nom", "Boite3");
            Boite3.put("nb_buchette", etat_courant[Etat_simulation.Contenants.BOITE2.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Boite3.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.BOITE2.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Boite3.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.BOITE2.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Boite3.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.BOITE2.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            contenantsArray.put(Boite3);

            // même principe que précédemment
            JSONObject Boite4 = new JSONObject();
            Boite4.put("nom", "Boite4");
            Boite4.put("nb_buchette", etat_courant[Etat_simulation.Contenants.BOITE3.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Boite4.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.BOITE3.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Boite4.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.BOITE3.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Boite4.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.BOITE3.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            contenantsArray.put(Boite4);

            // même principe que précédemment
            JSONObject Zone_Groupement = new JSONObject();
            Zone_Groupement.put("nom", "Zone_Groupement");
            Zone_Groupement.put("nb_buchette", etat_courant[Contenants.ZONE_GROUPEMENT.ordinal()][Etat_simulation.Objets.BUCHETTE.ordinal()]);
            Zone_Groupement.put("nb_groupement_10", etat_courant[Etat_simulation.Contenants.ZONE_GROUPEMENT.ordinal()][Etat_simulation.Objets.GROUPEMENT10.ordinal()]);
            Zone_Groupement.put("nb_groupement_100", etat_courant[Etat_simulation.Contenants.ZONE_GROUPEMENT.ordinal()][Etat_simulation.Objets.GROUPEMENT100.ordinal()]);
            Zone_Groupement.put("nb_groupement_100b", etat_courant[Etat_simulation.Contenants.ZONE_GROUPEMENT.ordinal()][Etat_simulation.Objets.GROUPEMENT100B.ordinal()]);
            contenantsArray.put(Zone_Groupement);

            // on ajoute le tableau des contenants à l'objet représentant l'état de la simulation
            etat_simulation.put("contenants", contenantsArray);
            // permet de savoir dans la console que l'objet à bien été crée, sous le bon format
            Log.d("JSON LOG",etat_simulation.toString());

        }catch (JSONException e){
            e.printStackTrace();
        }
        return etat_simulation;
    }
}
