package com.ter.app.applicationtest.Contenants;


import android.view.View;
import android.widget.RelativeLayout;


/**
 * Classe correspondant à la réserve.
 */
public class Reserve {
    /**
     * RelativeLayout dans le xml, correspondant à la réserve de buchettes.
     */
    public RelativeLayout reserve;


    /**
     * Constructeur de la réserve de buchette
     * @param r le RelativeLayout dans le xml, correspondant à la réserve
     */
    public Reserve(RelativeLayout r){

        reserve = r;
        reserve.setContentDescription("reserve; ");
    }


    /**
     * Métode permettant d'ajouter un enfant dans le RelativeLayout correspondant à la réserve
     * @param child l'enfant à ajouter
     */
    public void addView(View child){
        reserve.addView(child);
    }

    public String getNom() {
        return "reserve";
    }
}
