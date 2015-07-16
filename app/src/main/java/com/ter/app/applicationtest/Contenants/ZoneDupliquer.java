package com.ter.app.applicationtest.Contenants;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.Buchette;
import com.ter.app.applicationtest.objets.Groupement10;
import com.ter.app.applicationtest.objets.Groupement100;
import com.ter.app.applicationtest.objets.Groupement100B;
import com.ter.app.applicationtest.objets.Groupement3;

import java.util.ArrayList;

/**
 * Classe représentant la zone qui permet de dupliquer un objet
 */
public class ZoneDupliquer extends Contenant{

    /**
     * Le RelativeLayout associé à la zone pour dupliquer
     */
    private RelativeLayout zone;

    /**
     * Le contexte (de l'activité) associé à la zone pour dupliquer
     */
    private Context c;

    /**
     * Le nom associé au RelativeLayout de la zone pour dupliquer
     */
    private String nom = "zoneDupliquer";


    /**
     * Le dragListener de la zone pour dupliquer
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
                    // récupère la vue déposée
                    View view = (View) event.getLocalState();
                    // si l'objet que l'on veut déposer est un selectAll alors la zone dupliquer ne réagit pas
                    if(view.getContentDescription().equals("selectAll"));
                    // sinon, si ce n'est pas un selectAll
                    else {
                        // on récupère le parent de la view
                        ViewGroup owner = (ViewGroup) view.getParent();
                        // enlève la vue de ce parent
                        owner.removeView(view);
                        // on récupère la description de l'objet que l'on veut déposer
                        String objet = view.getContentDescription().toString();
                        // si il y a déjà au moins un objet dans la zone pour dupliquer
                        if (zone.getChildCount() > 0) {
                            // si l'objet que l'on veut déposé est différent de celui déjà présent dans la zone pour dupliquer
                            if (!objet.equals(zone.getChildAt(0).getContentDescription())) {
                                // on enlève tous les objets qui étaient dans la zone
                                zone.removeAllViews();
                                // on ajoute le nouvel objet
                                zone.addView(view);

                                View obj = view;
                                // on ajoute un à un des objets identiques à celui que l'on vient de déposer (on en met 300 pour avoir une impression d'infini)
                                for (int i = 0; i < 300; i++) {
                                    // on créer la bonne image correspondante selon le type de l'objet que l'on a déposé
                                    if (objet.equals("buchette")) {
                                        Buchette b = new Buchette(c);
                                        obj = b.getImageView();
                                    }
                                    if (objet.equals("groupement3")) {
                                        Groupement3 b = new Groupement3(c);
                                        obj = b.getImageView();
                                    }
                                    if (objet.equals("groupement10")) {
                                        Groupement10 b = new Groupement10(c);
                                        obj = b.getImageView();
                                    }
                                    if (objet.equals("groupement100")) {
                                        Groupement100 b = new Groupement100(c);
                                        obj = b.getImageView();
                                    }
                                    if (objet.equals("groupement100b")) {
                                        Groupement100B b = new Groupement100B(c);
                                        obj = b.getImageView();
                                    }
                                    // on ajoute cette image à la zone pour dupliquer
                                    zone.addView(obj);
                                }
                            }

                        }
                        // sinon (si il n'y avait aucun objet dans la zone pour dupliquer)
                        else {
                            // on ajoute directement des objets identiques à celui que l'on veut déposer dans la zone
                            View obj = view;
                            for (int i = 0; i < 300; i++) {
                                if (objet.equals("buchette")) {
                                    Buchette b = new Buchette(c);
                                    obj = b.getImageView();
                                }
                                if (objet.equals("groupement3")) {
                                    Groupement3 b = new Groupement3(c);
                                    obj = b.getImageView();
                                }
                                if (objet.equals("groupement10")) {
                                    Groupement10 b = new Groupement10(c);
                                    obj = b.getImageView();
                                }
                                if (objet.equals("groupement100")) {
                                    Groupement100 b = new Groupement100(c);
                                    obj = b.getImageView();
                                }
                                if (objet.equals("groupement100b")) {
                                    Groupement100B b = new Groupement100B(c);
                                    obj = b.getImageView();
                                }
                                zone.addView(obj);
                            }
                        }

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
                        // on ajoute le déplacement aux traces
                        Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), Contenant.getNomStatic(zone), "reussi", true);                    // on précise que le drag est fini
                        // on ajoute l'action de dupliquer aux traces
                        Traces.addDupliquer(objet);

                        // GESTION DES CAS DE DÉPASSEMENT DU NOMBRE AUTORISÉ D'ÉLÉMENTS DANS LES BOITES
                        // BOITE 0
                        // si, pour la boite 0, on dépasse le nombre autorisé de buchettes, et que l'on veut afficher un message
               /*         if(ListContenant.boite0.depasseMaxBuchette() && ListContenant.boite0.showPopupBuchette){
                            Traces.addPopup();
                            // si le message existe on l'affiche
                            if (ListContenant.boite0.popupBuchette != null) ListContenant.boite0.popupBuchette.show();
                        }
                        if(ListContenant.boite0.depasseMaxGroupement10() && ListContenant.boite0.showPopupGroupement10){
                            Traces.addPopup();
                            if (ListContenant.boite0.popupGroupement10 != null) ListContenant.boite0.popupGroupement10.show();
                        }
                        if(ListContenant.boite0.depasseMaxGroupement100() && ListContenant.boite0.showPopupGroupement100){
                            Traces.addPopup();
                            if (ListContenant.boite0.popupGroupement100 != null) ListContenant.boite0.popupGroupement100.show();
                        }
                        if(ListContenant.boite0.depasseMaxGroupement100b() && ListContenant.boite0.showPopupGroupement100b){
                            Traces.addPopup();
                            if (ListContenant.boite0.popupGroupement100b != null) ListContenant.boite0.popupGroupement100b.show();
                        }

                        // BOITE 1
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

                        onDrag = false;
                        break;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }
            return onDrag;
        }
    };


    /**
     * Constructeur de la zone pour dupliquer
     * @param context le contexte associé à la zone
     * @param r le RelativeLayout associé à la zone
     */
    public ZoneDupliquer(Context context, RelativeLayout r) {
        super(context);
        // on récupère le RelativeLayout associé à la zone dupliquer
        zone = r;
        // on précise que le RelativeLayout est celui de la zone pour dupliquer
        zone.setContentDescription(nom);
        // on ajoute le draglistener à la zone dupliquer
        zone.setOnDragListener(dragListener);
        // on récupère le contexte associé à la zone dupliquer
        c = context;

    }

