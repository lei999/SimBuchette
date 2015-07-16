package com.ter.app.applicationtest.objets;

import android.view.View;

import com.ter.app.applicationtest.feedbacks.Etat_simulation;

import java.util.ArrayList;

/**
 * Classe permettant de gérer la liste d'objets sélectionnés dans une boite
 */
public class ListObjets {
    /**
     * La liste des objets sélectionnés
     */
    public static ArrayList<View> listView = new ArrayList<>();

    /**
     * Méthode permettant de réinitialiser la liste des objets sélectionés
     */
    public static void removeAll(){
        for(int i = listView.size() -1 ; i >= 0; i--){
            listView.remove(i);
        }
    }

    public static ArrayList<Etat_simulation.Objets> typeObjets(){
        ArrayList<Etat_simulation.Objets> s = new ArrayList<>();
        for(int i = 0; i< listView.size(); i++){
            if(listView.get(i).getContentDescription().toString().equals("buchette")) s.add(Etat_simulation.Objets.BUCHETTE);
            if(listView.get(i).getContentDescription().toString().equals("groupement10")) s.add(Etat_simulation.Objets.GROUPEMENT10);
            if(listView.get(i).getContentDescription().toString().equals("groupement100")) s.add(Etat_simulation.Objets.GROUPEMENT100);
            if(listView.get(i).getContentDescription().toString().equals("groupement100b")) s.add(Etat_simulation.Objets.GROUPEMENT100B);
        }
        return s;
    }


}
