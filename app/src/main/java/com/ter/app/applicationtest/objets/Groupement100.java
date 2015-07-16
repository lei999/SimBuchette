package com.ter.app.applicationtest.objets;

import android.content.Context;

import com.ter.app.applicationtest.parametresExercices.Config;

/**
 * Classe représentant un groupement de 100
 */
public class Groupement100 extends Objet {
    /**
     * Constructeur d'un groupement de 100
     * @param c    contexte de l'activité associée au groupement de 100
     */
    public Groupement100(Context c) {
        super(c, Config.id_image_groupement100, "groupement100");
    }
}
