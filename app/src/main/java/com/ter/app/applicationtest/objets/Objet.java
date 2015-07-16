package com.ter.app.applicationtest.objets;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


/**
 * Classe représentant un objet "déplacable" dans l'application
 */
public abstract class Objet extends View{
    /**
     * Le contexte associé à l'objet
     */
    Context c;

    /**
     * ImageView représentant l'objet
     */
    protected ImageView image;

    /**
     * Le touchListener associé à l'objet
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
/*
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // on récupère l'action
            int action = MotionEventCompat.getActionMasked(event);
            // selon l'action qui a été exécutée
            switch (action) {
                // si l'utilisateur à appuyé sur la buchette
                case MotionEvent.ACTION_DOWN:
                    //permet le drag
                   // ClipData data = ClipData.newPlainText("", "");

                    if (BoutonSelectionner.onSelection) selected = !selected;
                    ImageView im =  new ImageView(c);
                    im.setImageResource(R.drawable.baton2_selec);

                    if(selected){
                        v.setBackgroundDrawable(im.getDrawable());
                    }
                    else v.setBackgroundDrawable(image.getDrawable());


                    // on déclanche le drag
                    //v.startDrag(data, shadowBuilder, v, 0);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE :
                    //permet le drag
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
                    //if (selected && event.getX() > x + DISTANCE_DRAG && event.getY() > y + DISTANCE_DRAG ){
                    if (!BoutonSelectionner.onSelection){
                        v.startDrag(data, shadowBuilder, v, 0);
                    }

                default:
            }
            return true;
        }
    };
*/



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
     * Constructeur d'un objet
     * @param c contexte de l'activité associée à l'objet
     * @param id l'identifiant correspondant à l'image (R.drawable.nomImage)
     */
    public Objet(Context c, int id, String type){
        super(c);
        this.c = c;
        // on créer l'ImageView associée à l'objet
        image = new ImageView(c);
        // on précise l'image associée à l'ImageView
        image.setImageResource(id);
        image.setContentDescription(type);
        // on ajoute un touchListener à l'objet
        image.setOnTouchListener(onTouchListener);
    }


    /**
     * Méthode retournant l'image de l'objet
     * @return l'ImageView associée à l'objet
     */
    public ImageView getImageView(){return image;}


    /**
     * Méthode permettant de modifier la visibilité de l'objet
     * @param visibile true si l'objet est visible, false sinon
     */
    public void setVisibility(boolean visibile){
        if(visibile) image.setVisibility(View.VISIBLE);
        else image.setVisibility(View.INVISIBLE);
    }

}



