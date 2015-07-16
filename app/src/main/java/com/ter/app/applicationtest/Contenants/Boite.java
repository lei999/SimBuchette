package com.ter.app.applicationtest.Contenants;

import android.annotation.TargetApi;

import android.content.ClipData;
import android.content.Context;

import android.os.Build;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ter.app.applicationtest.feedbacks.AffichageMachine;
import com.ter.app.applicationtest.feedbacks.Etat_simulation;
import com.ter.app.applicationtest.objets.Buchette;
import com.ter.app.applicationtest.objets.Groupement10;
import com.ter.app.applicationtest.objets.Groupement100;
import com.ter.app.applicationtest.objets.Groupement100B;
import com.ter.app.applicationtest.parametresExercices.Config;
import com.ter.app.applicationtest.R;
import com.ter.app.applicationtest.feedbacks.Alert;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.ListObjets;

import java.util.ArrayList;

/**
 * Classe représentant la "boite" où l'on dépose des objets (bûchettes, …).
 */
public class Boite extends Contenant {

    /**
     * RelativeLayout dans le xml, correspondant à la boite.
     */
    private RelativeLayout boite;

    /**
     * l'affichage du nom de la boite
     */
    private TextView affichageNom;


    /**
     * Attribut signalant si l'utilisateur à séléctionné la boite
     */
    private boolean selected = false;

    /**
     * Le nom de la boite
     */
    private String nom;

    /**
     * L'image associée au bouton vider de a boite
     */
    private ImageButton boutonSelectAll;

    /**
     * Le contexte associé à la boite
     */
    Context c;

    /**
     * Le nombre maximal de buchette autorisé dans la boite
     */
    int nbmaxBuchette;

    /**
     * La rétroaction a effectuer lorsque le nombre de buchettes dans la boite dépasse le nombre de buchettes autorisé
     */
    public Alert.Action actionbuchette;

    /**
     * Le message à afficher lorsque le nombre de buchettes dans la boite dépasse le nombre de buchettes autorisé
     */
    public Alert popupBuchette;
    public Alert popupBuchette_refus;

    /**
     * Le nombre maximal de groupement de 10 autorisé dans la boite
     */
    int nbmaxGroupement10;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 10 dans la boite dépasse le nombre de groupement de 10 autorisé
     */
    public Alert.Action actiongroupement10;

    /**
     * Le message à afficher lorsque le nombre de groupement de 10 dans la boite dépasse le nombre de groupement de 10 autorisé
     */
    public Alert popupGroupement10;
    public Alert popupGroupement10_refus;

    /**
     * Le nombre maximal de groupement de 100 autorisé dans la boite
     */
    int nbmaxGroupement100;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 100 dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    public Alert.Action actiongroupement100;

    /**
     * Le message à afficher lorsque le nombre de groupement de 100 dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    public Alert popupGroupement100;
    public Alert popupGroupement100_refus;

    /**
     * Le nombre maximal de groupement de 100b autorisé dans la boite
     */
    int nbmaxGroupement100b;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 100b dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    public Alert.Action actiongroupement100b;

    /**
     * Le message à afficher lorsque le nombre de groupement de 100b dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    public Alert popupGroupement100b;
    public Alert popupGroupement100b_refus;

    /**
     * L'affichage des éléments associé à la boite (affichage machine)
     */
    AffichageMachine affichageElements;

    /**
     * Attribut signalant si l'application doit afficher l'affichage machine
     */
    private boolean showAffichageElements = false;

    private boolean afficher_cardianalite_totale = false;

    int num;



    /**
     * Le clickListener associé au bouton sélectionner tout de la boite
     */
     private OnClickListener selectAll = new OnClickListener() {
        @Override
        public void onClick(View v) {
            selectAll();
        }
    };

    /**
     * Le dragListener de la boite
     */
    private View.OnDragListener dragListener = new View.OnDragListener() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            boolean onDrag =  true;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // récupère l'objet déposée
                    View view = (View) event.getLocalState();
                    // on récupère le type d'objet qui est déposé ( écrit dans la desciption de la vue)
//                    String type = view.getContentDescription().toString();
                    // récupère le parent de la vue
                    ViewGroup owner = (ViewGroup) view.getParent();
                    // variable permettant de savoir si le dépot a été effectué
                    boolean depot = false;
                    // si la view déposée correspond à la séléction de tous les éléments
                    if(view.getContentDescription().equals("selectAll")){
                        // on retire la view de la boite qui la contenait
                        owner.removeView(view);
                        // pour tous les objets qui ont été selectionnés
                        for (int i = 0; i < ListObjets.listView.size(); i++) {
                            // on récupére l'objet
                            View tmp = ListObjets.listView.get(i);
                            // on le rend visible
                            tmp.setVisibility(VISIBLE);
                            // on l'enlève de la boite où il se trouvait
                            owner.removeView(tmp);
                            // on l'ajoute à cette boite
                            boite.addView(tmp);
                            Log.d("d1", tmp.getContentDescription().toString());
                            // on ajoute une trace de déplacement (en attente de vérification si l'on peut déposer tout ces objets)
                            Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "attente", true);
                        }

                        // si l'on ne dépsse aucun nombre d'objet autorisé
                        if(!depasseMaxBuchette() && !depasseMaxGroupement10() && !depasseMaxGroupement100() && !depasseMaxGroupement100b()){
                            depot = true;
                            // on ajoute une trace de déplacement pour chaque objet
                            for (int i = 0; i < ListObjets.listView.size(); i++) {
                                // on récupére l'objet
                                View tmp = ListObjets.listView.get(i);
                                Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                            }
                            // on vide la liste des objets seléctionnés
                            ListObjets.removeAll();
                        }

                        // on teste si l'on a dépassé le nombre de buchettes autorisé
                        if (depasseMaxBuchette()){
                            // selon la rétroaction associée au dépassement du nombre de buchettes autorisées
                            switch (actionbuchette){
                                // si l'action est autorisée (et sans affichage de message)
                                case AUTORISATION_ACTION:
                                    depot = true;
                                    // pout tous les objets seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action autorisée avec l'affichage d'un message
                                case AUTORISATION_ET_MESSAGE:
                                    depot = true;
                                    // si le message a afficher lorsque le nombre de buchettes déposées dépasse le nombre maximal de buchettes autorisées existe alors il l'affiche
                                    if(popupBuchette != null) popupBuchette.show();
                                    // pour tous les objets seléctionnés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    Traces.addPopup();
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action est refusée (et sans affichage de message)
                                case REFUS_ACTION:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    break;
                                // si l'action est refusée avec l'affichage d'un message
                                case REFUS_ET_MESSAGE:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    // si le message existe, on l'affiche
                                    if(popupBuchette_refus != null) popupBuchette_refus.show();
                                    Traces.addPopup();
                                    break;
                            }
                        }

                        if (depasseMaxGroupement10()){
                            // on teste si l'on a dépassé le nombre de groupement de 10 autorisé, on fait la même chose que pour les buchettes mais avec les règles pour les groupements de 10
                            switch (actiongroupement10){
                                // si l'action est autorisée (et sans affichage de message)
                                case AUTORISATION_ACTION:
                                    depot = true;
                                    // pout tous les objets seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action autorisée avec l'affichage d'un message
                                case AUTORISATION_ET_MESSAGE:
                                    depot = true;
                                    // si le message a afficher lorsque le nombre de buchettes déposées dépasse le nombre maximal de buchettes autorisées existe alors il l'affiche
                                    if(popupGroupement10 != null) popupGroupement10.show();
                                    // pour tous les objets seléctionnés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    Traces.addPopup();
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action est refusée (et sans affichage de message)
                                case REFUS_ACTION:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    break;
                                // si l'action est refusée avec l'affichage d'un message
                                case REFUS_ET_MESSAGE:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    // si le message existe, on l'affiche
                                    if(popupGroupement10_refus != null) popupGroupement10_refus.show();
                                    Traces.addPopup();
                                    break;
                            }
                        }
                        // on teste si l'on a dépassé le nombre de groupement de 100 autorisé,
                        // on fait la même chose que pour les buchettes mais avec les règles pour les groupements de 100

