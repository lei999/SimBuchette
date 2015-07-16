package com.ter.app.applicationtest.parametresExercices;

import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.feedbacks.Alert;
import com.ter.app.applicationtest.gestionBoutons.BoutonTerminer;

import java.util.ArrayList;

/**
 * Classe représentant la configuration de la simulation d'un exercice
 */
public class Config {
    // TAILLE DES OBJETS
    public static int id_image_buchette = R.drawable.buchette_g;
    public static int id_image_groupement3 = R.drawable.groupement3;
    public static int id_image_groupement10 = R.drawable.groupement10;
    public static int id_image_groupement100 = R.drawable.groupement100;
    public static int id_image_groupement100b = R.drawable.groupement100b_g;
    public static int taille = 1; // 1 pour G, 2 pour M, 3 pour P
    public static boolean exercice_grandes_boites = false;
    public static boolean exercice_complement = false;


    // ELEMENTS PRÉSENTS DANS L'ÉCRAN
    public static boolean utilisation_zone_groupements = false;
    public static boolean utilisation_dupliquer = false;
    public static boolean utilisation_poubelle = false;
    public static BoutonTerminer.Action action_bouton_terminer;

    public static String DEFAULT = "default";

    // CARDINALITÉ RÉSERVE
    public static int nb_buchettes = 0;
    public static int nb_groupement3 = 0;
    public static int nb_groupement10 = 0;
    public static int nb_groupement100 = 0;
    public static int nb_groupement100b = 0;

    // BOUTONS
    public static boolean vider = false;
    public static boolean reset = false;
    public static boolean terminer = false;
    public static boolean annuler = false;


    // BOITE 0
    public static int boite0_max_objets;
    public static boolean boite0_differentObjets = false;
    public static boolean boite0_affichage_machine = false;
    public static boolean boite0_affichage_cadrinalite_totale = false;
    public static String boite0_nom = "";
    public static boolean boite0_popup = false;
    public static boolean boite0_visible = false;
    public static String boite0_typeObjet = "";

    public static int boite0_nb_max_buchette = 0;
    public static Alert.Action boite0_action_depassement_buchette;
    public static String boite0_message_si_depassement_buchette_autorise;
    public static String boite0_message_si_depassement_buchette_refuse;

    public static int boite0_nb_max_groupement10 = 0;
    public static Alert.Action boite0_action_depassement_groupement10;
    public static String boite0_message_si_depassement_groupement10_autorise;
    public static String boite0_message_si_depassement_groupement10_refuse;

    public static int boite0_nb_max_groupement100 = 0;
    public static Alert.Action boite0_action_depassement_groupement100;
    public static String boite0_message_si_depassement_groupement100_autorise;
    public static String boite0_message_si_depassement_groupement100_refuse;

    public static int boite0_nb_max_groupement100b = 0;
    public static Alert.Action boite0_action_depassement_groupement100b;
    public static String boite0_message_si_depassement_groupement100b_autorise;
    public static String boite0_message_si_depassement_groupement100b_refuse;

    public static int boite0_nombre_buchette_initial;
    public static int boite0_nombre_groupement10_initial;
    public static int boite0_nombre_groupement100_initial;
    public static int boite0_nombre_groupement100b_initial;




    // BOITE 1
    public static int boite1_max_objets;
    public static boolean boite1_differentObjets = false;
    public static boolean boite1_affichage_machine = false;
    public static boolean boite1_affichage_cadrinalite_totale = false;
    public static String boite1_nom = "";
    public static boolean boite1_popup = false;
    public static boolean boite1_visible = false;
    public static String boite1_typeObjet ="";

    public static int boite1_nb_max_buchette = 0;
    public static Alert.Action boite1_action_depassement_buchette;
    public static String boite1_message_si_depassement_buchette_autorise;
    public static String boite1_message_si_depassement_buchette_refuse;

    public static int boite1_nb_max_groupement10 = 0;
    public static Alert.Action boite1_action_depassement_groupement10;
    public static String boite1_message_si_depassement_groupement10_autorise;
    public static String boite1_message_si_depassement_groupement10_refuse;

