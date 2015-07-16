package com.ter.app.applicationtest.Parser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Log;

import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.feedbacks.Alert;
import com.ter.app.applicationtest.gestionBoutons.BoutonTerminer;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.parametresExercices.Config_Attendue;
import com.ter.app.applicationtest.parametresExercices.Consigne;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.PublicKey;
import java.util.logging.Handler;

/**
 * Classe permettant de parser un fichier XML de configuration de la simulation
 */
public class Parser {

    XmlPullParser parser;

  //  public static Context context;

    /**
     * Constructeur du parseur
     * @param c contexte associé au parser
     * @param choix choix du scénario de l'exercice (numéro de l'exercice que l'on veut lancer)
     */
    public Parser(Context c, int choix) {

        try {
            // permet de signifier que l'on veut parser le fichier XML spécifique
            if (choix == 1 || choix == 2 || choix == 11){
                if (choix == 1 ) parser = c.getResources().getXml(R.xml.c1_1);
                if (choix == 2) parser = c.getResources().getXml(R.xml.c1_2);
                if (choix == 11) parser = c.getResources().getXml(R.xml.s3);

                parseXML2(parser);
            }
            else{

                if (choix == 3) parser = c.getResources().getXml(R.xml.c2_1);
                if (choix == 4) parser = c.getResources().getXml(R.xml.c2_2);
                if (choix == 5) parser = c.getResources().getXml(R.xml.c2_3);
                if (choix == 6) parser = c.getResources().getXml(R.xml.c2_4);
                if (choix == 7) parser = c.getResources().getXml(R.xml.c2_5);
                if (choix == 8) parser = c.getResources().getXml(R.xml.c2_6);
                if (choix == 9) parser = c.getResources().getXml(R.xml.s1);
                if (choix == 10) parser = c.getResources().getXml(R.xml.s2);

                // parse le fichier XML
                parseXML(parser);

            }

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    public Parser(File file){
        /**********************************************/
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();
            // create an input stream to be read by the stream reader.
            FileInputStream fis = new FileInputStream(file);
            // set the input for the parser using an InputStreamReader
            parser.setInput(new InputStreamReader(fis));
            parseXML2(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e ){

        } catch (IOException e) {
            e.printStackTrace();
        }

        /***************************************/

    }


    public Parser(Context c, String exercice) {

        try {

            // permet de signifier que l'on veut parser le fichier XML spécifique
            if (exercice.equals("c1_1")) parser = c.getResources().getXml(R.xml.c1_1);
            if (exercice.equals("c1_2")) parser = c.getResources().getXml(R.xml.c1_2);
            if (exercice.equals("s3")) parser = c.getResources().getXml(R.xml.s3);
            if (exercice.equals("t1")) parser = c.getResources().getXml(R.xml.t1);
            if (exercice.equals("t2")) parser = c.getResources().getXml(R.xml.t2);
            if (exercice.equals("t3")) parser = c.getResources().getXml(R.xml.t3);
            if (exercice.equals("t4")) parser = c.getResources().getXml(R.xml.t4);
            if (exercice.equals("t5")) parser = c.getResources().getXml(R.xml.t5);
            if (exercice.equals("t6")) parser = c.getResources().getXml(R.xml.t6);
            if (exercice.equals("t7")) parser = c.getResources().getXml(R.xml.t7);
            if (exercice.equals("t8")) parser = c.getResources().getXml(R.xml.t8);
            if (exercice.equals("t9")) parser = c.getResources().getXml(R.xml.t9);
            if (exercice.equals("t10")) parser = c.getResources().getXml(R.xml.t10);
            parseXML2(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }


    /**
     * Méthode permettant de parser un fichier XML de configuration d'exercice
     * @param parser le parser paramétré avec le fichier XML voulut
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Integer numBoite = null;
        int eventType = parser.getEventType();
        Integer nbmax = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    if(parser.getName().equals("fenetre")) {
                        parser.nextTag();

                        if(parser.getName().equals("taille")){
                            String taille = parser.nextText();
                            if(taille.equals("G")){
                                Config.id_image_buchette = R.drawable.buchette_g;
                                Config.id_image_groupement3 = R.drawable.groupement3;
                                Config.id_image_groupement10 = R.drawable.groupement10;
                                Config.id_image_groupement100 = R.drawable.groupement100;
                                Config.id_image_groupement100b = R.drawable.groupement100b_g;
                                Config.taille = 1;
                            }
                            if(taille.equals("M")){
                                Config.id_image_buchette = R.drawable.buchette_m;
                                Config.id_image_groupement3 = R.drawable.groupement3_m;
                                Config.id_image_groupement10 = R.drawable.groupement10_m;
                                Config.id_image_groupement100 = R.drawable.groupement100_m;
                                Config.id_image_groupement100b = R.drawable.groupement100b_m;
                                Config.taille = 2;
                            }
                            if(taille.equals("P")){
                                Config.id_image_buchette = R.drawable.buchette_p;
                                Config.id_image_groupement3 = R.drawable.groupement3_p;
                                Config.id_image_groupement10 = R.drawable.groupement10_p;
                                Config.id_image_groupement100 = R.drawable.groupement100_m;
                                Config.id_image_groupement100b = R.drawable.groupement100b_p;
                                Config.taille = 3;
                            }
                            parser.nextTag();
                        }

                        if(parser.getName().equals("nb_buchettes")){
                            Config.nb_buchettes = new Integer(parser.nextText());
                            parser.nextTag();
                        }

                        if(parser.getName().equals("nb_groupement3")){
                            Config.nb_groupement3 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement10")){
                            Config.nb_groupement10 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement100")){
                            Config.nb_groupement100 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement100b")){
                            Config.nb_groupement100b = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("utilisation_zone_groupements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.utilisation_zone_groupements = true;
                            if(utilisation.equals("non")) Config.utilisation_zone_groupements = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("exercice_complement")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.exercice_complement = true;
                            if(utilisation.equals("non")) Config.exercice_complement = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("utilisation_dupliquer")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.utilisation_dupliquer = true;
                            if(utilisation.equals("non")) Config.utilisation_dupliquer = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("groupement3_possible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.groupement3_possible = true;
                            if(utilisation.equals("non")) Config.groupement3_possible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("groupement10_possible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.groupement10_possible = true;
                            if(utilisation.equals("non")) Config.groupement10_possible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("groupement100_possible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.groupement100_possible = true;
                            if(utilisation.equals("non")) Config.groupement100_possible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("groupement_simple")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.groupement_simple = true;
                            if(utilisation.equals("non")) Config.groupement_simple = false;
                            parser.nextTag();
                        }
                    }

                    if(parser.getName().equals("boutons")) {
                        parser.nextTag();
                        if(parser.getName().equals("vider")){
                            Config.vider = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("annuler")){
                            Config.annuler = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("terminer")){
                            Config.terminer = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("reset")){
                            Config.reset = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                    }


                    if(parser.getName().equals("boite")){
                        parser.nextTag();
                        if(parser.getName().equals("num_boite")){
                            numBoite = new Integer(parser.nextText()) - 1;
                            if(numBoite == 0 ) Config.boite0_visible = true;
                            if(numBoite == 1 ) Config.boite1_visible = true;
                            if(numBoite == 2 ) Config.boite2_visible = true;
                            if(numBoite == 3 ) Config.boite3_visible = true;

                            parser.nextTag();

                        }
                        if(parser.getName().equals("nom")){
                            String nom = parser.nextText();
                            if(numBoite == 0 ) Config.boite0_nom = nom;
                            if(numBoite == 1 ) Config.boite1_nom = nom;
                            if(numBoite == 2 ) Config.boite2_nom = nom;
                            if(numBoite == 3 ) Config.boite3_nom = nom;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_max_objets")){
                            nbmax = new Integer(parser.nextText());
                            if(numBoite == 0 ) Config.boite0_max_objets = nbmax;
                            if(numBoite == 1 ) Config.boite1_max_objets = nbmax;
                            if(numBoite == 2 ) Config.boite2_max_objets = nbmax;
                            if(numBoite == 3 ) Config.boite3_max_objets = nbmax;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("type_objet")){
                            String typeObjet = parser.nextText();

                            if(typeObjet.equals("buchette")){
                                if(numBoite == 0 ) Config.boite0_typeObjet += "buchette,";
                                if(numBoite == 1 ) Config.boite1_typeObjet += "buchette,";
                                if(numBoite == 2 ) Config.boite2_typeObjet += "buchette,";
                                if(numBoite == 3 ) Config.boite3_typeObjet += "buchette,";
                            }
                            if(typeObjet.equals("groupement3")){
                                if(numBoite == 0 ) Config.boite0_typeObjet += "groupement3,";
                                if(numBoite == 1 ) Config.boite1_typeObjet += "groupement3,";
                                if(numBoite == 2 ) Config.boite2_typeObjet += "groupement3,";
                                if(numBoite == 3 ) Config.boite3_typeObjet += "groupement3,";
                            }
                            if(typeObjet.equals("groupement10")){
                                if(numBoite == 0 ) Config.boite0_typeObjet += "groupement10,";
                                if(numBoite == 1 ) Config.boite1_typeObjet += "groupement10,";
                                if(numBoite == 2 ) Config.boite2_typeObjet += "groupement10,";
                                if(numBoite == 3 ) Config.boite3_typeObjet += "groupement10,";
                            }
                            if(typeObjet.equals("groupement100")){
                                if(numBoite == 0 ) Config.boite0_typeObjet += "groupement100,";
                                if(numBoite == 1 ) Config.boite1_typeObjet += "groupement100,";
                                if(numBoite == 2 ) Config.boite2_typeObjet += "groupement100,";
                                if(numBoite == 3 ) Config.boite3_typeObjet += "groupement100,";
                            }
                            parser.nextTag();
                        }
                        if (parser.getName().equals("different_objet")){
                            if(numBoite == 0 ) Config.boite0_differentObjets = true;
                            if(numBoite == 1 ) Config.boite1_differentObjets = true;
                            if(numBoite == 2 ) Config.boite2_differentObjets = true;
                            if(numBoite == 3 ) Config.boite3_differentObjets = true;
                            parser.nextText();
                            parser.nextTag();
                        }

                        if (parser.getName().equals("affichage_machine")) {
                            if (numBoite == 0) Config.boite0_affichage_machine = true;
                            if (numBoite == 1) Config.boite1_affichage_machine = true;
                            if (numBoite == 2) Config.boite2_affichage_machine = true;
                            if (numBoite == 3) Config.boite3_affichage_machine = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                        if (parser.getName().equals("popup")){
                            if(numBoite == 0 ) Config.boite0_popup = true;
                            if(numBoite == 1 ) Config.boite1_popup = true;
                            if(numBoite == 2 ) Config.boite2_popup = true;
                            if(numBoite == 3 ) Config.boite3_popup = true;
                            parser.nextText();
                            parser.nextTag();
                        }
                    }
                    if(parser.getName().equals("consigne")){
                        String consigne = parser.nextText() ;
                        Config.consigne = consigne;
                    }
                    if(parser.getName().equals("solution")){
                        parser.nextTag();
                        if(parser.getName().equals("boite")) {
                            parser.nextTag();
                            if (parser.getName().equals("num_boite")) {
                                numBoite = new Integer(parser.nextText()) - 1;
                                parser.nextTag();
                            }
                            if(parser.getName().equals("type_objet")){
                                String typeObjet = parser.nextText();

                                if(typeObjet.equals("buchette")){
                                    if(numBoite == 0 ) Config.solution_boite0_type_objet = "buchette";
                                    if(numBoite == 1 ) Config.solution_boite1_type_objet = "buchette";
                                    if(numBoite == 2 ) Config.solution_boite2_type_objet = "buchette";
                                    if(numBoite == 3 ) Config.solution_boite3_type_objet = "buchette";
                                }
                                if(typeObjet.equals("groupement3")){
                                    if(numBoite == 0 ) Config.solution_boite0_type_objet = "groupement3";
                                    if(numBoite == 1 ) Config.solution_boite1_type_objet = "groupement3";
                                    if(numBoite == 2 ) Config.solution_boite2_type_objet = "groupement3";
                                    if(numBoite == 3 ) Config.solution_boite3_type_objet = "groupement3";
                                }
                                if(typeObjet.equals("groupement10")){
                                    if(numBoite == 0 ) Config.solution_boite0_type_objet = "groupement10";
                                    if(numBoite == 1 ) Config.solution_boite1_type_objet = "groupement10";
                                    if(numBoite == 2 ) Config.solution_boite2_type_objet = "groupement10";
                                    if(numBoite == 3 ) Config.solution_boite3_type_objet = "groupement10";
                                }
                                if(typeObjet.equals("groupement100")){
                                    if(numBoite == 0 ) Config.solution_boite0_type_objet = "groupement100";
                                    if(numBoite == 1 ) Config.solution_boite1_type_objet = "groupement100";
                                    if(numBoite == 2 ) Config.solution_boite2_type_objet = "groupement100";
                                    if(numBoite == 3 ) Config.solution_boite3_type_objet = "groupement100";
                                }
                                parser.nextTag();
                            }
                            if(parser.getName().equals("nb_objets")){
                                nbmax = new Integer(parser.nextText());
                                if(numBoite == 0 ) Config.solution_boite0_nb_objet = nbmax;
                                if(numBoite == 1 ) Config.solution_boite1_nb_objet = nbmax;
                                if(numBoite == 2 ) Config.solution_boite2_nb_objet = nbmax;
                                if(numBoite == 3 ) Config.solution_boite3_nb_objet = nbmax;
                                parser.nextTag();
                            }



                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.nextTag();
        }

    }



    /**
     * Méthode permettant de parser un fichier XML de configuration d'exercice
     * @param parser le parser paramétré avec le fichier XML voulut
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseXML2(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    /*      TAILLE   OBJETS        */
                    Log.d("XML name", parser.getName());
                    if(parser.getName().equals("taille_objets")) {
                        parser.nextTag();
                        String s = "" + (parser == null);
                        Log.d("XML null?", s);
                        Log.d("XML name", parser.getName());
                        if(parser.getName().equals("taille_buchettes")){
                            String taille = parser.nextText();
                            if(taille.equals("G")){
                                Config.id_image_buchette = R.drawable.buchette_g;
                                Config.id_image_groupement3 = R.drawable.groupement3;
                                Config.id_image_groupement10 = R.drawable.groupement10;
                                Config.id_image_groupement100 = R.drawable.groupement100;
                                Config.id_image_groupement100b = R.drawable.groupement100b_g;
                                Config.taille = 1;
                            }
                            if(taille.equals("M")){
                                Config.id_image_buchette = R.drawable.buchette_m;
                                Config.id_image_groupement3 = R.drawable.groupement3_m;
                                Config.id_image_groupement10 = R.drawable.groupement10_m;
                                Config.id_image_groupement100 = R.drawable.groupement100_m;
                                Config.id_image_groupement100b = R.drawable.groupement100b_m;
                                Config.taille = 2;
                            }
                            if(taille.equals("P")){
                                Config.id_image_buchette = R.drawable.buchette_p;
                                Config.id_image_groupement3 = R.drawable.groupement3_p;
                                Config.id_image_groupement10 = R.drawable.groupement10_p;
                                Config.id_image_groupement100 = R.drawable.groupement100_p;
                                Config.id_image_groupement100b = R.drawable.groupement100b_p;
                                Config.taille = 3;
                            }
                            parser.nextTag();
                        }
                        if(parser.getName().equals("taille_boites")){
                            String taille = parser.nextText();

                            if(taille.equals("G")){
                                Config.exercice_grandes_boites = true;
                                Config.exercice_complement = true;
                            }
                            if(taille.equals("M")){
                                Config.exercice_grandes_boites = false;
                                Config.exercice_complement = false;
                            }
                            parser.nextTag();
                        }
                    }



                    /*   ELEMENTS    PRÉSENTS    DANS    INTERFACE     */
                    if(parser.getName().equals("elements_presents_dans_interface")) {
                        parser.nextTag();
                        if(parser.getName().equals("zone_groupements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.utilisation_zone_groupements = true;
                            if(utilisation.equals("non")) Config.utilisation_zone_groupements = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("zone_duplication")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.utilisation_dupliquer = true;
                            if(utilisation.equals("non")) Config.utilisation_dupliquer = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("poubelle")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.utilisation_poubelle = true;
                            if(utilisation.equals("non")) Config.utilisation_poubelle = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("bouton_fin")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("Suivant")) Config.action_bouton_terminer = BoutonTerminer.Action.SUIVANT;
                            if(utilisation.equals("Terminer")) Config.action_bouton_terminer = BoutonTerminer.Action.TERMINER;
                            parser.nextTag();
                        }
                    }


                    /*   CONTRAINTES    CARDINALITÉ  RÉSERVE     */
                    if(parser.getName().equals("contraintes_cardinalite_reserve")) {
                        parser.nextTag();
                        if(parser.getName().equals("nb_buchettes_depart")){
                            Config.nb_buchettes = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement3_depart")){
                            Config.nb_groupement3 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement10_depart")){
                            Config.nb_groupement10 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement100_unites_depart")){
                            Config.nb_groupement100b = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nb_groupement_100_groupement_10_depart")){
                            Config.nb_groupement100 = new Integer(parser.nextText());
                            parser.nextTag();
                        }
                    }


                     /*   BOITE   1    */
                    if(parser.getName().equals("boite_1")) {
                        parser.nextTag();
                        if(parser.getName().equals("visible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite0_visible = true;
                            if(utilisation.equals("non")) Config.boite0_visible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nom_affiche")){
                            Config.boite0_nom = parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_cardinalite_totale")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite0_affichage_cadrinalite_totale = true;
                            if(utilisation.equals("non")) Config.boite0_affichage_cadrinalite_totale = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_nombre_elements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite0_affichage_machine = true;
                            if(utilisation.equals("non")) Config.boite0_affichage_machine = false;
                            parser.nextTag();
                        }
                        // CONTRAINTES BOITE
                        if(parser.getName().equals("contraintes_boite")) {
                            parser.nextTag();
                            // BUCHETTE
                            if(parser.getName().equals("buchette")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite0_nombre_buchette_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("nombre_max")){
                                    Config.boite0_nb_max_buchette = new Integer(parser.nextText());
                                    if(Config.boite0_nb_max_buchette > 0) Config.boite0_typeObjet +="buchette";
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("comportement_si_depassement")){
                                    String utilisation = parser.nextText();
                                    if(utilisation.equals("autorisation_action")) Config.boite0_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                                    if(utilisation.equals("refus_action")) Config.boite0_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                                    if(utilisation.equals("autorisation_et_message")) Config.boite0_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if(utilisation.equals("refus_et_message")) Config.boite0_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                    if(Config.boite0_action_depassement_buchette != null) Log.d("parser", Config.boite0_action_depassement_buchette.toString());
                                    if(Config.boite0_action_depassement_buchette == null) Log.d("parser", "action null");
                                }
                                if(parser.getName().equals("message_si_autorisation")){
                                    Config.boite0_message_si_depassement_buchette_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_refus")){
                                    Config.boite0_message_si_depassement_buchette_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  10
                            if(parser.getName().equals("groupement10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite0_nombre_groupement10_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite0_nb_max_groupement10 = new Integer(parser.nextText());
                                    if(Config.boite0_nb_max_groupement10 > 0) Config.boite0_typeObjet +="groupement10,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite0_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite0_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite0_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite0_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite0_message_si_depassement_groupement10_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite0_message_si_depassement_groupement10_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100b
                            if(parser.getName().equals("groupement100_unites")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite0_nombre_groupement100b_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite0_nb_max_groupement100b = new Integer(parser.nextText());
                                    if(Config.boite0_nb_max_groupement100b > 0) Config.boite0_typeObjet +="groupement100b,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite0_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite0_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite0_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite0_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite0_message_si_depassement_groupement100b_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite0_message_si_depassement_groupement100b_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100
                            if(parser.getName().equals("groupement_100_groupement_10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite0_nombre_groupement100_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite0_nb_max_groupement100 = new Integer(parser.nextText());
                                    if(Config.boite0_nb_max_groupement100 > 0) Config.boite0_typeObjet +="groupement100,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite0_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite0_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite0_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite0_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite0_message_si_depassement_groupement100_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite0_message_si_depassement_groupement100_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                            }
                        }
                    }
                    /*   BOITE   2    */
                    if(parser.getName().equals("boite_2")) {
                        parser.nextTag();
                        if(parser.getName().equals("visible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite1_visible = true;
                            if(utilisation.equals("non")) Config.boite1_visible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nom_affiche")){
                            Config.boite1_nom = parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_cardinalite_totale")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite1_affichage_cadrinalite_totale = true;
                            if(utilisation.equals("non")) Config.boite1_affichage_cadrinalite_totale = false;
                            Log.d("parser", ""+Config.boite1_affichage_cadrinalite_totale);
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_nombre_elements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite1_affichage_machine = true;
                            if(utilisation.equals("non")) Config.boite1_affichage_machine = false;
                            parser.nextTag();
                        }
                        // CONTRAINTES BOITE
                        if(parser.getName().equals("contraintes_boite")) {
                            parser.nextTag();
                            // BUCHETTE
                            if(parser.getName().equals("buchette")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite1_nombre_buchette_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("nombre_max")){
                                    Config.boite1_nb_max_buchette = new Integer(parser.nextText());
                                    if(Config.boite1_nb_max_buchette > 0) Config.boite1_typeObjet +="buchette";
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("comportement_si_depassement")){
                                    String utilisation = parser.nextText();
                                    if(utilisation.equals("autorisation_action")) Config.boite1_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                                    if(utilisation.equals("refus_action")) Config.boite1_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                                    if(utilisation.equals("autorisation_et_message")) Config.boite1_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if(utilisation.equals("refus_et_message")) Config.boite1_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_autorisation")){
                                    Config.boite1_message_si_depassement_buchette_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_refus")){
                                    Config.boite1_message_si_depassement_buchette_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  10
                            if(parser.getName().equals("groupement10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite1_nombre_groupement10_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite1_nb_max_groupement10 = new Integer(parser.nextText());
                                    if(Config.boite1_nb_max_groupement10 > 0) Config.boite1_typeObjet +="groupement10,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite1_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite1_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite1_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite1_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite1_message_si_depassement_groupement10_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite1_message_si_depassement_groupement10_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100b
                            if(parser.getName().equals("groupement100_unites")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite1_nombre_groupement100b_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite1_nb_max_groupement100b = new Integer(parser.nextText());
                                    if(Config.boite1_nb_max_groupement100b > 0) Config.boite1_typeObjet +="groupement100b,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite1_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite1_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite1_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite1_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite1_message_si_depassement_groupement100b_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite1_message_si_depassement_groupement100b_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100
                            if(parser.getName().equals("groupement_100_groupement_10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite1_nombre_groupement100_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite1_nb_max_groupement100 = new Integer(parser.nextText());
                                    if(Config.boite1_nb_max_groupement100 > 0) Config.boite1_typeObjet +="groupement100,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite1_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite1_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite1_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite1_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite1_message_si_depassement_groupement100_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite1_message_si_depassement_groupement100_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                            }
                        }
                    }


                    /*   BOITE   3    */
                    if(parser.getName().equals("boite_3")) {
                        parser.nextTag();
                        if(parser.getName().equals("visible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite2_visible = true;
                            if(utilisation.equals("non")) Config.boite2_visible = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("nom_affiche")){
                            Config.boite2_nom = parser.nextText();
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_cardinalite_totale")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite2_affichage_cadrinalite_totale = true;
                            if(utilisation.equals("non")) Config.boite2_affichage_cadrinalite_totale = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_nombre_elements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite2_affichage_machine = true;
                            if(utilisation.equals("non")) Config.boite2_affichage_machine = false;
                            parser.nextTag();
                        }
                        // CONTRAINTES BOITE
                        if(parser.getName().equals("contraintes_boite")) {
                            parser.nextTag();
                            // BUCHETTE
                            if(parser.getName().equals("buchette")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite2_nombre_buchette_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("nombre_max")){
                                    Config.boite2_nb_max_buchette = new Integer(parser.nextText());
                                    if(Config.boite2_nb_max_buchette > 0) Config.boite2_typeObjet +="buchette,";
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("comportement_si_depassement")){
                                    String utilisation = parser.nextText();
                                    if(utilisation.equals("autorisation_action")) Config.boite2_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                                    if(utilisation.equals("refus_action")) Config.boite2_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                                    if(utilisation.equals("autorisation_et_message")) Config.boite2_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if(utilisation.equals("refus_et_message")) Config.boite2_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_autorisation")){
                                    Config.boite2_message_si_depassement_buchette_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_refus")){
                                    Config.boite2_message_si_depassement_buchette_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  10
                            if(parser.getName().equals("groupement10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite2_nombre_groupement10_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite2_nb_max_groupement10 = new Integer(parser.nextText());
                                    if(Config.boite2_nb_max_groupement10 > 0) Config.boite2_typeObjet +="groupement10,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite2_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite2_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite2_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite2_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite2_message_si_depassement_groupement10_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite2_message_si_depassement_groupement10_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100b
                            if(parser.getName().equals("groupement100_unites")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite2_nombre_groupement100b_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite2_nb_max_groupement100b = new Integer(parser.nextText());
                                    if(Config.boite2_nb_max_groupement100b > 0) Config.boite2_typeObjet +="groupement100b,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite2_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite2_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite2_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite2_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite2_message_si_depassement_groupement100b_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite2_message_si_depassement_groupement100b_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100
                            if(parser.getName().equals("groupement_100_groupement_10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite2_nombre_groupement100_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite2_nb_max_groupement100 = new Integer(parser.nextText());
                                    if(Config.boite2_nb_max_groupement100 > 0) Config.boite2_typeObjet +="groupement100,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite2_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite2_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite2_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite2_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite2_message_si_depassement_groupement100_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite2_message_si_depassement_groupement100_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                            }
                        }
                    }

                     /*   BOITE   4    */
                    if(parser.getName().equals("boite_4")) {
                        parser.nextTag();
                        if(parser.getName().equals("visible")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite3_visible = true;
                            if(utilisation.equals("non")) Config.boite3_visible = false;
                            parser.nextTag();
                        }
                        //Log.d("parser, balise :", parser.getName());
                        if(parser.getName().equals("nom_affiche")){
                            Config.boite3_nom = parser.nextText();
                            Log.d("nom boite :",Config.boite3_nom);
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_cardinalite_totale")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite3_affichage_cadrinalite_totale = true;
                            if(utilisation.equals("non")) Config.boite3_affichage_cadrinalite_totale = false;
                            parser.nextTag();
                        }
                        if(parser.getName().equals("afficher_nombre_elements")){
                            String utilisation = parser.nextText();
                            if(utilisation.equals("oui")) Config.boite3_affichage_machine = true;
                            if(utilisation.equals("non")) Config.boite3_affichage_machine = false;
                            parser.nextTag();
                        }
                        // CONTRAINTES BOITE
                        if(parser.getName().equals("contraintes_boite")) {
                            parser.nextTag();
                            // BUCHETTE
                            if(parser.getName().equals("buchette")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite3_nombre_buchette_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("nombre_max")){
                                    Config.boite3_nb_max_buchette = new Integer(parser.nextText());
                                    if(Config.boite3_nb_max_buchette > 0) Config.boite3_typeObjet +="buchette";
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("comportement_si_depassement")){
                                    String utilisation = parser.nextText();
                                    if(utilisation.equals("autorisation_action")) Config.boite3_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                                    if(utilisation.equals("refus_action")) Config.boite3_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                                    if(utilisation.equals("autorisation_et_message")) Config.boite3_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if(utilisation.equals("refus_et_message")) Config.boite3_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_autorisation")){
                                    Config.boite3_message_si_depassement_buchette_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("message_si_refus")){
                                    Config.boite3_message_si_depassement_buchette_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  10
                            if(parser.getName().equals("groupement10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite3_nombre_groupement10_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite3_nb_max_groupement10 = new Integer(parser.nextText());
                                    if(Config.boite3_nb_max_groupement10 > 0) Config.boite3_typeObjet +="groupement10,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite3_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite3_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite3_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite3_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite3_message_si_depassement_groupement10_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite3_message_si_depassement_groupement10_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            // GROUPEMENT  100b
                            if(parser.getName().equals("groupement100_unites")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite3_nombre_groupement100b_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite3_nb_max_groupement100b = new Integer(parser.nextText());
                                    if(Config.boite3_nb_max_groupement100b > 0) Config.boite3_typeObjet +="groupement100b,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite3_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite3_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite3_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite3_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite3_message_si_depassement_groupement100b_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite3_message_si_depassement_groupement100b_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }

                            // GROUPEMENT  100
                            if(parser.getName().equals("groupement_100_groupement_10")) {
                                parser.nextTag();
                                if(parser.getName().equals("nombre_contenu_initialement")){
                                    Config.boite3_nombre_groupement100_initial = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("nombre_max")) {
                                    Config.boite3_nb_max_groupement100 = new Integer(parser.nextText());
                                    if(Config.boite3_nb_max_groupement100 > 0) Config.boite3_typeObjet +="groupement100,";
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("comportement_si_depassement")) {
                                    String utilisation = parser.nextText();
                                    if (utilisation.equals("autorisation_action"))
                                        Config.boite3_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                                    if (utilisation.equals("refus_action"))
                                        Config.boite3_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                                    if (utilisation.equals("autorisation_et_message"))
                                        Config.boite3_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                                    if (utilisation.equals("refus_et_message"))
                                        Config.boite3_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_autorisation")) {
                                    Config.boite3_message_si_depassement_groupement100_autorise = parser.nextText();
                                    parser.nextTag();
                                }
                                if (parser.getName().equals("message_si_refus")) {
                                    Config.boite3_message_si_depassement_groupement100_refuse = parser.nextText();
                                    parser.nextTag();
                                }
                            }
                            parser.nextTag();
                        }
                    }
                      /*   RÉGLES   DE   GROUPEMENTS    */
                    if(parser.getName().equals("regles_groupements")) {
                        parser.nextTag();
                        // GROUPEMENT 10
                        if(parser.getName().equals("groupement_10")) {
                            parser.nextTag();
                            if(parser.getName().equals("autorise")){
                                String utilisation = parser.nextText();
                                if(utilisation.equals("oui")) Config.groupement10_possible = true;
                                if(utilisation.equals("non")) Config.groupement10_possible = false;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("comportement_si_erreur")) {
                                String utilisation = parser.nextText();
                                if (utilisation.equals("autorisation_action"))
                                    Config.regle_groupement10_action = Alert.Action.AUTORISATION_ACTION;
                                if (utilisation.equals("refus_action"))
                                    Config.regle_groupement10_action = Alert.Action.REFUS_ACTION;
                                if (utilisation.equals("autorisation_et_message"))
                                    Config.regle_groupement10_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                                if (utilisation.equals("refus_et_message"))
                                    Config.regle_groupement10_action = Alert.Action.REFUS_ET_MESSAGE;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_inf")) {
                                Config.regle_groupement10_message_autorise_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_sup")) {
                                Config.regle_groupement10_message_autorise_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_non_autorise")) {
                                Config.regle_groupement10_message_autorise_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_inf")) {
                                Config.regle_groupement10_message_refus_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_sup")) {
                                Config.regle_groupement10_message_refus_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_non_autorise")) {
                                Config.regle_groupement10_message_refus_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                            parser.nextTag();
                        }

                        // GROUPEMENT 100b
                        if(parser.getName().equals("groupement_100_unite")) {
                            parser.nextTag();
                            if(parser.getName().equals("autorise")){
                                String utilisation = parser.nextText();
                                if(utilisation.equals("oui")) Config.groupement_simple = true;
                                if(utilisation.equals("non")) Config.groupement_simple = false;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("comportement_si_erreur")) {
                                String utilisation = parser.nextText();
                                if (utilisation.equals("autorisation_action"))
                                    Config.regle_groupement100b_action = Alert.Action.AUTORISATION_ACTION;
                                if (utilisation.equals("refus_action"))
                                    Config.regle_groupement100b_action = Alert.Action.REFUS_ACTION;
                                if (utilisation.equals("autorisation_et_message"))
                                    Config.regle_groupement100b_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                                if (utilisation.equals("refus_et_message"))
                                    Config.regle_groupement100b_action = Alert.Action.REFUS_ET_MESSAGE;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_inf")) {
                                Config.regle_groupement100b_message_autorise_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_sup")) {
                                Config.regle_groupement100b_message_autorise_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_non_autorise")) {
                                Config.regle_groupement100b_message_autorise_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_inf")) {
                                Config.regle_groupement100b_message_refus_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_sup")) {
                                Config.regle_groupement100b_message_refus_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_non_autorise")) {
                                Config.regle_groupement100b_message_refus_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                            parser.nextTag();
                        }
                        // GROUPEMENT 100
                        if(parser.getName().equals("groupement_100_groupement_10")) {
                            parser.nextTag();
                            if(parser.getName().equals("autorise")){
                                String utilisation = parser.nextText();
                                if(utilisation.equals("oui")) Config.groupement100_possible = true;
                                if(utilisation.equals("non")) Config.groupement100_possible = false;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("comportement_si_erreur")) {
                                String utilisation = parser.nextText();
                                if (utilisation.equals("autorisation_action"))
                                    Config.regle_groupement100_action = Alert.Action.AUTORISATION_ACTION;
                                if (utilisation.equals("refus_action"))
                                    Config.regle_groupement100_action = Alert.Action.REFUS_ACTION;
                                if (utilisation.equals("autorisation_et_message"))
                                    Config.regle_groupement100_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                                if (utilisation.equals("refus_et_message"))
                                    Config.regle_groupement100_action = Alert.Action.REFUS_ET_MESSAGE;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_inf")) {
                                Config.regle_groupement100_message_autorise_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_nb_sup")) {
                                Config.regle_groupement100_message_autorise_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_autorisation_si_non_autorise")) {
                                Config.regle_groupement100_message_autorise_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_inf")) {
                                Config.regle_groupement100_message_refus_si_nb_inf = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_nb_sup")) {
                                Config.regle_groupement100_message_refus_si_nb_sup = parser.nextText();
                                parser.nextTag();
                            }
                            if (parser.getName().equals("message_refus_si_non_autorise")) {
                                Config.regle_groupement100_message_refus_si_non_autorise = parser.nextText();
                                parser.nextTag();
                            }
                        }
                        parser.nextTag();
                    }
                       /*   CONFIGURATIONS   ATTENDUES    */
                    if(parser.getName().equals("configurations_attendues")) {
                        parser.nextTag();
                        /* Config */
                        while(parser.getName().equals("config")) {
                            parser.nextTag();
                            Config_Attendue config_attendue = new Config_Attendue();
                            if(parser.getName().equals("nom_configuration")) {
                                config_attendue.nom = parser.nextText();
                                parser.nextTag();
                            }
                            if(parser.getName().equals("boite_1")) {
                                parser.nextTag();
                                if(parser.getName().equals("buchette")) {
                                    config_attendue.boites.get(0).nb_buchettes = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement10")) {
                                    config_attendue.boites.get(0).nb_groupement10 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement100_unite")) {
                                    config_attendue.boites.get(0).nb_groupement100b = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement_100_groupement_10")) {
                                    config_attendue.boites.get(0).nb_groupement100 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            if(parser.getName().equals("boite_2")) {
                                parser.nextTag();
                                if(parser.getName().equals("buchette")) {
                                    config_attendue.boites.get(1).nb_buchettes = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement10")) {
                                    config_attendue.boites.get(1).nb_groupement10 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement100_unite")) {
                                    config_attendue.boites.get(1).nb_groupement100b = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement_100_groupement_10")) {
                                    config_attendue.boites.get(1).nb_groupement100 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            if(parser.getName().equals("boite_3")) {
                                parser.nextTag();
                                if(parser.getName().equals("buchette")) {
                                    config_attendue.boites.get(2).nb_buchettes = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement10")) {
                                    config_attendue.boites.get(2).nb_groupement10 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement100_unite")) {
                                    config_attendue.boites.get(2).nb_groupement100b = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement_100_groupement_10")) {
                                    config_attendue.boites.get(2).nb_groupement100 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            if(parser.getName().equals("boite_4")) {
                                parser.nextTag();
                                if(parser.getName().equals("buchette")) {
                                    config_attendue.boites.get(3).nb_buchettes = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement10")) {
                                    config_attendue.boites.get(3).nb_groupement10 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement100_unite")) {
                                    config_attendue.boites.get(3).nb_groupement100b = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                if(parser.getName().equals("groupement_100_groupement_10")) {
                                    config_attendue.boites.get(3).nb_groupement100 = new Integer(parser.nextText());
                                    parser.nextTag();
                                }
                                parser.nextTag();
                            }
                            if(parser.getName().equals("table_vide")) {
                                String utilisation = parser.nextText();
                                if(utilisation.equals("oui")) config_attendue.table_vide = true;
                                if(utilisation.equals("non")) config_attendue.table_vide = false;
                                parser.nextTag();
                            }
                            if (parser.getName().equals("comportement_fin")) {
                                String utilisation = parser.nextText();
                                if (utilisation.equals("sortie"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.SORTIE;
                                if (utilisation.equals("continuer"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.CONTINUER;
                                if (utilisation.equals("nouvel_eleve"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.NOUVEL_ELEVE;
                                if (utilisation.equals("sortie_message"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.SORTIE_ET_MESSAGE;
                                if (utilisation.equals("continuer_message"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.CONTINUER_ET_MESSAGE;
                                if (utilisation.equals("nouvel_eleve_message"))
                                    config_attendue.comportement_fin = Config_Attendue.Action.NOUVEL_ELEVE_ET_MESSAGE;
                                Log.d("comportement", config_attendue.comportement_fin.toString());
                                parser.nextTag();
                            }
                            if(parser.getName().equals("message_sortie")) {
                                config_attendue.message_sortie = parser.nextText();
                                parser.nextTag();
                            }
                            if(parser.getName().equals("message_continuer")) {
                                config_attendue.message_continuer = parser.nextText();
                                parser.nextTag();
                            }
                            if(parser.getName().equals("message_nouvel_eleve")) {
                                config_attendue.message_nouvel_eleve = parser.nextText();
                                parser.nextTag();
                            }
                            Config.configurations_attendues.add(config_attendue);
                            parser.nextTag();
                        }
                        if(parser.getName().equals("config_par_defaut")) {
                            parser.nextTag();
                            if (parser.getName().equals("comportement_fin")) {
                                String utilisation = parser.nextText();
                                if (utilisation.equals("sortie"))
                                    Config.conguration_par_defaut.comportement_fin= Config_Attendue.Action.SORTIE;
                                if (utilisation.equals("continuer"))
                                    Config.conguration_par_defaut.comportement_fin= Config_Attendue.Action.CONTINUER;
                                if (utilisation.equals("sortie_message"))
                                    Config.conguration_par_defaut.comportement_fin= Config_Attendue.Action.SORTIE_ET_MESSAGE;
                                if (utilisation.equals("continuer_message"))
                                    Config.conguration_par_defaut.comportement_fin= Config_Attendue.Action.CONTINUER_ET_MESSAGE;
                                parser.nextTag();
                            }
                            if(parser.getName().equals("message_sortie")) {
                                Config.conguration_par_defaut.message_sortie = parser.nextText();
                                parser.nextTag();
                            }
                            if(parser.getName().equals("message_continuer")) {
                                Config.conguration_par_defaut.message_continuer = parser.nextText();
                                parser.nextTag();
                            }
                        }

                    }
                    if(parser.getName().equals("consigne")) {
                        Config.consigne = parser.nextText();
                        parser.nextTag();
                    }
                    if(parser.getName().equals("nb_actions_de_reference")) {
                        Config.nb_actions_de_reference = new Integer(parser.nextText());
                        parser.nextTag();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
    }
}



