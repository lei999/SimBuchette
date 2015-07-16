package com.ter.app.applicationtest.feedbacks;

import android.util.Log;
import android.widget.RelativeLayout;

import com.ter.app.applicationtest.Contenants.Boite;
import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.MQTT.MQTT;
import com.ter.app.applicationtest.parametresExercices.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe permettant de gérer les traces et l'envoit de traces et de l'état de simulation par l'intermédiare de MQTT.
 */
public class Traces{

    /**
     * L'identifiant de l'élève
     */
    public static int id = 0;
    /**
     * Le nom de l'élève
     */
    public static String nomEleve = "";
    /**
     * Le nom de l'exercice
     */
    public static String nomExercice = "";
    /**
     * Les traces pour la gestion du bouton annuler
     */
    public static ArrayList<String> traces = new ArrayList<>();
    /**
     * Les traces pour l'analyse de la résolution de l'exercice par l'élève
     */
    public static ArrayList<String> tracesEleve = new ArrayList<>();

    /**
     * Constructeur de la trace
     */
    public Traces(){
        super();
    }

    /**
     * Méthode permettant de récupérer les traces (pour le bouton annuler) sous la forme d'un String
     * @return les traces (pour le bouton annuler) sous la forme d'un String
     */
    public static String string(){
        String s = "";
        for(int i = 0; i <traces.size() ; i++){
            s += traces.get(i) + "\n";
        }
        return s;
    }


    /**
     * Méthode permettant de récupérer les traces (pour l'analyse) sous la forme d'un String. (Permet de générer la String pour la création du fichier de traces)
     * @return les traces (pour l'analyse) sous la forme d'un String
     */
    public static String stringTraceEleve() {
        String s = "Timestamp,auteur,exercice,action,paramètres \n";
        for (int i = 0; i < tracesEleve.size(); i++) {
            s += tracesEleve.get(i) + "\n";
        }
        return s;
    }

    /**
     * Méthode permettant d'ajouter une trace d'un déplacement
     * @param objet le type de l'objet déplacé
     * @param source le nom du contenant où était l'objet
     * @param destnation le nom du contenant où est déposé l'objet
     * @param resultat le résultat du déplacement (si il a réussi ou non)
     */
    public static void addDeplacement(String objet, String source, String destnation, String resultat, boolean modif_etat){
        Date d = new Date();
        // si l'action a réussi
        if(resultat.equals("reussi")){
            // on ajoute une trace de déplacement dans les traces pour le bouton annuler
            traces.add("deplacement," + objet + "," + source + "," + destnation + "," + resultat);
            // on ajoute une trace de déplacementpour les traces à analyser
            tracesEleve.add("" + d + "," + nomEleve + "," + nomExercice + "," + "déplacement," + objet + "," + source + "," + destnation);
        }
        // sinon, si l'action a échouée
        if(resultat.equals("echec")){
            // on ajoute une trace d'interdiction de déplacement par le système
            tracesEleve.add("" + d + "," + "Système" + "," + nomExercice + "," + "interdiction" + "," + "déplacement," + objet + "," + source + "," + destnation);
        }
        // si l'état de la simulation a été modifié
        if(modif_etat){
            // on ajoute l'objet déplacé dans le contenant où l'élève a déposé l'objet
            Etat_simulation.ajouter(Etat_simulation.stringToObjet(objet),Etat_simulation.stringToContenant(destnation));
            // on enlève l'objet déplacé du contenant d'où l'élève a pris l'objet
            Etat_simulation.enlever(Etat_simulation.stringToObjet(objet), Etat_simulation.stringToContenant(source));
            // on créer un JSONObject représentant l'état de la simulation
            JSONObject jsonObject = Etat_simulation.etat_Simulation_JSON();
            // on envoi un message correspondant à l'état de la simulation, par l'intermédiaire de MQTT
            MQTT.getInstance().sendMessage("ETAT_SIMULATION", jsonObject.toString());
            // permet de vérifier dans la console quelle message d'action va être envoyé par MQTT
            Log.d("JSON", jsonObject.toString());
        }
        // permet de vérifier dans la console quelle message d'action va être envoyé par MQTT
        Log.d("JSON", action(d, id, nomExercice, "deplacement").toString());
        // on envois un message correspondant à l'action effectuée, par l'intermédiaire de MQTT
        if(!resultat.equals("attente"))MQTT.getInstance().sendMessage("ACTIONS", action(d, id, nomExercice, "deplacement").toString());

    }


