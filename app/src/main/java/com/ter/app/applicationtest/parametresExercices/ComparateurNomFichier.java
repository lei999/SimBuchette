package com.ter.app.applicationtest.parametresExercices;

import java.io.File;
import java.util.Comparator;

/**
 * Created by myriam on 16/07/15.
 */
public class ComparateurNomFichier implements Comparator<FichierXML> {

    public ComparateurNomFichier(){}


    @Override
    public int compare(FichierXML lhs, FichierXML rhs) {
        return lhs.getNom().compareTo(rhs.getNom());
    }
}
