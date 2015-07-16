package com.ter.app.applicationtest.Contenants;

/**
 * Created by myriam on 16/07/15.
 */
public class Parametres {
    /*public final static float X = 40f;
    public final static float Y_2L_T2 = 76f;
    public final static float Y_2L_T3 = 50f;
    public final static float Y_3L_T2 = 160f;
    public final static float Y_3L_T3 = 100f;
    public final static float Y_4L_T3 = 150f;
    public final static float Y_5L_T3 = 200f;*/

    public static float X = 80f;
    public static float Y_2L_T2 = 150f;
    public static float Y_2L_T3 = 100f;
    public static float Y_3L_T2 = 320f;
    public static float Y_3L_T3 = 200f;
    public static float Y_4L_T3 = 300f;
    public static float Y_5L_T3 = 400f;


    public Parametres(boolean isAsus){
        if(isAsus){
            X = 40f;
            Y_2L_T2 = 76f;
            Y_2L_T3 = 50f;
            Y_3L_T2 = 160f;
            Y_3L_T3 = 100f;
            Y_4L_T3 = 150f;
            Y_5L_T3 = 200f;
        }else{
            X = 80f;
            Y_2L_T2 = 150f;
            Y_2L_T3 = 100f;
            Y_3L_T2 = 320f;
            Y_3L_T3 = 200f;
            Y_4L_T3 = 300f;
            Y_5L_T3 = 400f;
        }
    }

}
