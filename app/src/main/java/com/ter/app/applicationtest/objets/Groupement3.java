package com.ter.app.applicationtest.objets;

import android.content.Context;

import com.ter.app.applicationtest.parametresExercices.Config;

/**
 * Classe représentant un groupement 3
 */
public class Groupement3 extends Objet {

    /**
     * Constructeur d'un groupement3
     * @param c contexte de l'activité associée au groupement3
     */
    public Groupement3(Context c){
        // on créer l'ImageView associée à la buchette
        super(c, Config.id_image_groupement3, "groupement3");
    }
}

