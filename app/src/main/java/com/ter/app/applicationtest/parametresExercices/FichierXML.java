package com.ter.app.applicationtest.parametresExercices;

import java.io.File;

/**
 * Created by myriam on 16/07/15.
 */
public class FichierXML {
    private String nom;
    private File fichier;

    public FichierXML(String nom, File fichier) {
        this.nom = nom;
        this.fichier = fichier;
    }

    public String getNom() {return nom;}
    public File getFichier() {return fichier;}
}
