package com.ter.app.applicationtest.feedbacks;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Classe représentant l'affichage machine d'une boite, c'est à dire le nombre d'élément (selon leut type) qu'elle contient
 */
public class AffichageMachine {
    /**
     * Le RelativeLayout regroupant les affichages
     */
    RelativeLayout affichage;
    /**
     * La zone de texte pour l'affichage du nombre de buchettes
     */
    TextView nb_buchette;
    /**
     * La zone de texte pour l'affichage du nombre de groupements de 10
     */
    TextView nb_groupement10;
    /**
     * La zone de texte pour l'affichage du nombre de groupements de 100
     */
    TextView nb_groupement100;
    /**
     * La zone de texte pour l'affichage du nombre de groupements de 100b
     */
    TextView nb_groupement100b;


    /**
     * Constructeur de l'affichage machine d'une boite
     * @param relativeLayout Le RelativeLayout regroupant les affichages
     * @param nb_buchette La zone de texte pour l'affichage du nombre de buchettes
     * @param nb_groupement10 La zone de texte pour l'affichage du nombre de groupements de 10
     * @param nb_groupement100 La zone de texte pour l'affichage du nombre de groupements de 100
     * @param nb_groupement100b La zone de texte pour l'affichage du nombre de groupements de 100b
     */
    public AffichageMachine(RelativeLayout relativeLayout, TextView nb_buchette, TextView nb_groupement10, TextView nb_groupement100, TextView nb_groupement100b){
        affichage = relativeLayout;
        this.nb_buchette = nb_buchette;
        this.nb_groupement10 = nb_groupement10;
        this.nb_groupement100 = nb_groupement100;
        this.nb_groupement100b = nb_groupement100b;
    }

    /**
     * Méthode permettant de mettre à jour l'affichage du nombre de buchettes dans la boite
     * @param nb le nomnbre de buchettes dans la boite
     */
    public void mettreAJourAffichageBuchette(int nb){
        nb_buchette.setText("" + nb);
    }

    /**
     * Méthode permettant de mettre à jour l'affichage du nombre de groupements de 10 dans la boite
     * @param nb le nomnbre de groupements de 10 dans la boite
     */
    public void mettreAJourAffichageGroupement10(int nb){
        nb_groupement10.setText("" + nb);
    }

    /**
     * Méthode permettant de mettre à jour l'affichage du nombre de groupements de 100 dans la boite
     * @param nb le nomnbre de groupements de 100 dans la boite
     */
    public void mettreAJourAffichageGroupement100(int nb){
        nb_groupement100.setText("" + nb);
    }

    /**
     * Méthode permettant de mettre à jour l'affichage du nombre de groupements de 100b dans la boite
     * @param nb le nomnbre de groupements de 100b dans la boite
     */
    public void mettreAJourAffichageGroupement100b(int nb){
        nb_groupement100b.setText("" + nb);
    }

    /**
     * Méthode permettant d'afficher ou non l'affichage machine
     * @param visible true si l'on veut afficher l'affichage machine, false sinon
     */
    public void setVisibility(boolean visible){
        if(visible) affichage.setVisibility(View.VISIBLE);
        else affichage.setVisibility(View.INVISIBLE);
    }

}