    /**
     * Méthode permettant d'ajouter une trace de l'action vider une boite
     * @param nomBoite le nom de la boite qui a été vidée
     * @param nbObjets le nombre d'objets qu'elle contenait
     * @param typeObjet le type des objets qu'elle contenait
     */
    public static void addActionVider(String nomBoite, int nbObjets, String[] typeObjet ){
        Date d = new Date();
        String objets = " [";
        // pour tous les objets enlevés de la boite
        for(int numObjet = 0; numObjet < nbObjets; numObjet ++){
            // on ajoute une trace précisant que l'on a enlevé cet objet de la boite
            traces.add("vider," + nomBoite + "," + typeObjet[numObjet]);
            // on récupère l'ensemble des type des objets que l'on a vider
            if(numObjet == 0) objets += "" + typeObjet[numObjet];
            else objets += ", " + typeObjet[numObjet];
        }
        objets += " ]";
        // on ajoute une trace pour l'analyse précisant que l'on a vider la boite de tous les éléments (en précisant leurs types)
        tracesEleve.add(""+d+","+ nomEleve + "," + nomExercice +"," +"vider," + nomBoite + "," + objets);
    }

    /**
     * Méthode permettant d'ajouter une trace de l'action vider une boite
     * @param nomBoite le nom de la boite qui a été vidée
     * @param nbObjets le nombre d'objets qu'elle contenait
     * @param objets les objets que la boite contenait
     */
    public static void addActionVider(String nomBoite, int nbObjets, ArrayList<Etat_simulation.Objets> objets){
        Date d = new Date();
        // pour tous les objets enlevés de la boite
        for(int numObjet = 0; numObjet < nbObjets; numObjet ++){
            // on ajoute une trace précisant que l'on a enlevé cet objet de la boite
            traces.add("vider," + nomBoite + "," + objets.get(numObjet));
            // on enlève l'objet de la boite, dans l'état de la simulation
            Etat_simulation.enlever(objets.get(numObjet), Etat_simulation.stringToContenant(nomBoite));
        }
        // on créer un JSONObject représentant l'état de la simulation
        JSONObject jsonObject = Etat_simulation.etat_Simulation_JSON();
        // on envoi un message correspondant à l'état de la simulation, par l'intermédiaire de MQTT
        MQTT.getInstance().sendMessage("ETAT_SIMULATION", jsonObject.toString());
        // on ajoute une trace pour l'analyse précisant que l'on a vider la boite de tous les éléments (en précisant leurs types)
        tracesEleve.add(""+d+","+ nomEleve + "," + nomExercice +"," +"vider," + nomBoite + "," + objets);
        // on envois un message correspondant à l'action effectuée, par l'intermédiaire de MQTT
        MQTT.getInstance().sendMessage("ACTIONS", action(d, id, nomExercice, "vider").toString());
    }

