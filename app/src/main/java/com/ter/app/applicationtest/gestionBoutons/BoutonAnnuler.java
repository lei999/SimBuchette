package com.ter.app.applicationtest.gestionBoutons;

import android.content.Context;
import android.widget.Button;

import com.ter.app.applicationtest.Contenants.Boite;
import com.ter.app.applicationtest.Contenants.Contenant;
import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.Contenants.ZoneGroupement;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.objets.Buchette;
import com.ter.app.applicationtest.objets.Groupement10;
import com.ter.app.applicationtest.objets.Groupement3;
import com.ter.app.applicationtest.objets.Objet;

import java.util.ArrayList;

/**
 * Classe représentant le bouton Annuler. N'EST PLUS UTILISÉ
 */
public class BoutonAnnuler extends Bouton {
    /**
     * Le contexte associé au bouton "annuler"
     */
    static Context context ;

    /**
     * Constructeur du bouton Annuler
     * @param b le Button dans le xml, représentant le bouton
     */
    public BoutonAnnuler(Button b, Context c){
        super();
        bouton = b;
        context = c;
        bouton.setOnClickListener(clickListener);
    }

    /**
     * Méthode permettant de modifier le contexte associé au bouton
     * @param c
     */
    public static void setContetxt (Context c){
        context = c;
    }

