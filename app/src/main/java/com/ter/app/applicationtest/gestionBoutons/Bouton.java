package com.ter.app.applicationtest.gestionBoutons;

import android.view.View;
import android.widget.Button;

/**
 * Classe Abstraite représentant un bouton de la simulation
 */
public abstract class Bouton {
    /**
     * Le Button dans le xml
     */
    protected Button bouton;

    /**
     * Le clickListener du bouton
     */
    protected View.OnClickListener clickListener;


}
