package com.ter.app.applicationtest.objets;

import android.content.Context;

import com.ter.app.applicationtest.parametresExercices.Config;

/**
 * Classe représentant un groupement de 100 fait à partir de Buchettes
 */
public class Groupement100B extends Objet{
    /**
     * Constructeur d'un groupement de 100 fait à partir de buchettes
     * @param c    contexte de l'activité associée au groupement de 100
     */
    public Groupement100B(Context c) {
        super(c, Config.id_image_groupement100b, "groupement100b");
    }
}
