package com.ter.app.applicationtest.Contenants;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ter.app.applicationtest.feedbacks.Alert;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.Buchette;
import com.ter.app.applicationtest.objets.Groupement10;
import com.ter.app.applicationtest.objets.Groupement100;
import com.ter.app.applicationtest.objets.Groupement100B;
import com.ter.app.applicationtest.objets.Groupement3;

import java.util.ArrayList;

/**
 * Classe représentant la zone de groupement.
 */
public class ZoneGroupement extends Contenant  {

    /**
     * Le RelativeLayout associé à la zone de groupement
     */
    private RelativeLayout zone;

    /**
     * L'affichage associé à la zone de groupement
     */
    private TextView affichageMachine;

    /**
     * Le contexte associé à la zone de groupement
     */
    Context c;

    /**
     * Le bouton (contenant une image) permettant de grouper des éléments
     */
    ImageButton grouper;

    /**
     * Le bouton (contenant une image) permettant de défaire des groupements
     */
    ImageButton defaire;

    /**
     * Le nom associé au RelativeLayout de la zone de groupement
     */
    private String nom = "zoneGroupement";

    /**
     * Le dragListener associé à la zone de groupement
     */
    private View.OnDragListener dragListener = new View.OnDragListener() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            boolean onDrag = true;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:

                    break;
                case DragEvent.ACTION_DROP:

                    // DROP DE LA VUE
                    // récupère l'objet déposée
                    View view = (View) event.getLocalState();
                    // si l'objet que l'on veut déposer est un selectAll, la zone de groupement de fait rien
                    if(view.getContentDescription().equals("selectAll"));
                    // sinon, si l'objet que l'on veut déposer n'est pas un selectAll
                    else {
                        // on récupère le parent de la view
                        ViewGroup owner = (ViewGroup) view.getParent();
                        // enlève l'objet de ce parent
                        owner.removeView(view);
                        // on ajoute l'objet à la zone de groupement
                        zone.addView(view);

                        // RÉORGANISATION DES VUES DANS LES CONTENANTS DE FAÇON COHÉRENTE
                        Contenant c;
                        // pour toutes les contenants de l'exercice
                        for (int i = 0; i < ListContenant.getLisContenant().size(); i++) {
                            // on récupère le contenant
                            c = ListContenant.getLisContenant().get(i);
                            // on l'ordonne
                            c.ordonner();
                            // on compte le nombre d'objets contenus et on met à jour l'affichage machine
                            c.countChild();

                        }
                        // on ajoute une trace de déplacement, avec modifiaction de l'état de simulation
                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), Contenant.getNomStatic(zone), "reussi", true);
                        // on précise que le drag est fini
                        onDrag = false;
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return onDrag;
        }
    };

    /**
     * Le clickListener associé au bouton pour grouper les objets
     */
    public OnClickListener actionGrouper = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // on récupère le nombre d'objets contenus dans la zone
            int nbObjet = zone.getChildCount();
            // variable pour le type des objets
            String type ="";
            // le résultat du groupement
            boolean resultat = false;
            // le résulat du test pour savoir si tous les éléments de la zone sont des buchettes
            boolean buchette = true;
            // le résulat du test pour savoir si tous les éléments de la zone sont des groupements de 10
            boolean isgroupement10 = true;
            // pour chaque objets dans la zone
            for(int i = zone.getChildCount()-1; i >= 0; i-- ){
                // on récupère le type des objets (sous la forme : "type1, type2, type 3")
                if(i == zone.getChildCount()){
                    type += zone.getChildAt(i).getContentDescription();

                }
                else type += "," +zone.getChildAt(i).getContentDescription();
                // on vérifie si tous les objets sont des buchettes
                if(!zone.getChildAt(i).getContentDescription().equals("buchette")) buchette= false;
                // on vérifie si tous les objets sont des groupements de 10
                if(!zone.getChildAt(i).getContentDescription().equals("groupement10")) isgroupement10= false;
            }

            // si 3 objets sont dans la zone et que les groupements de 3 sont possibles
      /*       if(nbObjet == 3 && Config.groupement3_possible){
                // pour tous les objets de la zone
                for(int i = zone.getChildCount()-1; i >= 0; i-- ){
                    // on récupère tous les types des objets (sous la forme obj1,obj2,obj3)
                    if(i == zone.getChildCount()){
                        type += zone.getChildAt(i).getContentDescription();
                        obj += zone.getChildAt(i).getContentDescription();
                    }
                    else type += "," +zone.getChildAt(i).getContentDescription();
                    // on vérifie que ce ne sont que des buchettes
                    if(!zone.getChildAt(i).getContentDescription().equals("buchette")) buchette= false;
                }
                // si il n'y avait que des buchettes
                if(buchette) {
                    // on précise que le groupement a réussi
                    resultat = true;
                    // on créer un groupement de 3
                    Groupement3 groupement3 = new Groupement3(c);
                    // on enlève tous les objets de la zone de groupement
                    zone.removeAllViews();
                    // on ajoute le groupement de 3 (sa view)
                    zone.addView(groupement3.getImageView());
                    Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette", resultat);
                }
              //   Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette");
                 // on ajoute une trace de l'action grouper
                // on met à jour l'affichage machine
                countChild();
            }
          /*  if(nbObjet == 3 && !Config.groupement3_possible){
                switch (Config.regle_groupement3_action){
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement3_message_refus);
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement3_message_autorise);
                        boolean buchette = true;
                        // pour tous les objets de la zone
                        for(int i = zone.getChildCount()-1; i >= 0; i-- ){
                            // on récupère tous les types des objets (sous la forme obj1,obj2,obj3)
                            if(i == zone.getChildCount()){
                                type += zone.getChildAt(i).getContentDescription();
                                obj += zone.getChildAt(i).getContentDescription();
                            }
                            else type += "," +zone.getChildAt(i).getContentDescription();
                            // on vérifie que ce ne sont que des buchettes
                            if(!zone.getChildAt(i).getContentDescription().equals("buchette")) buchette= false;
                        }
                        // si il n'y avait que des buchettes
                        if(buchette) {
                            // on précise que le groupement a réussi
                            resultat = true;
                            // on créer un groupement de 3
                            Groupement3 groupement3 = new Groupement3(c);
                            // on enlève tous les objets de la zone de groupement
                            zone.removeAllViews();
                            // on ajoute le groupement de 3 (sa view)
                            zone.addView(groupement3.getImageView());
                            Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette", resultat);

                        }
                        // on ajoute une trace de l'action grouper
               //         Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette");
                        // on met à jour l'affichage machine
                        countChild();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        buchette = true;
                        // pour tous les objets de la zone
                        for(int i = zone.getChildCount()-1; i >= 0; i-- ){
                            // on récupère tous les types des objets (sous la forme obj1,obj2,obj3)
                            if(i == zone.getChildCount()){
                                type += zone.getChildAt(i).getContentDescription();
                                obj += zone.getChildAt(i).getContentDescription();
                            }
                            else type += "," +zone.getChildAt(i).getContentDescription();
                            // on vérifie que ce ne sont que des buchettes
                            if(!zone.getChildAt(i).getContentDescription().equals("buchette")) buchette= false;
                        }
                        // si il n'y avait que des buchettes
                        if(buchette) {
                            // on précise que le groupement a réussi
                            resultat = true;
                            // on créer un groupement de 3
                            Groupement3 groupement3 = new Groupement3(c);
                            // on enlève tous les objets de la zone de groupement
                            zone.removeAllViews();
                            // on ajoute le groupement de 3 (sa view)
                            zone.addView(groupement3.getImageView());
                            Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette", resultat);

                        }
                        // on ajoute une trace de l'action grouper
                   //     Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette");
                        // on met à jour l'affichage machine
                        countChild();
                        break;
                }
            }
            */
            // si il y a 10 objets dans la zone de groupement et que le groupement de 10 ou que le groupement de 100 est possible
            if(nbObjet == 10 && (Config.groupement10_possible || Config.groupement100_possible)){
                // si tous les objets étaient des buchettes et que le groupement de 10 est possible
                if(buchette && Config.groupement10_possible) {
                    // on précise que l'on a pu faire le groupement
                    resultat = true;
                    // on créer un groupement de 10
                    Groupement10 groupement10 = new Groupement10(c);
                    // on enlève tous les objets de la zone
                    zone.removeAllViews();
                    // on ajoute le groupement de 10 (sa view)
                    zone.addView(groupement10.getImageView());
                    // on ajoute une trace de l'action grouper avec modification de l'état de simulation
                    Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchette", true);

                }
                // si tous les objets étaient des groupements de 10 et que le groupement de 100 est possible
                if(isgroupement10 && Config.groupement100_possible){
                    // on précise que l'on a pu faire le groupement
                    resultat = true;
                    // on créer un groupement de 100
                    Groupement100 groupement100 = new Groupement100(c);
                    // on enlève tous les objets de la zone
                    zone.removeAllViews();
                    // on ajoute le groupement de 10 (sa view)
                    zone.addView(groupement100.getImageView());
                    // on ajoute une trace de l'action grouper avec modification de l'état de simulation
                    Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "groupement10", true);

                }
                // on met à jour l'affichage
                countChild();
            }
            // si le nombre d'objet est de 10 mais que le groupement de 10 ou le groupement de 100 ne sont pas possible
            if(nbObjet == 10 &&( !Config.groupement10_possible || !Config.groupement100_possible)){
                // si tous les objets étaient des buchettes et que le groupement de 10 n'est pas possible
                if(buchette && !Config.groupement10_possible) {
                    // selon le type de rétroaction configurée
                    switch (Config.regle_groupement10_action){
                        // si l'action est refusée et que l'on veut afficher un message
                        case REFUS_ET_MESSAGE:
                            // on crée le message voulu
                            Alert a = new Alert(c, Config.regle_groupement10_message_refus_si_non_autorise);
                            // et on l'affiche
                            Traces.addPopup();
                            a.show();
                            break;
                        // si l'action est refusée et que l'on ne veut rien afficher
                        case REFUS_ACTION:
                            // on ne fait rien
                            break;
                        // si l'action est autorisée et que l'on veut afficher un message
                        case AUTORISATION_ET_MESSAGE:
                            // on crée le message voulu
                            a = new Alert(c, Config.regle_groupement10_message_autorise_si_non_autorise);
                            // on précise que l'on a pu faire le groupement
                            resultat = true;
                            // on créer un groupement de 10
                            Groupement10 groupement10 = new Groupement10(c);
                            // on enlève tous les objets de la zone
                            zone.removeAllViews();
                            // on ajoute le groupement de 10 (sa view)
                            zone.addView(groupement10.getImageView());
                            // on ajoute une trace de l'action grouper et que l'on a modifié l'état de la simulation
                            Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchettes", true);
                            // on met à jour l'affichage machine
                            countChild();
                            // on affiche le message
                            Traces.addPopup();
                            a.show();
                            break;
                        // si l'action est autorisée mais que l'on ne veut pas afficher un message
                        case AUTORISATION_ACTION:
                            // on précise que l'on a pu faire le groupement
                            resultat = true;
                            // on créer un groupement de 10
                            groupement10 = new Groupement10(c);
                            // on enlève tous les objets de la zone
                            zone.removeAllViews();
                            // on ajoute le groupement de 10 (sa view)
                            zone.addView(groupement10.getImageView());
                            // on ajoute une trace de l'action grouper et que l'on a modifié l'état de la simulation
                            Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchette", true);
                            // on met à jour l'affichage machine
                            countChild();
                            break;
                    }

                }
                // si tous les objets étaient des groupements de 10 et que le groupement de 100 n'est pas possible
                if(isgroupement10 && !Config.groupement100_possible){
                    // selon le type de rétroaction configurée
                    switch (Config.regle_groupement100_action){
                        // même principe que pour la condition précédente : "si tous les objets étaient des buchettes et que le groupement de 10 n'est pas possible"
                        case REFUS_ET_MESSAGE:
                            Traces.addPopup();
                            Alert a = new Alert(c, Config.regle_groupement100_message_refus_si_non_autorise);
                            a.show();
                            break;
                        case REFUS_ACTION:
                            break;
                        case AUTORISATION_ET_MESSAGE:
                            a = new Alert(c, Config.regle_groupement100_message_autorise_si_non_autorise);
                            // on précise que l'on a pu faire le groupement
                            resultat = true;
                            // on créer un groupement de 100
                            Groupement100 groupement100 = new Groupement100(c);
                            // on enlève tous les objets de la zone
                            zone.removeAllViews();
                            // on ajoute le groupement de 10 (sa view)
                            zone.addView(groupement100.getImageView());
                            // on ajoute une trace de l'action grouper et que l'on a modifié l'état de la simulation
                            Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", true);
                            // on met à jour l'affichage machine
                            countChild();
                            Traces.addPopup();
                            a.show();
                            break;
                        case AUTORISATION_ACTION:
                            // on précise que l'on a pu faire le groupement
                            resultat = true;
                            // on créer un groupement de 10
                            groupement100 = new Groupement100(c);
                            // on enlève tous les objets de la zone
                            zone.removeAllViews();
                            // on ajoute le groupement de 10 (sa view)
                            zone.addView(groupement100.getImageView());
                            // on ajoute une trace de l'action grouper et que l'on a modifié l'état de la simulation
                            Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", true);
                            // on met à jour l'affichage machine
                            countChild();
                            break;
                    }
                }


            }
            // si le nombre d'objets est de 100 et qu'il ny avait que des buchettes, et que l'on veut faire un groupement de 100 buchettes
            if(nbObjet == 100 && buchette && Config.groupement_simple){
                // on précise que l'on a pu faire le groupement
                resultat = true;
                // on créer un groupement de 100
                Groupement100B groupement100 = new Groupement100B(c);
                // on enlève tous les objets de la zone
                zone.removeAllViews();
                // on ajoute le groupement de 10 (sa view)
                zone.addView(groupement100.getImageView());
                // on ajoute aux traces que l'on a fait un groupement et que l'on a modifié l'état de la simulation
                Traces.addActionGrouper("" + nbObjet, type, "" + resultat,"buchettes", true);
                // on met à jour l'affichage
                countChild();

            }
            if((nbObjet >=50  && nbObjet <100 )&& buchette && Config.groupement_simple){
                // selon le type de rétroaction configurée
                switch (Config.regle_groupement100b_action) {
                    // même principe que pour la condition précédente : "si tous les objets étaient des buchettes et que le groupement de 10 n'est pas possible"
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement100b_message_refus_si_nb_inf);
                        Traces.addPopup();
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement100b_message_autorise_si_nb_inf);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement100B groupement100 = new Groupement100B(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute aux traces que l'on a fait un groupement et que l'on a modifié l'état de la simulation
                        Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchettes", true);
                        // on met à jour l'affichage
                        countChild();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        groupement100 = new Groupement100B(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute aux traces que l'on a fait un groupement et que l'on a modifié l'état de la simulation
                        Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchettes", true);
                        // on met à jour l'affichage
                        countChild();
                        break;
                }


            }
            if(nbObjet > 100 && buchette && Config.groupement_simple) {
                // selon le type de rétroaction configurée
                switch (Config.regle_groupement100b_action) {
                    // même principe que pour la condition précédente : "si tous les objets étaient des buchettes et que le groupement de 10 n'est pas possible"
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement100b_message_refus_si_nb_sup);
                        Traces.addPopup();
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement100b_message_autorise_si_nb_sup);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement100B groupement100 = new Groupement100B(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute aux traces que l'on a fait un groupement et que l'on a modifié l'état de la simulation
                        Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchettes", true);
                        // on met à jour l'affichage
                        countChild();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        groupement100 = new Groupement100B(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute aux traces que l'on a fait un groupement et que l'on a modifié l'état de la simulation
                        Traces.addActionGrouper("" + nbObjet, type, "" + resultat, "buchettes", true);
                        // on met à jour l'affichage
                        countChild();
                        break;
                }
            }
            // si les objets sont tous des buchettes mais qu'il y a moins de 10 objets
            if(buchette && nbObjet <10){
                // selon le type de rétroaction configurée
                switch (Config.regle_groupement10_action){
                    // même principe que pour la condition précédente : "si tous les objets étaient des buchettes et que le groupement de 10 n'est pas possible"
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement10_message_refus_si_nb_inf);
                        Traces.addPopup();
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement10_message_autorise_si_nb_inf);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement10 groupement10 = new Groupement10(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement10.getImageView());
                        // on ajoute une trace de l'action grouper, et que l'on a modifié l'état de la simulation
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchette", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        Traces.addPopup();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 10
                        groupement10 = new Groupement10(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement10.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchette", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        break;
                }
            }

            // si tous les objets sont des buchettes et que l'on en a plus que 10
            if(buchette && nbObjet >10 && nbObjet <50){
                // même principe que pour la condition précédente : "si les objets sont tous des buchettes mais qu'il y a moins de 10 objets"
                switch (Config.regle_groupement10_action){
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement10_message_refus_si_nb_sup);
                        Traces.addPopup();
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement10_message_autorise_si_nb_sup);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement10 groupement10 = new Groupement10(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement10.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchette", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        Traces.addPopup();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 10
                        groupement10 = new Groupement10(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement10.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"buchette", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        break;
                }
            }
            // si les objets sont tous des groupements de 10 mais qu'il y en a moins que 10
            if(isgroupement10 && nbObjet <10){
                // même principe que pour la condition précédente : "si les objets sont tous des buchettes mais qu'il y a moins de 10 objets"
                switch (Config.regle_groupement100_action){
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement100_message_refus_si_nb_inf);
                        Traces.addPopup();
                        a.show();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement100_message_autorise_si_nb_inf);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement100 groupement100 = new Groupement100(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        Traces.addPopup();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 10
                        groupement100 = new Groupement100(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        break;
                }

            }
            // si tous les objets sont des groupements de 10 mais qu'il y en a plus que 10
            if(isgroupement10 && nbObjet >10){
                // même principe que pour la condition précédente : "si les objets sont tous des buchettes mais qu'il y a moins de 10 objets"
                switch (Config.regle_groupement100_action){
                    case REFUS_ET_MESSAGE:
                        Alert a = new Alert(c, Config.regle_groupement100_message_refus_si_nb_sup);
                        a.show();
                        Traces.addPopup();
                        break;
                    case REFUS_ACTION:
                        break;
                    case AUTORISATION_ET_MESSAGE:
                        a = new Alert(c, Config.regle_groupement100_message_autorise_si_nb_sup);
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 100
                        Groupement100 groupement100 = new Groupement100(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        Traces.addPopup();
                        a.show();
                        break;
                    case AUTORISATION_ACTION:
                        // on précise que l'on a pu faire le groupement
                        resultat = true;
                        // on créer un groupement de 10
                        groupement100 = new Groupement100(c);
                        // on enlève tous les objets de la zone
                        zone.removeAllViews();
                        // on ajoute le groupement de 10 (sa view)
                        zone.addView(groupement100.getImageView());
                        // on ajoute une trace de l'action grouper
                        Traces.addActionGrouper(""+nbObjet,type,""+resultat,"groupement10", resultat);
                        // on met à jour l'affichage machine
                        countChild();
                        break;
                }

            }

            // si le groupement n'a pas été fait
            if(!resultat){
                // on récupère le type de tous les objets sous la forme :"[type1, type2]"
                String objets = ",[";
                for(int i = 0; i < nbObjet ; i++){
                    if (i == 0) objets += zone.getChildAt(i).getContentDescription().toString();
                    else objets += ", " + zone.getChildAt(i).getContentDescription().toString();
                }
                objets += "]";
                // on ajoute aux traces que l'élève à tenté de faire le groupement mais qu'il n'a pas réussi et que l'état de la simulation n'a pas changé
                Traces.addActionGrouper("" + nbObjet, objets, "false","buchette", false);
            }
        }
    };


    /**
     * Le clickListener associé au bouton pour défaire les groupements
     */
    public OnClickListener actionDefaire = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // on récupère le nombre d'objets dans la zone
            int nbObjet = zone.getChildCount();
            // pour récupérer le type de groupement
            String groupement = "";
            // si il n'y a qu'un objet dans la zone
            if (nbObjet == 1) {
                // on récupère le type de groupement
                groupement = zone.getChildAt(zone.getChildCount() - 1).getContentDescription().toString();
                // si c'est un groupement de 3
                if (groupement.equals("groupement3")) {
                    // on ajoute une trace du fait de défaire le groupement, et que l'on a modifié l'état de la simulation
                    Traces.addActionDefaireGroupement(groupement, "réussi",true);
                    // on enlève le groupement de la zone
                    zone.removeViewAt(zone.getChildCount()-1);
                    // on créer 3 buchettes que l'on ajoute à la zone
                    Buchette b;
                    for(int i=0; i<3 ; i++){
                        b = new Buchette(c);
                        zone.addView(b.getImageView());
                    }
                    // on ordonne la zone
                    ordonner();
                }
                // si le groupement est un groupement de 10
                if (groupement.equals("groupement10")) {
                    // on ajoute une trace du fait de défaire le groupement, et que l'on a modifié l'état de la simulation
                    Traces.addActionDefaireGroupement(groupement, "réussi", true);
                    // on enlève le groupement de la zone
                    zone.removeViewAt(zone.getChildCount()-1);
                    // on créer 10 buchettes que l'on ajoute à la zone
                    Buchette b;
                    for(int i=0; i<10 ; i++){
                        b = new Buchette(c);

                        zone.addView(b.getImageView());
                    }
                    // on ordonne la zone
                    ordonner();
                }
                // si le groupement est un groupement de 100
                if (groupement.equals("groupement100")) {
                    // on ajoute une trace du fait de défaire le groupement, et que l'on a modifié l'état de la simulation
                    Traces.addActionDefaireGroupement(groupement, "réussi", true);
                    // on enlève le groupement de la zone
                    zone.removeViewAt(zone.getChildCount()-1);
                    // on créer 10 groupements de 10 que l'on ajoute à la zone
                    Groupement10 g;
                    for(int i=0; i<10 ; i++){
                        g = new Groupement10(c);
                        zone.addView(g.getImageView());
                    }
                    // on ordonne la zone
                    ordonner();
                }
                if (groupement.equals("groupement100b")) {
                    // on ajoute une trace du fait de défaire le groupement, et que l'on a modifié l'état de la simulation
                    Traces.addActionDefaireGroupement(groupement, "réussi", true);
                    // on enlève le groupement de la zone
                    zone.removeViewAt(zone.getChildCount()-1);
                    Buchette b;
                    for(int i=0; i<100 ; i++){
                        b = new Buchette(c);

                        zone.addView(b.getImageView());
                    }
                    // on ordonne la zone
                    ordonner();
                }
                // sinon on ajoute une trace du faite que l'on a pas réussi à défaire le groupement et que l'état de la simulation n'a pas été modifiée
                if (!groupement.equals("groupement3")&& !groupement.equals("groupement10") && !groupement.equals("groupement100") && !groupement.equals("groupement100b"))Traces.addActionDefaireGroupement(groupement, "echec", false);
                // on met à jour l'affichage machine
                countChild();
            }
            // sinon, si il y a plus que 1 objet présent dans la zone de groupement
            else {
                // on récupère le type de tous les objets de la zone
                String objets = "[";
                for(int i = 1; i < nbObjet ; i++){
                    if (i == 1) objets += zone.getChildAt(i - 1).getContentDescription().toString();
                    else objets += ", " + zone.getChildAt(i - 1).getContentDescription().toString();
                }
                objets += "]";
                //on ajoute une trace du faite que l'on a pas réussi à défaire ces objets et que l'on a pas modifié l'état de la simulation
                Traces.addActionDefaireGroupement(objets, "echec", false);
            }
        }

    };

    /**
     * Constructeur de la zone de groupement
     * @param r le RelativeLayout associé à la zone de groupement
     * @param c le contexte associé à la zone de groupement
     * @param t la zone d'affichage pour la zone de groupement
     * @param g le bouton permettant de faire un groupement
     * @param d le bouton permettant de défaire un groupement
     */
    public ZoneGroupement(RelativeLayout r, Context c, TextView t, ImageButton g, ImageButton d){
        super(c);
        // on récupère le contexte associé à la zone de groupement
        this.c = c;
        // on affecte le RelativeLayout correspondant à la boite
        zone = r;
        // on précise que le RelativeLayout est celui de la zone de groupement
        zone.setContentDescription(nom);
        // on ajoute un dragListener à la boite (pour permettre le drop)
        zone.setOnDragListener(dragListener);
        // on affecte le TextView correspondant à l'affichage machine
        affichageMachine = t;
        // on met à jour l'affichage
        countChild();
        // on récupère les boutons
        grouper = g;
        defaire = d;
        // on ajoute les clickListener correspondants
        grouper.setOnClickListener(actionGrouper);
        defaire.setOnClickListener(actionDefaire);
    }


    @Override
    /**
     * Méthode permettant d'ordonner la zone de groupement
     */
    public void ordonner() {
        ArrayList<View> listBuchettes = new ArrayList<>();
        // pour tous les objets déposés dans la table
        for(int i = 0; i < zone.getChildCount(); i++){
            // on ajoute l'objet dans la liste
            listBuchettes.add(zone.getChildAt(i));
        }
        // pour tous les objets déposés dans la table
        for(int j = listBuchettes.size() -1 ; j >= 0 ; j --){
            // on les enlèves de la table
            zone.removeViewAt(j);
        }
        // pour tous les objets déposés dans la table
        for(int i = 0; i < listBuchettes.size(); i++){
            // on récupère l'objet dans la liste
            View view = listBuchettes.get(i);
            // on modifie sa position selon le nombre d'objets déja déposés dans la table
            zone.addView(view);
            // on ordonne la zone selon le nombre d'objets et selon leur taille
            if(countChild() <= 5 ){
                view.setX(Parametres.X * (zone.getChildCount() -1));
                view.setY(0f);
            }
            else {
                if(countChild() <=  10 ) {
                    if (Config.taille == 2) view.setY(Parametres.Y_2L_T2);
                    if (Config.taille == 3) view.setY(Parametres.Y_2L_T3);
                    view.setX(Parametres.X * (zone.getChildCount() - 6));
                }
                else{
                    if (Config.taille == 2) view.setY(Parametres.Y_3L_T2);
                    if (Config.taille == 3) view.setY(Parametres.Y_3L_T3);
                    view.setX(Parametres.X * (zone.getChildCount() - 11));
                }
            }
        }
    }

    @Override
    /**
     * Méthode permettant de compter le nombre d'objets dans la zone et de mettre à jour l'affichage
     * @return le nombre d'objets de la zone
     */
    public int countChild() {
        affichageMachine.setText("" + zone.getChildCount());
        return (zone.getChildCount());
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de buchettes contenues dans la zone de groupement dépasse le nombre maximal de buchettes autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de buchettes dans la zone de groupement
     */
    public boolean depasseMaxBuchette() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 10 contenus dans la zone de groupement dépasse le nombre maximal de groupements de 10 autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupement de 10 dans la zone de groupement
     */
    public boolean depasseMaxGroupement10() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100 contenus dans la zone de groupement dépasse le nombre maximal de groupements de 10 autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupement de 100 dans la zone de groupement
     */
    public boolean depasseMaxGroupement100() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100b contenus dans la zone de groupement dépasse le nombre maximal de groupements de 10 autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupement de 100b dans la zone de groupement
     */
    public boolean depasseMaxGroupement100b() {
        return false;
    }


    @Override
    /**
     * Méthode permettant de récupérer le nom associé au RelativeLayout de la zone de groupement
     * @return le nom associé au RelativeLayout de la zone
     */
    public String getNom() {
        return nom;
    }


    @Override
    /**
     * Méthode permettant de récupérer le RelativeLayout associé à la zone de groupement
     * @return le RelativeLayout associé à la zone
     */
    public RelativeLayout getRelativeLayout() {
        return zone;
    }

}
