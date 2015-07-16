package com.ter.app.applicationtest.Contenants;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.ListObjets;
import com.ter.app.applicationtest.parametresExercices.Config;

import java.util.ArrayList;

/**
 * Classe représentant la Table d'exercice
 */
public class Table extends Contenant{

    /**
     * RelativeLayout dans le xml, correspondant à la table
     */
    private RelativeLayout table;

    /**
     * Le nom associé au RelativeLayout de la table
     */
    private String nom = "table";

    /**
     * le dragListener associé à la table
     */
    private View.OnDragListener dragListener = new View.OnDragListener() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public boolean onDrag(View v, DragEvent event) {
            boolean onDrage =  true;
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    // DROP DE LA VUE
                    // récupère la vue déposée
                    View view = (View) event.getLocalState();
                    // récupère le parent de la vue
                    ViewGroup owner = (ViewGroup) view.getParent();
                    // enlève la vue de ce parent
                    owner.removeView(view);
                    // si la view correspond au selectAll
                    if(view.getContentDescription().equals("selectAll")){
                        // pour chaque objets sélectionnés
                        for(int i = 0; i < ListObjets.listView.size(); i++){
                            // on récupère la view
                            View tmp = ListObjets.listView.get(i);
                            // on le rend visible
                            tmp.setVisibility(VISIBLE);
                            // on l'enlève de son parent
                            owner.removeView(tmp);
                            // on l'ajoute à la table
                            table.addView(tmp);
                            // on ajoute une trace pour le déplacement de cet objet
                            Traces.addDeplacement(tmp.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), getNom(), "reussi", true);
                        }
                        // on enlève tous les objets de la liste des objets sélectionnés.
                        ListObjets.removeAll();
                    }
                    // sinon, si l'objet déplacé n'est pas un selectAll
                    else{
                        // on ajoute la view à la table
                        table.addView(view);
                        // on ajoute une trace pour le déplacement de cet objet
                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), getNom(), "reussi", true);

                    }
                    Contenant c;
                    // pour toutes les contenants de l'exercice
                    ArrayList<Contenant> listContenant = ListContenant.getLisContenant();
                    for(int i = 0; i< listContenant.size(); i++){
                        // on récupère le contenant
                        c = listContenant.get(i);
                        // on l'ordonne
                        c.ordonner();
                        // on compte le nombre d'objets contenus et on met à jour l'affichage machine, si il y en a un
                        c.countChild();
                        // on déselctionne le contenant
                        c.unSelected();
                    }
                    // on précise que le drag est fini
                    onDrage = false;

                    // GESTION DES DÉPASSEMENTS DU NOMBRE D'ÉLÉMENTS AUTORISÉ
                    // BOITE 0
                    // si l'on dépasse le maximum autorisé de buchettes et que l'on veut afficher un message
            /*        if(ListContenant.boite0.depasseMaxBuchette() && ListContenant.boite0.showPopupBuchette){
                        // on ajoute une trace de génération de popup
                        Traces.addPopup();
                        // si le message existe, on l'affiche
                        if (ListContenant.boite0.popupBuchette != null) ListContenant.boite0.popupBuchette.show();
                    }
                    // si l'on dépasse le maximum autorisé de groupements de 10 et que l'on veut afficher un message
                    if(ListContenant.boite0.depasseMaxGroupement10() && ListContenant.boite0.showPopupGroupement10){
                        // on ajoute une trace de génération de popup
                        Traces.addPopup();
                        // si le message existe, on l'affiche
                        if (ListContenant.boite0.popupGroupement10 != null) ListContenant.boite0.popupGroupement10.show();
                    }
                    // si l'on dépasse le maximum autorisé de groupements de 100 et que l'on veut afficher un message
                    if(ListContenant.boite0.depasseMaxGroupement100() && ListContenant.boite0.showPopupGroupement100){
                        // on ajoute une trace de génération de popup
                        Traces.addPopup();
                        // si le message existe, on l'affiche
                        if (ListContenant.boite0.popupGroupement100 != null) ListContenant.boite0.popupGroupement100.show();
                    }
                    // si l'on dépasse le maximum autorisé de groupements de 100b et que l'on veut afficher un message
                    if(ListContenant.boite0.depasseMaxGroupement100b() && ListContenant.boite0.showPopupGroupement100b){
                        // on ajoute une trace de génération de popup
                        Traces.addPopup();
                        // si le message existe, on l'affiche
                        if (ListContenant.boite0.popupGroupement100b != null) ListContenant.boite0.popupGroupement100b.show();
                    }

                    // BOITE 1
                    // même principe que pour la boite 0
                    if(ListContenant.boite1.depasseMaxBuchette() && ListContenant.boite1.showPopupBuchette){
                        Traces.addPopup();
                        if (ListContenant.boite1.popupBuchette != null) ListContenant.boite1.popupBuchette.show();
                    }
                    if(ListContenant.boite1.depasseMaxGroupement10() && ListContenant.boite1.showPopupGroupement10){
                        Traces.addPopup();
                        if (ListContenant.boite1.popupGroupement10 != null) ListContenant.boite1.popupGroupement10.show();
                    }
                    if(ListContenant.boite1.depasseMaxGroupement100() && ListContenant.boite1.showPopupGroupement100){
                        Traces.addPopup();
                        if (ListContenant.boite1.popupGroupement100 != null) ListContenant.boite1.popupGroupement100.show();
                    }
                    if(ListContenant.boite1.depasseMaxGroupement100b() && ListContenant.boite1.showPopupGroupement100b){
                        Traces.addPopup();
                        if (ListContenant.boite1.popupGroupement100b != null) ListContenant.boite1.popupGroupement100b.show();
                    }

                    // BOITE 2
                    // même principe que pour la boite 0
                    if(ListContenant.boite2.depasseMaxBuchette() && ListContenant.boite2.showPopupBuchette){
                        Traces.addPopup();
                        if (ListContenant.boite2.popupBuchette != null) ListContenant.boite2.popupBuchette.show();
                    }
                    if(ListContenant.boite2.depasseMaxGroupement10() && ListContenant.boite2.showPopupGroupement10){
                        Traces.addPopup();
                        if (ListContenant.boite2.popupGroupement10 != null) ListContenant.boite2.popupGroupement10.show();
                    }
                    if(ListContenant.boite2.depasseMaxGroupement100() && ListContenant.boite2.showPopupGroupement100){
                        Traces.addPopup();
                        if (ListContenant.boite2.popupGroupement100 != null) ListContenant.boite2.popupGroupement100.show();
                    }
                    if(ListContenant.boite2.depasseMaxGroupement100b() && ListContenant.boite2.showPopupGroupement100b){
                        Traces.addPopup();
                        if (ListContenant.boite2.popupGroupement100b != null) ListContenant.boite2.popupGroupement100b.show();
                    }

                    // BOITE 3
                    // même principe que pour la boite 0
                    if(ListContenant.boite3.depasseMaxBuchette() && ListContenant.boite3.showPopupBuchette){
                        Traces.addPopup();
                        if (ListContenant.boite3.popupBuchette != null) ListContenant.boite3.popupBuchette.show();
                    }
                    if(ListContenant.boite3.depasseMaxGroupement10() && ListContenant.boite3.showPopupGroupement10){
                        Traces.addPopup();
                        if (ListContenant.boite3.popupGroupement10 != null) ListContenant.boite3.popupGroupement10.show();
                    }
                    if(ListContenant.boite3.depasseMaxGroupement100() && ListContenant.boite3.showPopupGroupement100){
                        Traces.addPopup();
                        if (ListContenant.boite3.popupGroupement100 != null) ListContenant.boite3.popupGroupement100.show();
                    }
                    if(ListContenant.boite3.depasseMaxGroupement100b() && ListContenant.boite3.showPopupGroupement100b){
                        Traces.addPopup();
                        if (ListContenant.boite3.popupGroupement100b != null) ListContenant.boite3.popupGroupement100b.show();
                    }
                    */
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return onDrage;
        }
    };


    /**
     * Constructeur de table
     * @param t le RelativeLayout correspondant dans le fichier xml
     * @param c le contexte de l'application
     */
    public Table(RelativeLayout t, Context c){
        super(c);
        // on récupère le RelativeLayout de la table
        table = t;
        // on précise le draglistener (pour permettre le dépot d'objets)
        table.setOnDragListener(dragListener);
        // on precise que le relativeLayout est celui de la table
        table.setContentDescription(nom);
    }

    /**
     * Méthode permettant d'ordonner les objets contenus dans la table
     */
    public void ordonner(){
        ArrayList<View> listBuchettes = new ArrayList<>();
        // pour tous les objets déposés dans la table
        for(int i = 0; i < table.getChildCount(); i++){
            // on ajoute l'objet dans la liste
            listBuchettes.add(table.getChildAt(i));
        }
        // pour tous les objets déposés dans la table
        for(int j = listBuchettes.size() -1 ; j >= 0 ; j --){
            // on les enlèves de la table
            table.removeViewAt(j);
        }
        // pour tous les objets déposés dans la table
        for(int i = 0; i < listBuchettes.size(); i++){
            // on récupère l'objet dans la liste
            View view = listBuchettes.get(i);
            // on modifie sa position selon le nombre d'objets déja déposés dans la table
            if(Config.taille == 1){
                if(table.getChildCount() < 18){
                    view.setX(Parametres.X * (table.getChildCount() ));
                    // on veut que les objest soient sur la première ligne de la table
                    view.setY(0f);
                }
                if(table.getChildCount() >= 18){
                    view.setX(Parametres.X * (table.getChildCount() - 18));
                    view.setY(Parametres.Y_5L_T3);
                }

            }
            if(Config.taille == 2){
                if(table.getChildCount() < 18){
                    view.setX(Parametres.X * (table.getChildCount() ));
                    // on veut que les objest soient sur la première ligne de la table
                    view.setY(0f);
                }
                if(table.getChildCount() >= 18){
                    view.setX(Parametres.X * (table.getChildCount() - 18));
                    view.setY(Parametres.Y_2L_T2);
                }
            }
            if(Config.taille == 3){
                if(table.getChildCount() < 18){
                    view.setX(Parametres.X* (table.getChildCount() ));
                    // on veut que les objest soient sur la première ligne de la table
                    view.setY(0f);
                }
                if(table.getChildCount() >= 18 && table.getChildCount() < 36){
                    view.setX(Parametres.X * (table.getChildCount() - 18));
                    view.setY(Parametres.Y_2L_T3);
                }
                if(table.getChildCount() >= 36 && table.getChildCount() < 54 ){
                    view.setX(Parametres.X * (table.getChildCount() - 36));
                    view.setY(Parametres.Y_3L_T3);
                }
                if(table.getChildCount() >= 54){
                    view.setX(Parametres.X * (table.getChildCount() - 54));
                    view.setY(Parametres.Y_4L_T3);
                }
            }


            // on ajoute l'objet dans la table
            table.addView(view);
        }

    }

    @Override
    /**
     * Méthode permettant de compter le nombre d'objets déposés sur la table
     * @return le nombre d'objets déposés sur la table
     */
    public int countChild() {
        return table.getChildCount();
    }

    @Override
    /**
     * Méthode permettant de vérifier si l'on a dépassé le maximum de buchettes que l'on peut déposer sur la table
     * @return false (on ne peut pas dépasser le nombre maximal de la table)
     */
    public boolean depasseMaxBuchette() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de vérifier si l'on a dépassé le maximum de groupements de 10 que l'on peut déposer sur la table
     * @return false (on ne peut pas dépasser le nombre maximal de la table)
     */
    public boolean depasseMaxGroupement10() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de vérifier si l'on a dépassé le maximum de groupements de 100 que l'on peut déposer sur la table
     * @return false (on ne peut pas dépasser le nombre maximal de la table)
     */
    public boolean depasseMaxGroupement100() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de vérifier si l'on a dépassé le maximum de groupements de 100b que l'on peut déposer sur la table
     * @return false (on ne peut pas dépasser le nombre maximal de la table)
     */
    public boolean depasseMaxGroupement100b() {
        return false;
    }



    public boolean depasseMax() {
        return false;
    }

    /**
     * Méthode permettant de récupérer le nom de la table
     * @return "table"
     */
    public String getNom (){
        return nom;
    }

    @Override
    /**
     * Méthode permettant de récupérer le relativeLayout associé à la table
     * @return le RelativeLayout associé à la table
     */
    public RelativeLayout getRelativeLayout() {
        return table;
    }


    /**
     * Méthode permettant de tester si la table est vide (c-a-d si elle ne contient pas d'objet)
     * @return true si la table est vide
     */
    public boolean isVide(){
        boolean vide = false;
        if(table.getChildCount() > 0) vide = false;
        if(table.getChildCount() == 0) vide = true;
        return vide;
    }

}
