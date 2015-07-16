package com.ter.app.applicationtest.gestionBoutons;


import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ter.app.applicationtest.Contenants.Boite;
import com.ter.app.applicationtest.Contenants.Contenant;
import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.feedbacks.Etat_simulation;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.ListObjets;
import com.ter.app.applicationtest.parametresExercices.Consigne;

import java.util.ArrayList;


/**
 * Classe représentant le bouton Vider
 */
public class BoutonVider {
    /**
     * La zone du bouton vider
     */
    RelativeLayout zone;

    /**
     * Le dragListener de la zone du bouton pour vider
     */
    private View.OnDragListener vider = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
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
                    // récupère le parent de la vue
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    int nb = 1;
                    ArrayList<Etat_simulation.Objets> s = new ArrayList<>();
                    s.add(Etat_simulation.stringToObjet(view.getContentDescription().toString()));
                    if(view.getContentDescription().equals("selectAll")){
                        s = ListObjets.typeObjets();
                        nb = ListObjets.listView.size();

                    View v1 = owner.getChildAt(0);
                    View v2 = owner.getChildAt(1);

                    owner.removeAllViews();
                    owner.addView(v1);
                    owner.addView(v2);

                        ListObjets.removeAll();
                    }

                    // RÉORGANISATION DES VUES DANS LES CONTENANTS DE FAÇON COHÉRENTE
                    Contenant c;
                    // pour toutes les contenants de l'exercice
                    for (int i = 0; i < ListContenant.getLisContenant().size(); i++) {
                        // on récupère le contenant
                        c = ListContenant.getLisContenant().get(i);
                        // on l'ordonne
                        c.ordonner();
                        c.unSelected();
                        // on compte le nombre d'objets contenus et on met à jour l'affichage machine
                        c.countChild();

                        // si l'on dépasse le maximum autorisé de la boite on affiche le popup d'alerte
                       // if (c.depasseMax() && c.showPopup) c.popup.show();

                    }

                    Traces.addActionVider(Boite.getNomStatic((RelativeLayout) owner),nb,s);
                    //Traces.addDeplacement(view.getContentDescription().toString(), Contenant.getNomStatic((RelativeLayout) owner), Contenant.getNomStatic(zone), "reussi");
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                default:
                    break;
            }




            return true;
        }
    };


    /**
     * Constructeur du bouton Vider
     * @param z le Button dans le xml, représentant le bouton
     */
    public BoutonVider(RelativeLayout z){
        zone = z;
        zone.setOnDragListener(vider);
    }

}
