package com.ter.app.applicationtest.feedbacks;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Classe permettant de créer le fichier de trace
 */
public class Fichier {


    /**
     * Méthode permettant d'écrire un fichier de trace
     * @param trace la trace que l'on veut écrire
     */
    public static void write(String trace) {
        Date d = new Date();
        String nameFile = "[" + d + "] " + Traces.nomEleve + "_" + Traces.nomExercice + ".csv"; // le nom du fichier

        File myFile = new File(Environment.getExternalStorageDirectory() +
                File.separator + "SimBuchette", nameFile); //on déclare notre futur fichier

        File myDir = new File(Environment.getExternalStorageDirectory() +
                File.separator + "SimBuchette"); //pour créer le repertoire dans lequel on va mettre notre fichier


        Boolean success=true;


        if (!myDir.exists()) {
            success = myDir.mkdir(); //On crée le répertoire (s'il n'existe pas!!)
        }
        if (success){
            FileOutputStream output = null; //le true est pour écrire en fin de fichier, et non l'écraser
            try {
                //output = new FileOutputStream(myFile,true);
                output = new FileOutputStream(myFile,false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                output.write(trace.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e("TEST1", "ERROR DE CREATION DE DOSSIER");}

    }

}