    /**
     * Méthode permettant de faire l'action d'annuler
     */
    public static void action() {
        if (Traces.traces.size() > 0) {
            ArrayList<Contenant> c = ListContenant.getLisContenant();
            String derniereAction = Traces.traces.get(Traces.traces.size() - 1);
            ZoneGroupement z = ListContenant.zoneGroupement;

            String typeAction = derniereAction.split(",")[0];
            if (typeAction.equals("deplacement")) {
                Traces.addActionAnnuler("deplacement");
                Traces.traces.remove(Traces.traces.size() - 1);
                String objet = derniereAction.split(",")[1];
                String source = derniereAction.split(",")[2];
                String destination = derniereAction.split(",")[3];
                // gestion du retour en arrière

                Objet objet1 = null;
                if (objet.equals("buchette")) {
                    objet1 = new Buchette(context);
                }
                if (objet.equals("groupement3")) {
                    objet1 = new Groupement3(context);
                }
                if (objet.equals("groupement10")) {
                    objet1 = new Groupement10(context);
                }
                // retirer l'objet de la destination
                // rechercher la destination
                boolean trouve = false;
                if(Boite.getNomStatic(z.getRelativeLayout()).equals(destination)){
                    int init = 0;
                    trouve = false;
                    for (int numv = init; numv < z.getRelativeLayout().getChildCount(); numv++) {
                        if (!trouve && z.getRelativeLayout().getChildAt(numv).getContentDescription().equals(objet)) {
                            trouve = true;

                            z.getRelativeLayout().removeView(z.getRelativeLayout().getChildAt(numv));
                            z.ordonner();
                            z.countChild();

                        }
                    }
                }
                for (int i = 0; i < c.size(); i++) {
                        if (Boite.getNomStatic(c.get(i).getRelativeLayout()).equals(destination)) {
                            int init = 2;
                            if (destination.equals("table ")) init = 0;
                            trouve = false;
                            for (int numv = init; numv < c.get(i).getRelativeLayout().getChildCount(); numv++) {
                                if (!trouve && c.get(i).getRelativeLayout().getChildAt(numv).getContentDescription().equals(objet)) {
                                    trouve = true;

                                    c.get(i).getRelativeLayout().removeView(c.get(i).getRelativeLayout().getChildAt(numv));
                                    c.get(i).ordonner();
                                    c.get(i).countChild();

                                }
                            }

                        }
                        trouve = false;
                        if (!trouve && Boite.getNomStatic(c.get(i).getRelativeLayout()).equals(source)) {
                            trouve = true;
                            if (!source.equals("reserve")) {
                                if (objet.equals("buchette")) {
                                    c.get(i).getRelativeLayout().addView(new Buchette(context).getImageView());
                                }
                                if (objet.equals("groupement3")) {
                                    c.get(i).getRelativeLayout().addView(new Groupement3(context).getImageView());
                                }
                                if (objet.equals("groupement10")) {
                                    c.get(i).getRelativeLayout().addView(new Groupement10(context).getImageView());
                                }
                                c.get(i).ordonner();
                                c.get(i).countChild();
                               /* if (c.get(i).depasseMax() && c.get(i).showPopup) {
                                    if (c.get(i).popup != null) c.get(i).popup.show();
                                }*/
                            }
                        }
                        if (!trouve && Boite.getNomStatic(z).equals(source)) {
                            if (objet.equals("buchette")) {
                                z.getRelativeLayout().addView(new Buchette(context).getImageView());
                            }
                            if (objet.equals("groupement3")) {
                                z.getRelativeLayout().addView(new Groupement3(context).getImageView());
                            }
                            if (objet.equals("groupement10")) {
                                z.getRelativeLayout().addView(new Groupement10(context).getImageView());
                            }
                            z.ordonner();
                            z.countChild();
                        }

                    }
            }
            if (typeAction.equals("vider")) {
                Traces.addActionAnnuler("vider");
                while (typeAction.equals("vider")) {
                    Traces.traces.remove(Traces.traces.size() - 1);
                    String objet = derniereAction.split(",")[2];
                    String source = derniereAction.split(",")[1];

                    boolean trouve = false;
                    for (int i = 0; i < c.size(); i++) {
                        if (!trouve && Boite.getNomStatic(c.get(i).getRelativeLayout()).equals(source)) {
                            trouve = true;
                            if (!source.equals("reserve")) {
                                if (objet.equals("buchette")) {
                                    c.get(i).getRelativeLayout().addView(new Buchette(context).getImageView());
                                }
                                if (objet.equals("groupement3")) {
                                    c.get(i).getRelativeLayout().addView(new Groupement3(context).getImageView());
                                }
                                if (objet.equals("groupement10")) {
                                    c.get(i).getRelativeLayout().addView(new Groupement10(context).getImageView());
                                }
                                c.get(i).ordonner();
                                c.get(i).countChild();
                               /* if (c.get(i).depasseMax() && c.get(i).showPopup) {
                                    if (c.get(i).popup != null) c.get(i).popup.show();
                                }*/
                            }
                        }
                    }
                    derniereAction = Traces.traces.get(Traces.traces.size() - 1);
                    typeAction = derniereAction.split(",")[0];
                }
            }
            if (typeAction.equals("grouper")) {
                Traces.addActionAnnuler("grouper");
                String resultat = derniereAction.split(",")[1];

                if (resultat.equals("réussi")){
                    z.getRelativeLayout().removeViewAt(0);
                    String[] string = derniereAction.split(",");

                    for(int i = 2; i < string.length; i++){
                        String s = string[i];
                        if(s.equals("buchette")){
                            Buchette b = new Buchette(context);
                            z.getRelativeLayout().addView(b.getImageView());
                        }
                    }
                    z.ordonner();
                    z.countChild();
                }
            }
            if (typeAction.equals("defaireGroupement")) {
                Traces.addActionAnnuler("defaireGroupement");
                String resultat = derniereAction.split(",")[1];
                if (resultat.equals("réussi")){
                    String groupement = derniereAction.split(",")[2];
                    if(groupement.equals("groupement3")){
                        z.getRelativeLayout().removeViewAt(z.getRelativeLayout().getChildCount()-1);
                        z.getRelativeLayout().removeViewAt(z.getRelativeLayout().getChildCount()-1);
                        z.getRelativeLayout().removeViewAt(z.getRelativeLayout().getChildCount()-1);
                        z.getRelativeLayout().addView(new Groupement3(context).getImageView());
                    }
                    if(groupement.equals("groupement10")){
                        for(int i =0; i <10; i++){
                            z.getRelativeLayout().removeViewAt(z.getRelativeLayout().getChildCount()-1);
                        }
                        z.getRelativeLayout().addView(new Groupement10(context).getImageView());
                    }
                    z.countChild();
                }
            }
        }
    }
}