    public static int boite1_nb_max_groupement100 = 0;
    public static Alert.Action boite1_action_depassement_groupement100;
    public static String boite1_message_si_depassement_groupement100_autorise;
    public static String boite1_message_si_depassement_groupement100_refuse;


    public static int boite1_nb_max_groupement100b = 0;
    public static Alert.Action boite1_action_depassement_groupement100b;
    public static String boite1_message_si_depassement_groupement100b_autorise;
    public static String boite1_message_si_depassement_groupement100b_refuse;

    public static int boite1_nombre_buchette_initial;
    public static int boite1_nombre_groupement10_initial;
    public static int boite1_nombre_groupement100_initial;
    public static int boite1_nombre_groupement100b_initial;



    // BOITE 2
    public static int boite2_max_objets;
    public static boolean boite2_differentObjets = false;
    public static boolean boite2_affichage_machine = false;
    public static boolean boite2_affichage_cadrinalite_totale = false;
    public static String boite2_nom = "";
    public static boolean boite2_popup = false;
    public static boolean boite2_visible = false;
    public static String boite2_typeObjet ="";

    public static int boite2_nb_max_buchette = 0;
    public static Alert.Action boite2_action_depassement_buchette;
    public static String boite2_message_si_depassement_buchette_autorise;
    public static String boite2_message_si_depassement_buchette_refuse;

    public static int boite2_nb_max_groupement10 = 0;
    public static Alert.Action boite2_action_depassement_groupement10;
    public static String boite2_message_si_depassement_groupement10_autorise;
    public static String boite2_message_si_depassement_groupement10_refuse;

    public static int boite2_nb_max_groupement100 = 0;
    public static Alert.Action boite2_action_depassement_groupement100;
    public static String boite2_message_si_depassement_groupement100_autorise;
    public static String boite2_message_si_depassement_groupement100_refuse;

    public static int boite2_nb_max_groupement100b = 0;
    public static Alert.Action boite2_action_depassement_groupement100b;
    public static String boite2_message_si_depassement_groupement100b_autorise;
    public static String boite2_message_si_depassement_groupement100b_refuse;

    public static int boite2_nombre_buchette_initial;
    public static int boite2_nombre_groupement10_initial;
    public static int boite2_nombre_groupement100_initial;
    public static int boite2_nombre_groupement100b_initial;



    // BOITE 3
    public static int boite3_max_objets;
    public static boolean boite3_differentObjets = false;
    public static boolean boite3_affichage_machine = false;
    public static boolean boite3_affichage_cadrinalite_totale = false;
    public static String boite3_nom  = "";
    public static boolean boite3_popup = false;
    public static boolean boite3_visible = false;
    public static String boite3_typeObjet ="";

    public static int boite3_nb_max_buchette = 0;
    public static Alert.Action boite3_action_depassement_buchette;
    public static String boite3_message_si_depassement_buchette_autorise;
    public static String boite3_message_si_depassement_buchette_refuse;

    public static int boite3_nb_max_groupement10 = 0;
    public static Alert.Action boite3_action_depassement_groupement10;
    public static String boite3_message_si_depassement_groupement10_autorise;
    public static String boite3_message_si_depassement_groupement10_refuse;

    public static int boite3_nb_max_groupement100 = 0;
    public static Alert.Action boite3_action_depassement_groupement100;
    public static String boite3_message_si_depassement_groupement100_autorise;
    public static String boite3_message_si_depassement_groupement100_refuse;


    public static int boite3_nb_max_groupement100b = 0;
    public static Alert.Action boite3_action_depassement_groupement100b;
    public static String boite3_message_si_depassement_groupement100b_autorise;
    public static String boite3_message_si_depassement_groupement100b_refuse;

    public static int boite3_nombre_buchette_initial;
    public static int boite3_nombre_groupement10_initial;
    public static int boite3_nombre_groupement100_initial;
    public static int boite3_nombre_groupement100b_initial;



    // RÈGLES DE GROUPEMENTS
    public static boolean groupement3_possible = false;

