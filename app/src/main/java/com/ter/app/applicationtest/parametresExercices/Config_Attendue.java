package com.ter.app.applicationtest.parametresExercices;

import java.util.ArrayList;

/**
 * Created by myriam on 29/06/15.
 */
public class Config_Attendue {
    public enum Action {SORTIE, CONTINUER,NOUVEL_ELEVE, SORTIE_ET_MESSAGE, CONTINUER_ET_MESSAGE,NOUVEL_ELEVE_ET_MESSAGE}

    public String nom;
    public ArrayList<Config_boite> boites = new ArrayList<>();
    public boolean table_vide;
    public Action comportement_fin;
    public String message_sortie;
    public String message_continuer;
    public String message_nouvel_eleve;

    public Config_Attendue(){
        nom ="";
        Config_boite c = new Config_boite();
        boites.add(c);
        c = new Config_boite();
        boites.add(c);
        c = new Config_boite();
        boites.add(c);
        c = new Config_boite();
        boites.add(c);
    }


}
