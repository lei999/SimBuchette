package com.ter.app.applicationtest.Contenants;

import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Classe représentant tous les contenants de l'exercice
 */
public class ListContenant {

    /**
     * La Table de l'exercice
     */
    public static Table table;

    /**
     * La Boite 0 de l'exercice
     */
    public static Boite boite0;
    /**
     * La Boite 1 de l'exercice
     */
    public static Boite boite1;

    /**
     * La Boite 2 de l'exercice
     */
    public static Boite boite2;

    /**
     * La Boite 3 de l'exercice
     */
    public static Boite boite3;

    /**
     * La Reserve de buchettes de l'exercice
     */
    public static Reserve reserveBuchette;

    /**
     * La Reserve de groupements de l'exercice
     */
    public static Reserve reserveGroupement;

    /**
     * La Zone de Groupement de l'exercice
     */
    public static ZoneGroupement zoneGroupement;

    /**
     * La Zone Dupliquer de l'exercice
     */
    public static ZoneDupliquer zoneDupliquer;


    /**
     * Méthode permettant de récupérer les contenants utilisés pendant l'exercice
     * @return les contenants utilisés pendant l'exercice
     */
    public static ArrayList<Contenant> getLisContenant(){
        ArrayList<Contenant> c =  new ArrayList<>();
        c.add(table);
        c.add(boite0);
        c.add(boite1);
        c.add(boite2);
        c.add(boite3);
        if(zoneGroupement != null) c.add(zoneGroupement);
        if(zoneDupliquer != null) c.add(zoneDupliquer);
        return c;
    }

    /**
     * Méthode permettant de récupérer la boite associée à un RelativeLayout
     * @param r le RelativeLayout dont on veut connaitre la boite associé
     * @return la boite associée au RelativeLayout
     */
    public static Boite getBoite(RelativeLayout r){
        Boite c = null;
        if(r.equals(boite0.getRelativeLayout())) c = boite0;
        if(r.equals(boite1.getRelativeLayout())) c = boite1;
        if(r.equals(boite2.getRelativeLayout())) c = boite2;
        if(r.equals(boite3.getRelativeLayout())) c = boite3;
        return c;
    }

}
