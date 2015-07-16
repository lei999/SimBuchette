package com.ter.app.applicationtest.objets;

import android.content.Context;

import com.ter.app.applicationtest.parametresExercices.Config;

/**
 * Classe représentant une buchette
 */
public class Buchette extends Objet{


    /********       CONSTRUCTEUR        ****************/
    /**
     * Constructeur d'une buchette
     * @param c contexte de l'activité associée à la buchette
     */
    public Buchette(Context c){
        super(c, Config.id_image_buchette, "buchette");
    }
}