    /**
     * Méthode permettant d'ajouter une trace de l'action de grouper des objets
     * @param nbObjets le nombre d'objets groupés
     * @param objet le type d'objet groupé
     * @param resultat le résultat de la réalisation de ce groupement
     * @param type le type de l'objet que l'on a groupé
     * @param modif_etat true si l'état de la simulation a été modifiée
     */
    public static void addActionGrouper(String nbObjets, String objet, String resultat, String type, boolean modif_etat){
        Date d = new Date();
        String s = "";
        if(resultat.equals("true")) s = "réussi";
        else s = "echec";
        // on ajoute une trace (pour le bouton annuler) que l'on a fait le groupement (réussi ou non)
        traces.add("grouper"+ "," + s + objet );
        // on ajoute aux traces, pour l'analyse, que l'élève a voulu faire un groupement
        tracesEleve.add("" + d + "," + nomEleve + "," + nomExercice + "," +"grouper," + objet);
        // si le groupement n'a pas été généré on génère une trace précisant que le système a interdit de faire ce groupemenent
        if(!resultat.equals("true")){tracesEleve.add(""+d+","+ "Système" + "," + nomExercice +","+"interdiction"+"," +"grouper," + objet);}
        // si l'état a été modifié
        if(modif_etat){
            // pour tout les objets groupés
            for(int i =0; i < new Integer(nbObjets) ; i++){
                // on les enlèves de la zone de groupement
                Etat_simulation.enlever(Etat_simulation.stringToObjet(type), Etat_simulation.Contenants.ZONE_GROUPEMENT);
            }
            // si les objets étaient des buchettes et qu'il n'y en avait pas 100, alors on ajoute un groupement de 10
            if(Etat_simulation.stringToObjet(type) == Etat_simulation.Objets.BUCHETTE && new Integer(nbObjets) != 100) Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.ZONE_GROUPEMENT);
            // si les objets étaient des buchettes et qu'il y en avait 100, alors on ajoute un groupement de 100b
            if(Etat_simulation.stringToObjet(type) == Etat_simulation.Objets.BUCHETTE && new Integer(nbObjets) == 100) Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.ZONE_GROUPEMENT);
            // si les objets étaient des groupements de 10, alors on ajoute un groupement de 100
            if(Etat_simulation.stringToObjet(type) == Etat_simulation.Objets.GROUPEMENT10) Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.ZONE_GROUPEMENT);
            // on créer un JSONObject représentant l'état de la simulation
            JSONObject jsonObject = Etat_simulation.etat_Simulation_JSON();
            // on envoi un message correspondant à l'état de la simulation, par l'intermédiaire de MQTT
            MQTT.getInstance().sendMessage("ETAT_SIMULATION", jsonObject.toString());
        }
        // on envois un message correspondant à l'action effectuée, par l'intermédiaire de MQTT
        MQTT.getInstance().sendMessage("ACTIONS", action(d, id, nomExercice, "grouper").toString());
    }

    /**
     * Méthode permettant d'ajouter une trace de l'action défaire un groupement
     * @param groupement le groupement défait
     * @param resultat le résultat de la réalisation de cette action
     * @param modif_etat true si l'état de la simulation a été modifiée
     */
    public static void addActionDefaireGroupement(String groupement, String resultat, boolean modif_etat){
        Date d = new Date();
        // on ajoute une trace de l'action défaire groupement
        traces.add("defaireGroupement" + "," + resultat + "," + groupement);
        // on ajoute que l'élève a voulu grouper
        tracesEleve.add("" + d + "," + nomEleve + "," + nomExercice + "," +"defaireGroupement," + groupement);
        // on ajoute une trace d'interdiction de défaire ce groupement par le système
        if(!resultat.equals("réussi"))tracesEleve.add(""+d+","+ "Système" + "," + nomExercice +","+"interdiction"+"," +"defaireGroupement," + groupement);
        // si l'état de la simulation a été modifié
        if(modif_etat){
            // le nombre d'objet généré
            int nbObjets = 0;
            // si le groupement à défaire était un groupement de 10
            if(Etat_simulation.stringToObjet(groupement) == Etat_simulation.Objets.GROUPEMENT10){
                // on a 10 objets générés
                nbObjets = 10;
                // on enlève le groupement de 10 de la zone de groupement
                Etat_simulation.enlever(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                // pour tous les objets générés
                for(int i =0; i < new Integer(nbObjets) ; i++){
                    // on ajoute ces objets à la zone de groupement
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                }
            }
            // si le groupement à défaire était un groupement de 100
            if(Etat_simulation.stringToObjet(groupement) == Etat_simulation.Objets.GROUPEMENT100){
                // on a 10 objets générés
                nbObjets = 10;
                // on enlève le groupement de 100 de la zone de groupement
                Etat_simulation.enlever(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                // pour tous les objets générés
                for(int i =0; i < new Integer(nbObjets) ; i++){
                    // on ajoute ces objets à la zone de groupement
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                }
            }
            // si le groupement à défaire était un groupement de 100b
            if(Etat_simulation.stringToObjet(groupement) == Etat_simulation.Objets.GROUPEMENT100B){
                // on a 100 objets générés
                nbObjets = 100;
                // on enlève le groupement de 100b de la zone de groupement
                Etat_simulation.enlever(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                // pour tous les objets générés
                for(int i =0; i < new Integer(nbObjets) ; i++){
                    // on ajoute ces objets à la zone de groupement
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.ZONE_GROUPEMENT);
                }
            }
            // on créer un JSONObject représentant l'état de la simulation
            JSONObject jsonObject = Etat_simulation.etat_Simulation_JSON();
            // on envoi un message correspondant à l'état de la simulation, par l'intermédiaire de MQTT
            MQTT.getInstance().sendMessage("ETAT_SIMULATION", jsonObject.toString());
        }
        // on envois un message correspondant à l'action effectuée, par l'intermédiaire de MQTT
        MQTT.getInstance().sendMessage("ACTIONS", action(d, id, nomExercice, "defaire_groupement").toString());
    }

    /**
     * Méthode permettant d'ajouter une trace de l'utilisation du bouton annuler
     * @param action l'action qui a été annulée
     */
    public static void addActionAnnuler(String action){
        Date d = new Date();
        tracesEleve.add("" + d + "," + nomEleve + "," + nomExercice + "," +"annuler," + action);

    }

    /**
     * Méthode permettant d'ajouter une trace de l'évaluation de l'exercice lorsqu'il est terminé
     * @param resultat le résultat le l'évaluation de la réussite de l'exercice terminé
     */
    public static void addTerminerExercice(String resultat){
        Date d = new Date();
        // on ajoute une trace pour signifier que l'élève a fini l'exercice
        tracesEleve.add("" + d + "," + nomEleve + "," + nomExercice + "," +"TermineExercice");
        // on ajoute une trace du système qui évalu la solution que l'élève a apporté à l'exercice
        tracesEleve.add("" + d + "," + "Système" + "," + nomExercice + "," +"évalue" +"," + resultat);
        // JSON object représentant l'état de la simulation (terminée)
        JSONObject object = new JSONObject();
        try{
            // on lui ajoute un attribut "idEleve", ayant pour valeur l'identifiant de l'élève
            object.put("idEleve", Traces.id);
            // on lui ajoute un attribut "code_exercice", ayant pour valeur le code de l'exercice (son nom)
            object.put("code_exercice", Traces.nomExercice);
            // on lui ajoute un attribut "contentType", ayant pour valeur "EXERCICE_TERMINE" pour signifier à l'application de l'enseignant que l'exercice est terminé
            object.put("contentType", "EXERCICE_TERMINE");
        }catch (JSONException e){
            e.printStackTrace();
        }
        // on envoi un message correspondant à l'état de la simulation, par l'intermédiaire de MQTT
        MQTT.getInstance().sendMessage("ETAT_SIMULATION", object.toString());
    }

    /**
     * Méthode permettant d'ajouter un exercice aux traces
     * @param nom le nom de l'exercice
     */
    public static void addExercice(String nom){
        Date d = new Date();
        nomExercice = nom;

    }

    /**
     * Méthode permettant d'ajouter un élève aux traces
     * @param nom le nom de l'élève
     */
    public static void addEleve(String nom){
        Date d = new Date();
        nomEleve = nom;
    }

    /**
     * Méthode permettant d'ajouter une trace de l'utilisation de la zone pour dupliquer
     * @param objet l'objet dupliqué
     */
    public static void addDupliquer(String objet){
        Date d = new Date();
        // on ajoute une trace signifiant que le système a dupliquer des éléments
        tracesEleve.add("" + d + "," + "Système" + "," + nomExercice + "," +"dupliquer" +"," + objet);;
    }

    /**
     * Méthode permettant d'ajouter une trace de la génération d'un popup
     */
    public static void addPopup(){
        Date d = new Date();
        // on ajoute une trace signifiant que le système a généré un popup
        tracesEleve.add("" + d + "," + "Système" + "," + nomExercice + "," +"afficher Popup");;
    }


    /**
     * Méthode permettant de réinitialiser les traces (pour le bouton annuler)
     */
    public static void reset() {
        for (int i = traces.size() - 1; i >= 0; i--) {
            traces.remove(i);
        }
    }

    /**
     * Méthode permettant de réinitialiser les traces (pour l'analyse)
     */
    public static void resetTracesEleve(){
        for(int i = tracesEleve.size()-1; i >=0 ; i--){
            tracesEleve.remove(i);
        }
    }

    /**
     * Méthode permettant de générer un objet JSON pour envoyer l'action réalisée
     * @param d le timestamp de l'action
     * @param idEleve l'id de l'élève qui a effectué l'action
     * @param code_exercice le code de l'exercice où a eu lieu l'action
     * @param typeAction le type d'action qui a été réalisée
     * @return un JSONObject représentant l'action réalisée
     */
    public static JSONObject action(Date d, int idEleve, String code_exercice, String typeAction){
        // JSONObject représentant l'action
        JSONObject action = new JSONObject();
        try {
            // on ajoute un attribut "timestamp", qui a pour valeur le timestamp de l'action
            action.put("timestamp", d);
            // on ajoute un attribut "idEleve", qui a pour valeur l'id de l'élève qui a effectué l'action
            action.put("idEleve", idEleve);
            // on ajoute un attribut "code_exercice", qui a pour valeur le code de l'exercice où a eu lieu l'action
            action.put("code_exercice", code_exercice);
            // on ajoute un attribut "type_action", qui a pour valeur le type d'action qui a été réalisée
            action.put("type_action", typeAction);
            // on ajoute un attribut "type_action", qui a pour valeur le type d'action qui a été réalisée
            action.put("nb_action", "" + Config.nb_actions_de_reference);
        }catch (JSONException a){
            a.printStackTrace();
        }
        return action;
    }
}