                        if (depasseMaxGroupement100()){
                            switch (actiongroupement100){
                                // si l'action est autorisée (et sans affichage de message)
                                case AUTORISATION_ACTION:
                                    depot = true;
                                    // pout tous les objets seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action autorisée avec l'affichage d'un message
                                case AUTORISATION_ET_MESSAGE:
                                    depot = true;
                                    // si le message a afficher lorsque le nombre de buchettes déposées dépasse le nombre maximal de buchettes autorisées existe alors il l'affiche
                                    if(popupGroupement100 != null) popupGroupement100.show();
                                    // pour tous les objets seléctionnés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    Traces.addPopup();
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action est refusée (et sans affichage de message)
                                case REFUS_ACTION:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    break;
                                // si l'action est refusée avec l'affichage d'un message
                                case REFUS_ET_MESSAGE:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    // si le message existe, on l'affiche
                                    if(popupGroupement100_refus != null) popupGroupement100_refus.show();
                                    Traces.addPopup();
                                    break;
                            }
                        }
                        // on teste si l'on a dépassé le nombre de groupement de 100b autorisé,
                        // on fait la même chose que pour les buchettes mais avec les règles pour les groupements de 100b
                        if (depasseMaxGroupement100b()){
                            switch (actiongroupement100b){
                                // si l'action est autorisée (et sans affichage de message)
                                case AUTORISATION_ACTION:
                                    depot = true;
                                    // pout tous les objets seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action autorisée avec l'affichage d'un message
                                case AUTORISATION_ET_MESSAGE:
                                    depot = true;
                                    // si le message a afficher lorsque le nombre de buchettes déposées dépasse le nombre maximal de buchettes autorisées existe alors il l'affiche
                                    if(popupGroupement100b != null) popupGroupement100b.show();
                                    // pour tous les objets seléctionnés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        View tmp = ListObjets.listView.get(i);
                                        // on ajoute une trace de déplacement réussi dans la boite
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", false);
                                    }
                                    Traces.addPopup();
                                    // on vide la liste des objets seléctionnés
                                    ListObjets.removeAll();
                                    break;
                                // si l'action est refusée (et sans affichage de message)
                                case REFUS_ACTION:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    break;
                                // si l'action est refusée avec l'affichage d'un message
                                case REFUS_ET_MESSAGE:
                                    // on précise que le dépot ne doit pas être effectué
                                    depot = false;
                                    // pour tous les objets qui ont été seléctionés
                                    for (int i = 0; i < ListObjets.listView.size(); i++) {
                                        // on récupère la view
                                        View tmp = ListObjets.listView.get(i);
                                        // on la rend invisible
                                        tmp.setVisibility(INVISIBLE);
                                        // on l'enlève de cette boite
                                        boite.removeView(tmp);
                                        // on la remet dans sa boite d'origine
                                        owner.addView(tmp);
                                        // on ajoute une trace signalant que le dépot a été interdit
                                        Traces.addDeplacement(tmp.getContentDescription().toString(), nom, Contenant.getNomStatic((RelativeLayout) owner), "echec", true);
                                    }
                                    // remet la view "selectAll" dans la boite d'origine
                                    owner.addView(view);
                                    // si le message existe, on l'affiche
                                    if(popupGroupement100b_refus != null) popupGroupement100b_refus.show();
                                    Traces.addPopup();
                                    break;
                            }
                        }


                        // pour tous les contenants de l'exercice
                        ArrayList<Contenant> listContenant = ListContenant.getLisContenant();
                        for (int i = 0; i < listContenant.size(); i++) {
                            // on récupère le contenant
                            Contenant c = listContenant.get(i);
                            // on l'ordonne
                            c.ordonner();
                            // si l'on a déposer les objets , on compte le nombre d'objets contenus et on met à jour l'affichage machine
                           if(depot) c.countChild();
                           // on déselctionne les contenants
                           if(depot) c.unSelected();

                        }
                        // si l'on n'a pas déposé les objets on met à jour l'affichage machine de la boite
                        if(!depot)countChild();
                        // on précise que le dépot a réussi
                        onDrag = false;

                    }
                    // si l'objet déplacé n'est pas un "selectALL", c-a-d que l'on ne veut déplacer qu'une view
                    else {

                        if (!selected) {
                            String[] typeAcc = getlistObjetAcceptesString();
                            // on enlève l'objet de son parent
                            owner.removeView(view);
                            // on ajoute l'objet à la boite
                            boite.addView(view);
                            // si l'on ne dépasse pas le nombre maximal de chaque objet
                            if(!depasseMaxBuchette() && !depasseMaxGroupement10() && !depasseMaxGroupement100() && ! depasseMaxGroupement100b()){
                                // on ajoute une trace de déplacement réussi
                                Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                            }
                            // si on dépasse le nombre maximal de buchettes autorisé
                            if (depasseMaxBuchette()){
//                                if(num == 2){
//                                    switch (Config.boite2_action_depassement_buchette){
//                                        // si l'action est autorisée (et sans affichage de message)
//                                        case AUTORISATION_ACTION:
//                                            Log.d("deplacement", "autorisation action");
//                                            // on ajoute une trace de déplacement
//                                            Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
//                                            break;
//                                        // si l'action est autorisée avec l'affichage d'un message
//                                        case AUTORISATION_ET_MESSAGE:
//                                            // si le message existe on l'affiche
//                                            if(popupBuchette != null) popupBuchette.show();
//                                            // on ajoute une trace de déplacement
//                                            Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
//                                            Traces.addPopup();
//                                            break;
//                                        // si l'action est refusée (et sans affichage de message)
//                                        case REFUS_ACTION:
//                                            // on enlève l'objet de cette boite
//                                            boite.removeView(view);
//                                            // on remet l'objet dans son parent
//                                            owner.addView(view);
//                                            // on ajoute une trace de déplacement échoué
//                                            Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
//                                            break;
//                                        // si l'action est refusée avec l'affichage d'un message
//                                        case REFUS_ET_MESSAGE:
//                                            // on enlève l'objet de cette boite
//                                            boite.removeView(view);
//                                            // on remet l'objet dans son parent
//                                            owner.addView(view);
//                                            // si le message existe on l'affiche
//                                            if(popupBuchette != null) popupBuchette.show();
//                                            // on ajoute une trace de déplacement
//                                            Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
//                                            Traces.addPopup();
//                                            break;
//                                    }
//                                }
//                            }
                                // selon le type de rétroaction
                                switch (actionbuchette){
                                    // si l'action est autorisée (et sans affichage de message)
                                   case AUTORISATION_ACTION:
                                       Log.d("deplacement", "autorisation action");
                                       // on ajoute une trace de déplacement
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        break;
                                    // si l'action est autorisée avec l'affichage d'un message
                                    case AUTORISATION_ET_MESSAGE:
                                        // si le message existe on l'affiche
                                        if(popupBuchette != null) popupBuchette.show();
                                        // on ajoute une trace de déplacement
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        Traces.addPopup();
                                        break;
                                    // si l'action est refusée (et sans affichage de message)
                                    case REFUS_ACTION:
                                        // on enlève l'objet de cette boite
                                        boite.removeView(view);
                                        // on remet l'objet dans son parent
                                        owner.addView(view);
                                        // on ajoute une trace de déplacement échoué
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        break;
                                    // si l'action est refusée avec l'affichage d'un message
                                    case REFUS_ET_MESSAGE:
                                        // on enlève l'objet de cette boite
                                        boite.removeView(view);
                                        // on remet l'objet dans son parent
                                        owner.addView(view);
                                        // si le message existe on l'affiche
                                        if(popupBuchette_refus != null) popupBuchette_refus.show();
                                        // on ajoute une trace de déplacement
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        Traces.addPopup();
                                        break;
                                }
                            }

                            // même chose que pour le dépassement du nombre de buchette autorisé mais avec des groupements de 10
                            if (depasseMaxGroupement10()){
                                switch (actiongroupement10){
                                    case AUTORISATION_ACTION:
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        break;
                                    case AUTORISATION_ET_MESSAGE:
                                        if(popupGroupement10 != null) popupGroupement10.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        Traces.addPopup();
                                        break;
                                    case REFUS_ACTION:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        break;
                                    case REFUS_ET_MESSAGE:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        if(popupGroupement10_refus != null) popupGroupement10_refus.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        Traces.addPopup();
                                        break;
                                }
                            }
                            // même chose que pour le dépassement du nombre de buchette autorisé mais avec des groupements de 100
                            if (depasseMaxGroupement100()){
                                switch (actiongroupement100){
                                    case AUTORISATION_ACTION:
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        break;
                                    case AUTORISATION_ET_MESSAGE:
                                        if(popupGroupement100 != null) popupGroupement100.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        Traces.addPopup();
                                        break;
                                    case REFUS_ACTION:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        break;
                                    case REFUS_ET_MESSAGE:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        if(popupGroupement100_refus != null) popupGroupement100_refus.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        Traces.addPopup();
                                        break;
                                }
                            }
                            // même chose que pour le dépassement du nombre de buchette autorisé mais avec des groupements de 100b
                            if (depasseMaxGroupement100b()){
                                switch (actiongroupement100b){
                                    case AUTORISATION_ACTION:
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        break;
                                    case AUTORISATION_ET_MESSAGE:
                                        if(popupGroupement100b != null) popupGroupement100b.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "reussi", true);
                                        Traces.addPopup();
                                        break;
                                    case REFUS_ACTION:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        break;
                                    case REFUS_ET_MESSAGE:
                                        boite.removeView(view);
                                        owner.addView(view);
                                        if(popupGroupement100b_refus != null) popupGroupement100b_refus.show();
                                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), nom, "echec", false);
                                        Traces.addPopup();
                                        break;
                                }
                            }

                            ordonner();
                            countChild();
                            // pour tous les contenants de l'exercice
                            ArrayList<Contenant> listContenant = ListContenant.getLisContenant();
                            for (int i = 0; i < listContenant.size(); i++) {
                                // on récupère le contenant
                                Contenant c = listContenant.get(i);
                                // on l'ordonne
                                c.ordonner();
                                // on compte le nombre d'objets contenus et on met à jour l'affichage machine
                                c.countChild();
                            }
                            // on précise que le drag est fini
                            onDrag = false;
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            ordonner();
            countChild();
            return onDrag;

         }
    };

    /**
     * Constructeur de boite
     * @param c le RelativeLayout correspondant dans le fichier xml
     * @param num le numero de la boite que l'on veut instancier
     * @param context le contexte de l'application au moment de la création de la boite
     * @param affichage le RelativeLayout prévu pour la zone d'affichage des éléments de la boite
     * @param buchette la zone de texte permettant d'indiquer le nombre de buchettes contenues dans la boite
     * @param groupement10 la zone de texte permettant d'indiquer le nombre de groupements de 10 contenues dans la boite
     * @param groupement100 la zone de texte permettant d'indiquer le nombre de groupements de 100 contenues dans la boite
     * @param groupement100b la zone de texte permettant d'indiquer le nombre de groupements de 100b contenues dans la boite
     */
    public Boite(RelativeLayout c, int num, Context context,  RelativeLayout affichage, TextView buchette, TextView groupement10, TextView groupement100, TextView groupement100b){
        super(context);
        // on affecte le RelativeLayout correspondant à la boite
        boite = c;
        // on récupère le contexte associé à la boite
        this.c = context;
        // on ajoute un dragListener à la boite (pour permettre le dépot des objets)
        boite.setOnDragListener(dragListener);
        // on affecte le TextView correspondant à l'affichage du nom de la boite
        affichageNom = (TextView) boite.getChildAt(0);
        this.num = num;

        /*  BOITE 0  */
        //on récupère les informations de la boite, contenues dans la classe de configuration, selon le numéro que la boite que l'o veut instancier
        if(num == 0){
            /* PARAMÈTRES GÉNÉRAUX */
            // on récupère le nom de la boite
            nom = Config.boite0_nom;
            // on met à jour la description de la boite
            boite.setContentDescription(nom + ";" + Config.boite0_typeObjet);
            // on met à jour la variable permettant de savoir si l'on affiche un message d'erreur
            showPopup = Config.boite0_popup;
            // on récupère le fait d'afficher ou non le nombre d'éléments
            showAffichageElements = Config.boite0_affichage_machine;
            // on affiche ou cache la boite selon le paramètre précisé dans la configuration de l'exercice
            setVisible(Config.boite0_visible);
            // on instancie l'affichage des éléments avec les différents objets nécessaires
            this.affichageElements = new AffichageMachine(affichage, buchette, groupement10, groupement100, groupement100b);
            // on affiche ou cache l'affichage des éléments selon le paramètre précisé dans la configuration de l'exercice
            this.affichageElements.setVisibility(Config.boite0_affichage_machine);
            // pour chaque boite on récupère le bouton qui permet de selectionner tous les éléments
            boutonSelectAll = (ImageButton) boite.getChildAt(1);
            // on lui associe le cliListener correspondant
            boutonSelectAll.setOnClickListener(selectAll);
            // on met à jour l'affichage du nom de la boite
            if(afficher_cardianalite_totale) affichageNom.setText(nom +" : 0");
            else affichageNom.setText(nom);
            // on précise que la boite n'est pas séléctionnée
            selected = false;
            afficher_cardianalite_totale = Config.boite0_affichage_cadrinalite_totale;

            /*  NOMBRES MAX  */
            // on récupère le nombre maximal de buchettes que l'on peut déposer dans la boite
            nbmaxBuchette = Config.boite0_nb_max_buchette;
            // on récupère le nombre maximal de groupements de 10 que l'on peut déposer dans la boite
            nbmaxGroupement10 = Config.boite0_nb_max_groupement10;
            // on récupère le nombre maximal de groupements de 100 que l'on peut déposer dans la boite
            nbmaxGroupement100 = Config.boite0_nb_max_groupement100;
            // on récupère le nombre maximal de groupements de 100b que l'on peut déposer dans la boite
            nbmaxGroupement100b = Config.boite0_nb_max_groupement100b;

            /*  ACTIONS EN CAS DE DÉPASSEMENT */
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de buchettes autorisé
            actionbuchette = Config.boite0_action_depassement_buchette;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 10 autorisé
            actiongroupement10 = Config.boite0_action_depassement_groupement10;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 100 autorisé
            actiongroupement100 = Config.boite0_action_depassement_groupement100;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 100b autorisé
            actiongroupement100b = Config.boite0_action_depassement_groupement100b;


            popupBuchette = new Alert(this.c, Config.boite0_message_si_depassement_buchette_autorise);
            popupBuchette_refus = new Alert(this.c, Config.boite0_message_si_depassement_buchette_refuse);

            popupGroupement10 = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_autorise);
            popupGroupement10_refus = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_refuse);

            popupGroupement100 = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_autorise);
            popupGroupement100_refus = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_refuse);

            popupGroupement100b = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_autorise);
            popupGroupement100b_refus = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_refuse);

            /*  CONFIGURATION SELON LE TYPE DE RÉTROACTION */

                /*  BUCHETTES  */
            // si la rétrocation en cas de dépassement du nombre maximal de buchettes autorisé, est AUTORISATION_ET_MESSAGE
           /* if(Config.boite0_action_depassement_buchette == Alert.Action.AUTORISATION_ET_MESSAGE){
                // alors on précise que en cas de dépassement du nombre maximal de buchettes autorisé, on affichera un message
                showPopupBuchette = true;
                // on créer le message à afficher en cas de dépassement du nombre maximal de buchettes autorisé. On précise le texte qui devra s'afficher.
                popupBuchette = new Alert(this.c, Config.boite0_message_si_depassement_buchette_autorise);
            }
            popupBuchette = new Alert(this.c, Config.boite0_message_si_depassement_buchette_autorise);
            // si la rétrocation en cas de dépassement du nombre maximal de buchettes autorisé, est REFUS_ET_MESSAGE
            if(Config.boite0_action_depassement_buchette == Alert.Action.REFUS_ET_MESSAGE){
                // alors on précise que en cas de dépassement du nombre maximal de buchettes autorisé, on affichera un message
                showPopupBuchette = true;
                // on créer le message à afficher en cas de dépassement du nombre maximal de buchettes autorisé. On précise le texte qui devra s'afficher.
                popupBuchette = new Alert(this.c, Config.boite0_message_si_depassement_buchette_refuse);
            }
            popupBuchette = new Alert(this.c, Config.boite0_message_si_depassement_buchette_refuse);
            // si la rétrocation en cas de dépassement du nombre maximal de buchettes autorisé, est REFUS_ACTION ou AUTORISATION_ACTION alors on précise que l'on ne veut pas afficher de message
            if(Config.boite0_action_depassement_buchette == Alert.Action.REFUS_ACTION || Config.boite0_action_depassement_buchette == Alert.Action.AUTORISATION_ACTION ) showPopupBuchette = false;

                /*  GROUPEMENTS 10
            // même fonctionnement qu'en cas de dépassement du nombre maximal de buchettes autorisé, mais pour les groupements de 10
            if(Config.boite0_action_depassement_groupement10 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_autorise);
            }
            popupGroupement10 = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_autorise);

            if(Config.boite0_action_depassement_groupement10 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_autorise);
            }
            popupGroupement10 = new Alert(this.c, Config.boite0_message_si_depassement_groupement10_autorise);

            if(Config.boite0_action_depassement_groupement10 == Alert.Action.REFUS_ACTION || Config.boite0_action_depassement_groupement10 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement10 = false;

                /*  GROUPEMENTS 100
            // même fonctionnement qu'en cas de dépassement du nombre maximal de buchettes autorisé, mais pour les groupements de 100
            if(Config.boite0_action_depassement_groupement100 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_autorise);
            }
            popupGroupement100 = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_autorise);

            if(Config.boite0_action_depassement_groupement100 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_autorise);
            }
            popupGroupement100 = new Alert(this.c, Config.boite0_message_si_depassement_groupement100_autorise);

            if(Config.boite0_action_depassement_groupement100 == Alert.Action.REFUS_ACTION || Config.boite0_action_depassement_groupement100 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100 = false;

                /*  GROUPEMENTS 100B
            // même fonctionnement qu'en cas de dépassement du nombre maximal de buchettes autorisé, mais pour les groupements de 100b
            if(Config.boite0_action_depassement_groupement100b == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_autorise);
            }
            popupGroupement100b = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_autorise);

            if(Config.boite0_action_depassement_groupement100b == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_autorise);
            }
            popupGroupement100b = new Alert(this.c, Config.boite0_message_si_depassement_groupement100b_autorise);

            if(Config.boite0_action_depassement_groupement100b == Alert.Action.REFUS_ACTION || Config.boite0_action_depassement_groupement100b == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100b = false;
            */
            /* ÉLÉMENTS PRÉSENTS DANS LA BOITE INITIALEMENT */
            // Si la boite est visible
            if(Config.boite0_visible){
                // on créer et on ajoute le nombre de buchettes voulu dans la boite initialement
                for (int i = 0; i< Config.boite0_nombre_buchette_initial; i++){
                    // on créer l'objet
                    Buchette b = new Buchette(context);
                    // on ajoute son image dans la boite
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.BOITE0);
                }
                // on créer et on ajoute le nombre de groupements de 10 voulu dans la boite initialement
                for (int i = 0; i< Config.boite0_nombre_groupement10_initial; i++){
                    // on créer l'objet
                    Groupement10 b = new Groupement10(context);
                    // on ajoute son image dans la boite
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.BOITE0);

                }
                // on créer et on ajoute le nombre de groupements de 100 voulu dans la boite initialement
                for (int i = 0; i< Config.boite0_nombre_groupement100_initial; i++){
                    // on créer l'objet
                    Groupement100 b = new Groupement100(context);
                    // on ajoute son image dans la boite
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.BOITE0);

                }
                // on créer et on ajoute le nombre de groupements de 100b voulu dans la boite initialement
                for (int i = 0; i< Config.boite0_nombre_groupement100b_initial; i++){
                    // on créer l'objet
                    Groupement100B b = new Groupement100B(context);
                    // on ajoute son image dans la boite
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.BOITE0);

                }
                // on ordonne les éléments dans la boite
                ordonner();
                // on met à jour l'affichage du nombre d'éléments contenus dans la boite
                countChild();
            }

        }
        /*  BOITE 1  */
        // même principe que pour la boite 0 mais en allant chercher les informations pour la boite 1 (et non la boite 0) dans la classe Config
        if(num == 1){
            // on récupère le nom de la boite
            nom = Config.boite1_nom;
            // on met à jour la description de la boite
            boite.setContentDescription(nom + ";" + Config.boite1_typeObjet);
            // on met à jour la variable permettant de savoir si l'on affiche un message d'erreur
            showPopup = Config.boite1_popup;
            // on récupère le fait d'afficher ou non le nombre d'éléments
            showAffichageElements = Config.boite1_affichage_machine;
            // on affiche ou cache la boite selon le paramètre précisé dans la configuration de l'exercice
            setVisible(Config.boite1_visible);
            // on instancie l'affichage des éléments avec les différents objets nécessaires
            this.affichageElements = new AffichageMachine(affichage, buchette, groupement10, groupement100, groupement100b);
            // on affiche ou cache l'affichage des éléments selon le paramètre précisé dans la configuration de l'exercice
            this.affichageElements.setVisibility(Config.boite1_affichage_machine);
            // pour chaque boite on récupère le bouton qui permet de selectionner tous les éléments
            boutonSelectAll = (ImageButton) boite.getChildAt(1);
            // on lui associe le cliListener correspondant
            boutonSelectAll.setOnClickListener(selectAll);
            // on met à jour l'affichage du nom de la boite
            affichageNom.setText(nom);
            // on précise que la boite n'est pas séléctionnée
            selected = false;
            afficher_cardianalite_totale = Config.boite1_affichage_cadrinalite_totale;

            nbmaxBuchette = Config.boite1_nb_max_buchette;
            nbmaxGroupement10 = Config.boite1_nb_max_groupement10;
            nbmaxGroupement100 = Config.boite1_nb_max_groupement100;
            nbmaxGroupement100b = Config.boite1_nb_max_groupement100b;

   /*         actionbuchette = Config.boite1_action_depassement_buchette;
            actiongroupement10 = Config.boite1_action_depassement_groupement10;
            actiongroupement100 = Config.boite1_action_depassement_groupement100;
            actiongroupement100b = Config.boite1_action_depassement_groupement100b;

            if(Config.boite1_action_depassement_buchette == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite1_message_si_depassement_buchette_autorise);
            }
            if(Config.boite1_action_depassement_buchette == Alert.Action.REFUS_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite1_message_si_depassement_buchette_refuse);
            }
            if(Config.boite1_action_depassement_groupement10 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite1_message_si_depassement_groupement10_autorise);
            }
            if(Config.boite1_action_depassement_buchette == Alert.Action.REFUS_ACTION || Config.boite1_action_depassement_buchette == Alert.Action.AUTORISATION_ACTION ) showPopupBuchette = false;

            if(Config.boite1_action_depassement_groupement10 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite1_message_si_depassement_groupement10_autorise);
            }
            if(Config.boite1_action_depassement_groupement10 == Alert.Action.REFUS_ACTION || Config.boite1_action_depassement_groupement10 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement10 = false;

            if(Config.boite1_action_depassement_groupement100 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite1_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite1_action_depassement_groupement100 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite1_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite1_action_depassement_groupement100 == Alert.Action.REFUS_ACTION || Config.boite1_action_depassement_groupement100 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100 = false;


            if(Config.boite1_action_depassement_groupement100b == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite1_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite1_action_depassement_groupement100b == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite1_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite1_action_depassement_groupement100b == Alert.Action.REFUS_ACTION || Config.boite1_action_depassement_groupement100b == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100b = false;

*/

            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de buchettes autorisé
            actionbuchette = Config.boite1_action_depassement_buchette;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 10 autorisé
            actiongroupement10 = Config.boite1_action_depassement_groupement10;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 100 autorisé
            actiongroupement100 = Config.boite1_action_depassement_groupement100;
            // on récupère la rétrocation qui est associé au fait de dépasser le nombre de groupement de 100b autorisé
            actiongroupement100b = Config.boite1_action_depassement_groupement100b;


            popupBuchette = new Alert(this.c, Config.boite1_message_si_depassement_buchette_autorise);
            popupBuchette_refus = new Alert(this.c, Config.boite1_message_si_depassement_buchette_refuse);

            popupGroupement10 = new Alert(this.c, Config.boite1_message_si_depassement_groupement10_autorise);
            popupGroupement10_refus = new Alert(this.c, Config.boite1_message_si_depassement_groupement10_refuse);

            popupGroupement100 = new Alert(this.c, Config.boite1_message_si_depassement_groupement100_autorise);
            popupGroupement100_refus = new Alert(this.c, Config.boite1_message_si_depassement_groupement100_refuse);

            popupGroupement100b = new Alert(this.c, Config.boite1_message_si_depassement_groupement100b_autorise);
            popupGroupement100b_refus = new Alert(this.c, Config.boite1_message_si_depassement_groupement100b_refuse);


            if(Config.boite1_visible){
                for (int i = 0; i< Config.boite1_nombre_buchette_initial; i++){
                    Buchette b = new Buchette(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.BOITE1);
                }
                for (int i = 0; i< Config.boite1_nombre_groupement10_initial; i++){
                    Groupement10 b = new Groupement10(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.BOITE1);
                }
                for (int i = 0; i< Config.boite1_nombre_groupement100_initial; i++){
                    Groupement100 b = new Groupement100(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.BOITE1);
                }
                for (int i = 0; i< Config.boite1_nombre_groupement100b_initial; i++){
                    Groupement100B b = new Groupement100B(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.BOITE1);
                }
                ordonner();
                countChild();
            }

        }

        /*  BOITE 2  */
        // même principe que pour la boite 0 mais en allant chercher les informations pour la boite 2 (et non la boite 0) dans la classe Config
        if(num == 2){
            // on récupère le nom de la boite
            nom = Config.boite2_nom;
            // on met à jour la description de la boite
            boite.setContentDescription(nom + ";" + Config.boite1_typeObjet);
            // on met à jour la variable permettant de savoir si l'on affiche un message d'erreur
            showPopup = Config.boite2_popup;
            // on récupère le fait d'afficher ou non le nombre d'éléments
            showAffichageElements = Config.boite2_affichage_machine;
            // on affiche ou cache la boite selon le paramètre précisé dans la configuration de l'exercice
            setVisible(Config.boite2_visible);
            // on instancie l'affichage des éléments avec les différents objets nécessaires
            this.affichageElements = new AffichageMachine(affichage, buchette, groupement10, groupement100, groupement100b);
            // on affiche ou cache l'affichage des éléments selon le paramètre précisé dans la configuration de l'exercice
            this.affichageElements.setVisibility(Config.boite2_affichage_machine);
            // pour chaque boite on récupère le bouton qui permet de selectionner tous les éléments
            boutonSelectAll = (ImageButton) boite.getChildAt(1);
            // on lui associe le cliListener correspondant
            boutonSelectAll.setOnClickListener(selectAll);
            // on met à jour l'affichage du nom de la boite
            affichageNom.setText(nom);
            // on précise que la boite n'est pas séléctionnée
            selected = false;
            afficher_cardianalite_totale = Config.boite2_affichage_cadrinalite_totale;

            nbmaxBuchette = Config.boite2_nb_max_buchette;
            nbmaxGroupement10 = Config.boite2_nb_max_groupement10;
            nbmaxGroupement100 = Config.boite2_nb_max_groupement100;
            nbmaxGroupement100b = Config.boite2_nb_max_groupement100b;

            actionbuchette = Config.boite2_action_depassement_buchette;
            actiongroupement10 = Config.boite2_action_depassement_groupement10;
            actiongroupement100 = Config.boite2_action_depassement_groupement100;
            actiongroupement100b = Config.boite2_action_depassement_groupement100b;

   /*         if(Config.boite2_action_depassement_buchette == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite2_message_si_depassement_buchette_autorise);
            }
            if(Config.boite2_action_depassement_buchette == Alert.Action.REFUS_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite2_message_si_depassement_buchette_refuse);
            }
            if(Config.boite2_action_depassement_groupement10 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite2_message_si_depassement_groupement10_autorise);
            }
            if(Config.boite2_action_depassement_buchette == Alert.Action.REFUS_ACTION || Config.boite2_action_depassement_buchette == Alert.Action.AUTORISATION_ACTION ){
                showPopupBuchette = false;
            }

            if(Config.boite2_action_depassement_groupement10 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite2_message_si_depassement_groupement10_autorise);
            }
            if(Config.boite2_action_depassement_groupement10 == Alert.Action.REFUS_ACTION || Config.boite2_action_depassement_groupement10 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement10 = false;

            if(Config.boite2_action_depassement_groupement100 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite2_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite2_action_depassement_groupement100 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite2_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite2_action_depassement_groupement100 == Alert.Action.REFUS_ACTION || Config.boite2_action_depassement_groupement100 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100 = false;


            if(Config.boite2_action_depassement_groupement100b == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite2_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite2_action_depassement_groupement100b == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite2_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite2_action_depassement_groupement100b == Alert.Action.REFUS_ACTION || Config.boite2_action_depassement_groupement100b == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100b = false;
*/

            popupBuchette = new Alert(this.c, Config.boite2_message_si_depassement_buchette_autorise);
            popupBuchette_refus = new Alert(this.c, Config.boite2_message_si_depassement_buchette_refuse);

            popupGroupement10 = new Alert(this.c, Config.boite2_message_si_depassement_groupement10_autorise);
            popupGroupement10_refus = new Alert(this.c, Config.boite2_message_si_depassement_groupement10_refuse);

            popupGroupement100 = new Alert(this.c, Config.boite2_message_si_depassement_groupement100_autorise);
            popupGroupement100_refus = new Alert(this.c, Config.boite2_message_si_depassement_groupement100_refuse);

            popupGroupement100b = new Alert(this.c, Config.boite2_message_si_depassement_groupement100b_autorise);
            popupGroupement100b_refus = new Alert(this.c, Config.boite2_message_si_depassement_groupement100b_refuse);


            if(Config.boite2_visible){
                for (int i = 0; i< Config.boite2_nombre_buchette_initial; i++){
                    Buchette b = new Buchette(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.BOITE2);
                }
                for (int i = 0; i< Config.boite2_nombre_groupement10_initial; i++){
                    Groupement10 b = new Groupement10(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.BOITE2);
                }
                for (int i = 0; i< Config.boite2_nombre_groupement100_initial; i++){
                    Groupement100 b = new Groupement100(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.BOITE2);
                }
                for (int i = 0; i< Config.boite2_nombre_groupement100b_initial; i++){
                    Groupement100B b = new Groupement100B(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.BOITE2);
                }
                ordonner();
                countChild();
            }

        }

        /*  BOITE 3  */
        // même principe que pour la boite 0 mais en allant chercher les informations pour la boite 3 (et non la boite 0) dans la classe Config
        if(num == 3){
            // on récupère le nom de la boite
            nom = Config.boite3_nom;
            // on met à jour la description de la boite
            boite.setContentDescription(nom + ";" + Config.boite3_typeObjet);
            // on met à jour la variable permettant de savoir si l'on affiche un message d'erreur
            showPopup = Config.boite3_popup;
            // on récupère le fait d'afficher ou non le nombre d'éléments
            showAffichageElements = Config.boite3_affichage_machine;
            // on affiche ou cache la boite selon le paramètre précisé dans la configuration de l'exercice
            setVisible(Config.boite3_visible);
            // on instancie l'affichage des éléments avec les différents objets nécessaires
            this.affichageElements = new AffichageMachine(affichage, buchette, groupement10, groupement100, groupement100b);
            // on affiche ou cache l'affichage des éléments selon le paramètre précisé dans la configuration de l'exercice
            this.affichageElements.setVisibility(Config.boite3_affichage_machine);
            // pour chaque boite on récupère le bouton qui permet de selectionner tous les éléments
            boutonSelectAll = (ImageButton) boite.getChildAt(1);
            // on lui associe le cliListener correspondant
            boutonSelectAll.setOnClickListener(selectAll);
            // on met à jour l'affichage du nom de la boite
            affichageNom.setText(nom);
            // on précise que la boite n'est pas séléctionnée
            selected = false;
            afficher_cardianalite_totale = Config.boite3_affichage_cadrinalite_totale;

            nbmaxBuchette = Config.boite3_nb_max_buchette;
            nbmaxGroupement10 = Config.boite3_nb_max_groupement10;
            nbmaxGroupement100 = Config.boite3_nb_max_groupement100;
            nbmaxGroupement100b = Config.boite3_nb_max_groupement100b;

            actionbuchette = Config.boite3_action_depassement_buchette;
            actiongroupement10 = Config.boite3_action_depassement_groupement10;
            actiongroupement100 = Config.boite3_action_depassement_groupement100;
            actiongroupement100b = Config.boite3_action_depassement_groupement100b;

/*            if(Config.boite3_action_depassement_buchette == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite3_message_si_depassement_buchette_autorise);
            }
            if(Config.boite3_action_depassement_buchette == Alert.Action.REFUS_ET_MESSAGE){
                showPopupBuchette = true;
                popupBuchette = new Alert(this.c, Config.boite3_message_si_depassement_buchette_refuse);
            }
            if(Config.boite3_action_depassement_buchette == Alert.Action.REFUS_ACTION || Config.boite3_action_depassement_buchette == Alert.Action.AUTORISATION_ACTION ) showPopupBuchette = false;

            if(Config.boite3_action_depassement_groupement10 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite3_message_si_depassement_groupement10_autorise);
            }

            if(Config.boite3_action_depassement_groupement10 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement10 = true;
                popupGroupement10 = new Alert(this.c, Config.boite3_message_si_depassement_groupement10_autorise);
            }
            if(Config.boite3_action_depassement_groupement10 == Alert.Action.REFUS_ACTION || Config.boite3_action_depassement_groupement10 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement10 = false;

            if(Config.boite3_action_depassement_groupement100 == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite3_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite3_action_depassement_groupement100 == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100 = true;
                popupGroupement100 = new Alert(this.c, Config.boite3_message_si_depassement_groupement100_autorise);
            }
            if(Config.boite3_action_depassement_groupement100 == Alert.Action.REFUS_ACTION || Config.boite3_action_depassement_groupement100 == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100 = false;


            if(Config.boite3_action_depassement_groupement100b == Alert.Action.AUTORISATION_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite3_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite3_action_depassement_groupement100b == Alert.Action.REFUS_ET_MESSAGE){
                showPopupGroupement100b = true;
                popupGroupement100b = new Alert(this.c, Config.boite3_message_si_depassement_groupement100b_autorise);
            }
            if(Config.boite3_action_depassement_groupement100b == Alert.Action.REFUS_ACTION || Config.boite3_action_depassement_groupement100b == Alert.Action.AUTORISATION_ACTION ) showPopupGroupement100b = false;
*/

            popupBuchette = new Alert(this.c, Config.boite3_message_si_depassement_buchette_autorise);
            popupBuchette_refus = new Alert(this.c, Config.boite3_message_si_depassement_buchette_refuse);

            popupGroupement10 = new Alert(this.c, Config.boite3_message_si_depassement_groupement10_autorise);
            popupGroupement10_refus = new Alert(this.c, Config.boite3_message_si_depassement_groupement10_refuse);

            popupGroupement100 = new Alert(this.c, Config.boite3_message_si_depassement_groupement100_autorise);
            popupGroupement100_refus = new Alert(this.c, Config.boite3_message_si_depassement_groupement100_refuse);

            popupGroupement100b = new Alert(this.c, Config.boite3_message_si_depassement_groupement100b_autorise);
            popupGroupement100b_refus = new Alert(this.c, Config.boite3_message_si_depassement_groupement100b_refuse);

            if(Config.boite3_visible){
                for (int i = 0; i< Config.boite3_nombre_buchette_initial; i++){
                    Buchette b = new Buchette(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.BUCHETTE, Etat_simulation.Contenants.BOITE3);
                }
                for (int i = 0; i< Config.boite3_nombre_groupement10_initial; i++){
                    Groupement10 b = new Groupement10(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT10, Etat_simulation.Contenants.BOITE3);
                }
                for (int i = 0; i< Config.boite3_nombre_groupement100_initial; i++){
                    Groupement100 b = new Groupement100(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100, Etat_simulation.Contenants.BOITE3);
                }
                for (int i = 0; i< Config.boite3_nombre_groupement100b_initial; i++){
                    Groupement100B b = new Groupement100B(context);
                    boite.addView(b.getImageView());
                    Etat_simulation.ajouter(Etat_simulation.Objets.GROUPEMENT100B, Etat_simulation.Contenants.BOITE3);
                }
                ordonner();
                countChild();
            }
        }

    }


    /**
     * Méthode permettant de rendre visible ou non la boite
     * @param visible true si la boite doit être visible, false sinon
     */
    public void setVisible(boolean visible){
        if(visible) boite.setVisibility(View.VISIBLE);
        else boite.setVisibility(View.INVISIBLE);
    }

    /**
     * Méthode permettant de compter le nombre d'objets déposés dans la boite et d'actualiser l'affichage machine en conséquence
     * @return le nombre d'objets déposés dans la boite
     */
    public int countChild(){
        // le nombre d'éléments dans la boite
        int nb_buchette = 0;
        int nb_groupement10 = 0;
        int nb_groupement100 = 0;
        int nb_groupement100b = 0;

        // si la boite est seléctionnée
        if(selected){
            // on compte les objets à partir de la liste des objets seléctionnés
            for(int i = 0; i<ListObjets.listView.size(); i++){
                // selon le type de l'objet on augmente le nombre d'éléments de son type, de 1
                if(ListObjets.listView.get(i).getContentDescription().equals("buchette")) nb_buchette++;
                if(ListObjets.listView.get(i).getContentDescription().equals("groupement10")) nb_groupement10++;
                if(ListObjets.listView.get(i).getContentDescription().equals("groupement100")) nb_groupement100++;
                if(ListObjets.listView.get(i).getContentDescription().equals("groupement100b")) nb_groupement100b++;
            }
        }
        // sinon, si la boite n'est pas seléctionnée
        else{
            // pour tout les éléments contenus dans la boite (sans l'affichage du nom et du bouton selectAll)
            for(int i = 2; i<boite.getChildCount(); i++){
                // selon le type de l'objet on augmente le nombre d'éléments de son type, de 1
                if(boite.getChildAt(i).getContentDescription().equals("buchette")) nb_buchette++;
                if(boite.getChildAt(i).getContentDescription().equals("groupement10")) nb_groupement10++;
                if(boite.getChildAt(i).getContentDescription().equals("groupement100")) nb_groupement100++;
                if(boite.getChildAt(i).getContentDescription().equals("groupement100b")) nb_groupement100b++;
            }
        }
        // si la boite peut afficher le nombre d'éléments qu'elle contient elle l'affiche
        if (showAffichageElements){
            // on met à jour l'affichage du nombre de buchettes contenues dans la boite, à partir du nombre de buchettes calculé précédement
            affichageElements.mettreAJourAffichageBuchette(nb_buchette);
            // on met à jour l'affichage du nombre de buchettes contenues dans la boite, à partir du nombre de groupements de 10 calculé précédement
            affichageElements.mettreAJourAffichageGroupement10(nb_groupement10);
            // on met à jour l'affichage du nombre de buchettes contenues dans la boite, à partir du nombre de groupements de 100 calculé précédement
            affichageElements.mettreAJourAffichageGroupement100(nb_groupement100);
            // on met à jour l'affichage du nombre de buchettes contenues dans la boite, à partir du nombre de groupements de 100b calculé précédement
            affichageElements.mettreAJourAffichageGroupement100b(nb_groupement100b);

        }

        if(afficher_cardianalite_totale) affichageNom.setText(nom +" : " + (nb_buchette + nb_groupement10 + nb_groupement100 + nb_groupement100b));
        // on retourne le nombre total d'éléments que contient la boite
        return nb_buchette + nb_groupement10 + nb_groupement100 + nb_groupement100b;

    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de buchettes contenues dans la boite dépasse le nombre maximal de buchettes autorisé
     * @return true si le nombre de buchettes contenues dans la boite dépasse le nombre maximal de buchettes autorisé
     */
    public boolean depasseMaxBuchette() {
        int nb_buchettes = 0;
        for (int i = 2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().toString().equals("buchette")) nb_buchettes++;
        }
        return nb_buchettes > nbmaxBuchette;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 10 contenus dans la boite dépasse le nombre maximal de groupements de 10 autorisé
     * @return true si le nombre de groupements de 10 contenus dans la boite dépasse le nombre maximal de groupements de 10 autorisé
     */
    public boolean depasseMaxGroupement10() {
        int nb_groupement10 = 0;
        for (int i = 2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().toString().equals("groupement10")) nb_groupement10++;
        }
        return nb_groupement10 > nbmaxGroupement10;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100 contenus dans la boite dépasse le nombre maximal de groupements de 100 autorisé
     * @return true si le nombre de groupements de 100 contenus dans la boite dépasse le nombre maximal de groupements de 100 autorisé
     */
    public boolean depasseMaxGroupement100() {
        int nb_groupement100 = 0;
        for (int i = 2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().toString().equals("groupement100")) nb_groupement100++;
        }
        return nb_groupement100 > nbmaxGroupement100;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100b contenus dans la boite dépasse le nombre maximal de groupements de 100b autorisé
     * @return true si le nombre de groupements de 100b contenus dans la boite dépasse le nombre maximal de groupements de 100b autorisé
     */
    public boolean depasseMaxGroupement100b() {
        int nb_groupement100b = 0;
        for (int i = 2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().toString().equals("groupement100b")) nb_groupement100b++;
        }
        return nb_groupement100b > nbmaxGroupement100b;
    }


    /**
     * Méthode permettant d'ordonner les objets contenus dans la boite
     */
    public void ordonner(){
        if(boite.getChildCount() >2 && !boite.getChildAt(boite.getChildCount()-1).getContentDescription().toString().equals("selectAll")) {
            ArrayList<View> listBuchettes = new ArrayList<>();
            // pour tous les objets déposés dans la boite (sans l'affichage machine : boite.getChildAt(0))
            for (int i = 2; i < boite.getChildCount(); i++) {
                // on ajoute l'objet dans la liste
                listBuchettes.add(boite.getChildAt(i));
            }
            // pour tous les objets déposés dans la boite (sans l'affichage machine : boite.getChildAt(0))
            for (int j = listBuchettes.size() + 1; j >= 2; j--) {
                // on les enlèves de la boite
                boite.removeViewAt(j);
            }
            //pour tous les objets déposés dans la boite (sans l'affichage machine : boite.getChildAt(0))
            for (int i = 0; i < listBuchettes.size(); i++) {
                // on récupère l'objet dans la liste
                View view = listBuchettes.get(i);
                // on modifie sa position selon le nombre d'objets déja déposés dans la boite

                // on ajoute l'objet dans la boite
                boite.addView(view);
                // calcul de la position de l'objet selon le nombre et la taille d'objets déja contenus
                if(Config.taille == 1){
                    view.setX(Parametres.X * (boite.getChildCount() - 3));
                    view.setY(0f);
                }else {
                    if (countChild() <= 5) {
                        view.setX(Parametres.X * (boite.getChildCount() - 3));
                        view.setY(0f);
                    } else {
                        if (countChild() <= 10) {
                            if (Config.taille == 2) view.setY(Parametres.Y_2L_T2);
                            if (Config.taille == 3) view.setY(Parametres.Y_2L_T3);
                            view.setX(Parametres.X * (boite.getChildCount() - 8));
                        } else {
                            // l'exercice de complémet a des boites plus grandes donc on peut afficher sur plus de lignes
                            if (!Config.exercice_complement) {
                                //if (Config.taille == 2) view.setY(160f);
                                if (Config.taille == 3) view.setY(Parametres.Y_3L_T3);
                                view.setX(Parametres.X * (boite.getChildCount() - 13));
                            } else {
                                if (countChild() <= 15) {
                                    if (Config.taille == 2) view.setY(Parametres.Y_3L_T2);
                                    if (Config.taille == 3) view.setY(Parametres.Y_3L_T3);
                                    view.setX(Parametres.X * (boite.getChildCount() - 13));
                                } else {
                                    if (countChild() <= 20) {
                                        //if (Config.taille == 2) view.setY(240f);
                                        if (Config.taille == 3) view.setY(Parametres.Y_4L_T3);
                                        view.setX(Parametres.X * (boite.getChildCount() - 18));
                                    } else {
                                        //if (Config.taille == 2) view.setY(320f);
                                        if (Config.taille == 3) view.setY(Parametres.Y_5L_T3);
                                        view.setX(Parametres.X * (boite.getChildCount() - 23));

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Méthode permettant de vider la boite
     */
    public void vider(){
        ArrayList<View> listBuchettes = new ArrayList<>();
        // pour tous les objets déposés dans la boite (sans l'affichage machine : boite.getChildAt(0))
        for(int i = 2; i < boite.getChildCount(); i++){
            // on ajoute l'objet dans la liste
            listBuchettes.add(boite.getChildAt(i));
        }

        String[] typeObjet = new String[listBuchettes.size()];
        for (int i = 0; i < listBuchettes.size(); i++){
            typeObjet[i] = boite.getChildAt(i+2).getContentDescription().toString();
        }
        // on ajoute la trace du fait que l'on ai vidé la boite de tous ses objets
        Traces.addActionVider(nom, listBuchettes.size(),typeObjet );
        // pour tous les objets déposés dans la boite (sans l'affichage machine : boite.getChildAt(0))
        for(int j = listBuchettes.size() + 1  ; j >= 2 ; j --){
            // on enlève l'objet de la boite
            boite.removeViewAt(j);
        }
        // on actualise l'affichage machine
        countChild();
    }


    /**
     * Méthode permettant de récupérer la liste des objets acceptés pour un Relativelayout, sous forme d'un String[]
     * @param r le RelativeLayout dont on veut connaitre la liste d'objets acceptés
     * @return la liste d'objets acceptés
     */
    public String[] getlistObjetAcceptesString(RelativeLayout r){
        String[] list;
        String[] listO = {""};
        // la liste des objets acceptés sont dans la description du relativeLayout, sous la forme : nom;obj1,obj2,obj3
        String desc = r.getContentDescription().toString();
        list = desc.split(";");
        if (list.length > 1) listO = list[1].split(",");
        return listO;
    }


    /**
     * Méthode permettant de récupérer la liste des objets acceptés de la boite, sous forme d'un String[]
     * @return la liste d'objets acceptés
     */
    public String[] getlistObjetAcceptesString(){
        String[] list;
        String[] listO = {""};
        String desc = boite.getContentDescription().toString();
        list = desc.split(";");
        if (list.length > 1) listO = list[1].split(",");
        return listO;
    }

    /**
     * Méthode retournant le nom de la boite
     * @return le nom de la boite
     */
    public String getNom (){
        return nom;
    }

    /**
     * Méthode permettant de récupérer le RelativeLayout associé à la boite
     * @return le RelativeLayout associé à la boite
     */
    public RelativeLayout getRelativeLayout(){return boite;}


    /**
     * Méthode permettant de sélectionner tous les objets mis dans la boite
     */
    public void selectAll(){
        // si la boite est seléctionnée ou si la liste des éléments seléctionnés est vide
        if( selected || ListObjets.listView.size() == 0) {
            // si la boite n'est pas seléctionnée et qu'il y a au moins 2 objets dans la boite (sans compter l'affichage du nom et du bouton pour tout selectionner
            if (!selected && boite.getChildCount() > 3) {
                // on précise que la boite est seléctionnée
                selected = true;
                // on crée une image de selection de tous les objets
                ImageView selectAll = new ImageView(c);
                // on lui associe la bonne description
                selectAll.setContentDescription("selectAll");
                // selon la taille des éléments de l'exercice on met l'image correspondante
                if (Config.taille == 1)
                    selectAll.setImageResource(R.drawable.selection_g);
                if (Config.taille == 2)
                    selectAll.setImageResource(R.drawable.selection_m);
                if (Config.taille == 3)
                    selectAll.setImageResource(R.drawable.selection_p);

                //pour tous les objets de la boite (sans l'affichage du nom et du bouton pour tout seléctionner)
                for (int i = 2; i < boite.getChildCount(); i++) {
                    // on l'ajoute à la liste des objets selectionné
                    ListObjets.listView.add(boite.getChildAt(i));
                    // on rend l'objet invisible
                    boite.getChildAt(i).setVisibility(INVISIBLE);
                }
                // on positionne correctement l'image de selectAll
                selectAll.setX(60f);
                // on lui associe un touchListener
                selectAll.setOnTouchListener(touchListener);
                // on ajoute l'image du selectAll à la boite
                boite.addView(selectAll);
            }
            // sinon, si la boite est selectionnée ou si le nombre d'éléments qu'elle contient est inférieur à 2
            else {
                // si la boite est selectionnée et qu'il n'y a qu'un élémént présent (sans l'affichage du nom et du bouton pour tout seléctionner)
                if (selected && boite.getChildCount() > 2) {
                    // on déselectionne la boite
                    selected = false;
                    // on enlève l'image du selectAll
                    boite.removeViewAt(boite.getChildCount() - 1);
                    // pour tout les objets qui étaient selectionnés
                    for (int i = 2; i < boite.getChildCount(); i++) {
                        // on les rend visible
                        boite.getChildAt(i).setVisibility(VISIBLE);
                    }
                    // on vide la liste des éléments seléctionnés
                    ListObjets.removeAll();
                }
            }
            countChild();
        }

    }

    /**
     * Méthode permettant de déselectionner une boite
     */
    public void unSelected(){
        if(boite.getChildCount() > 2 && !boite.getChildAt(boite.getChildCount()-1).getContentDescription().toString().equals("selectAll")) selected = false;
        if(boite.getChildCount() == 2) selected = false;
    }


    /**
     * Le touchlistener pour la view, de sélection de tous les objets
     */
    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // on récupère l'action
            int action = MotionEventCompat.getActionMasked(event);
            // selon l'action qui a été exécutée
            switch (action) {
                // si l'utilisateur à appuyé sur la buchette
                case MotionEvent.ACTION_DOWN:

                    //permet le drag
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                    // on déclanche le drag
                    v.startDrag(data, shadowBuilder, v, 0);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE :

                default:
            }
            return true;
        }
    };

    /**
     * Méthode permettant de compter le nombre de buchettes présentes dans la boite
     * @return le nombre de buchettes présentes dans la boite
     */
    public int nbBuchettes(){
        int nb = 0;
        for(int i =2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().equals("buchette")) nb++;
        }
        return nb;
    }

    /**
     * Méthode permettant de compter le nombre de groupements de 10 présentes dans la boite
     * @return le nombre de groupements de 10 présentes dans la boite
     */
    public int nbGroupement10(){
        int nb = 0;
        for(int i =2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().equals("groupement10")) nb++;
        }
        return nb;
    }

    /**
     * Méthode permettant de compter le nombre de groupements de 100 présentes dans la boite
     * @return le nombre de groupements de 100 présentes dans la boite
     */
    public int nbGroupement100(){
        int nb = 0;
        for(int i =2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().equals("groupement100")) nb++;
        }
        return nb;
    }

    /**
     * Méthode permettant de compter le nombre de groupements de 100b présentes dans la boite
     * @return le nombre de groupements de 100b présentes dans la boite
     */
    public int nbGroupement100b(){
        int nb = 0;
        for(int i =2; i<boite.getChildCount(); i++){
            if(boite.getChildAt(i).getContentDescription().equals("groupement100b")) nb++;
        }
        return nb;
    }


}