    @Override
    /**
     * Méthode permettant d'ordonner la zone pour dupliquer
     */
    public void ordonner() {
        View v;
        // on place toutes les vues à la même place
        for(int i = 0; i<zone.getChildCount(); i++){
            v = zone.getChildAt(i);
            v.setX(Parametres.X);
            v.setY(0f);
        }
    }

    @Override
    /**
     * Méthode permettant de récupérer le nombre d'objets présents dans la zone pour dupliquer
     * @return le nombre d'objets présents dans la zone
     */
    public int countChild() {
        return zone.getChildCount();
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de buchettes contenues dans la zone dupliquer dépasse le nombre maximal de buchettes autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de buchettes dans la zone dupliquer
     */
    public boolean depasseMaxBuchette() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 10 contenus dans la zone dupliquer dépasse le nombre maximal de groupements de 10 autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupements de 10 dans la zone dupliquer
     */
    public boolean depasseMaxGroupement10() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100 contenus dans la zone dupliquer dépasse le nombre maximal de groupements de 100 autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupements de 100 dans la zone dupliquer
     */
    public boolean depasseMaxGroupement100() {
        return false;
    }

    @Override
    /**
     * Méthode permettant de tester si le nombre de groupements de 100b contenus dans le contenant dépasse le nombre maximal de groupements de 100b autorisé
     * @return false, on ne peut pas dépasser le nombre autorisé de groupements de 100b dans la zone dupliquer
     */
    public boolean depasseMaxGroupement100b() {
        return false;
    }


    @Override
    /**
     * Méthode permettant de récupérer le nom associé à la zone pour dupliquer
     * @return le nom associé à la zone
     */
    public String getNom() {
        return nom;
    }

    @Override
    /**
     * Méthode permettant de récupérer le RelativeLayout associé à la zone pour dupliquer
     * @return le RelativeLayout associé à la zone
     */
    public RelativeLayout getRelativeLayout() {
        return zone;
    }

}