    // GROUPEMENTS DE 10
    public static boolean groupement10_possible = false;
    public static Alert.Action regle_groupement10_action;
    public static String regle_groupement10_message_autorise_si_nb_inf;
    public static String regle_groupement10_message_autorise_si_nb_sup;
    public static String regle_groupement10_message_autorise_si_non_autorise;
    public static String regle_groupement10_message_refus_si_nb_inf;
    public static String regle_groupement10_message_refus_si_nb_sup;
    public static String regle_groupement10_message_refus_si_non_autorise;

    // GROUPEMENTS DE 100
    public static boolean groupement100_possible = false;
    public static Alert.Action regle_groupement100_action;
    public static String regle_groupement100_message_autorise_si_nb_inf;
    public static String regle_groupement100_message_autorise_si_nb_sup;
    public static String regle_groupement100_message_autorise_si_non_autorise;
    public static String regle_groupement100_message_refus_si_nb_inf;
    public static String regle_groupement100_message_refus_si_nb_sup;
    public static String regle_groupement100_message_refus_si_non_autorise;

    // GROUPEMENTS DE 100B
    public static boolean groupement_simple = false;
    public static Alert.Action regle_groupement100b_action;
    public static String regle_groupement100b_message_autorise_si_nb_inf;
    public static String regle_groupement100b_message_autorise_si_nb_sup;
    public static String regle_groupement100b_message_autorise_si_non_autorise;
    public static String regle_groupement100b_message_refus_si_nb_inf;
    public static String regle_groupement100b_message_refus_si_nb_sup;
    public static String regle_groupement100b_message_refus_si_non_autorise;



    // CONFIGURATIONS ATTENDUES
    public static ArrayList<Config_Attendue> configurations_attendues = new ArrayList<>();
    public static Config_Attendue conguration_par_defaut = new Config_Attendue();


    // CONSIGNE
    public static String consigne;

    // NOMBRES D'ACTIONS DE RÉFÉRENCE
    public static int nb_actions_de_reference;

    // solution
    public static String solution_boite0_type_objet ="";
    public static String solution_boite1_type_objet="";
    public static String solution_boite2_type_objet="";
    public static String solution_boite3_type_objet="";

    public static int solution_boite0_nb_objet;
    public static int solution_boite1_nb_objet;
    public static int solution_boite2_nb_objet;
    public static int solution_boite3_nb_objet;


    public static void reset(){
        // nb Objets
        nb_buchettes = 0;
        nb_groupement3 = 0;
        nb_groupement10 = 0;

        // boutons
        vider = false;
        reset = false;
        terminer = false;
        annuler = false;


        // boite 0
        boite0_max_objets = 0;
        boite0_differentObjets = false;
        boite0_affichage_machine = false;
        boite0_nom = DEFAULT;
        boite0_popup = false;
        boite0_visible = false;
        boite0_typeObjet = "";

        // boite 1
        boite1_max_objets = 0;
        boite1_differentObjets = false;
        boite1_affichage_machine = false;
        boite1_nom = DEFAULT;
        boite1_popup = false;
        boite1_visible = false;
        boite1_typeObjet ="";


        // boite 2
        boite2_max_objets = 0;
        boite2_differentObjets = false;
        boite2_affichage_machine = false;
        boite2_nom = DEFAULT;
        boite2_popup = false;
        boite2_visible = false;
        boite2_typeObjet ="";


        // boite 3
        boite3_max_objets = 0;
        boite3_differentObjets = false;
        boite3_affichage_machine = false;
        boite3_nom  = DEFAULT;
        boite3_popup = false;
        boite3_visible = false;
        boite3_typeObjet ="";


        // consigne
        consigne = "";


        // solution
        solution_boite0_type_objet ="";
        solution_boite1_type_objet="";
        solution_boite2_type_objet="";
        solution_boite3_type_objet="";

        solution_boite0_nb_objet = 0;
        solution_boite1_nb_objet = 0;
        solution_boite2_nb_objet = 0;
        solution_boite3_nb_objet = 0;

    }


}
