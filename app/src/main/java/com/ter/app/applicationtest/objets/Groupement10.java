package com.ter.app.applicationtest.objets;

import android.content.Context;

import com.ter.app.applicationtest.parametresExercices.Config;

/**
 * Classe représentant un groupement de 10
 */
public class Groupement10 extends Objet {
    /**
     * Constructeur du groupement de 10
     * @param c    contexte de l'activité associée au groupement de 10
     */
    public Groupement10(Context c) {
        super(c, Config.id_image_groupement10, "groupement10");
    }
}
