package com.ter.app.applicationtest.Contenants;


import android.content.Context;
import android.widget.RelativeLayout;
import com.ter.app.applicationtest.feedbacks.Alert;


/**
 * Classe représentant un contenant
 */
public abstract class Contenant extends RelativeLayout {

    /**
     * Attribut signalant si l'application doit afficher les popup
     */
    public boolean showPopup = false;

    /**
     * Attribut premettant de préciser si le contenant est sélectionné ou non
     */
    public boolean selected = false;

    /**
     * Constructeur de contenant
     * @param context le contexte associé au contenant
     */
    public Contenant(Context context) {
        super(context);
    }

    /**
     * Méthode permettant d'ordonner les objets contenus dans le contenant
     */
    public abstract void ordonner();

    /**
     * Méthode permettant de compter le nombre d'objets déposés dans le contenant et d'actualiser l'affichage machine en conséquence si il y a a un affichage machine
     * @return le nombre d'objets déposés dans la boite
     */
    public abstract int countChild();

    /**
     * Méthode permettant de tester si le nombre de buchettes contenues dans le contenant dépasse le nombre maximal de buchettes autorisé
     * @return true si le nombre de buchettes contenues dans le contenant dépasse le nombre maximal de buchettes autorisé
     */
    public abstract boolean depasseMaxBuchette();
    /**
     * Méthode permettant de tester si le nombre de groupements de 10 contenus dans le contenant dépasse le nombre maximal de groupements de 10 autorisé
     * @return true si le nombre de groupements de 10 contenus dans le contenant dépasse le nombre maximal de groupements de 10 autorisé
     */
    public abstract boolean depasseMaxGroupement10();
    /**
     * Méthode permettant de tester si le nombre de groupements de 100 contenus dans le contenant dépasse le nombre maximal de groupements de 100 autorisé
     * @return true si le nombre de groupements de 100 contenus dans le contenant dépasse le nombre maximal de groupements de 100 autorisé
     */
    public abstract boolean depasseMaxGroupement100();
    /**
     * Méthode permettant de tester si le nombre de groupements de 100b contenus dans le contenant dépasse le nombre maximal de groupements de 100b autorisé
     * @return true si le nombre de groupements de 100b contenus dans le contenant dépasse le nombre maximal de groupements de 100b autorisé
     */
    public abstract boolean depasseMaxGroupement100b();

    /**
     * Le nombre maximal de buchette autorisé dans la boite
     */
    int nbmaxBuchette;

    /**
     * La rétroaction a effectuer lorsque le nombre de buchettes dans la boite dépasse le nombre de buchettes autorisé
     */
    Alert.Action actionbuchette;

    /**
     * Permet de préciser si l'on doit afficher un message en cas de dépassement du nombre de buchettes autorisé
     */
    public boolean showPopupBuchette;

    /**
     * Le message à afficher lorsque le nombre de buchettes dans la boite dépasse le nombre de buchettes autorisé
     */
    Alert popupBuchette;

    /**
     * Le nombre maximal de groupement de 10 autorisé dans la boite
     */
    int nbmaxGroupement10;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 10 dans la boite dépasse le nombre de groupement de 10 autorisé
     */
    Alert.Action actiongroupement10;

    /**
     * Permet de préciser si l'on doit afficher un message en cas de dépassement du nombre de groupements de 10 autorisé
     */
    public boolean showPopupGroupement10;

    /**
     * Le message à afficher lorsque le nombre de groupement de 10 dans la boite dépasse le nombre de groupement de 10 autorisé
     */
    Alert popupGroupement10;

    /**
     * Le nombre maximal de groupement de 100 autorisé dans la boite
     */
    int nbmaxGroupement100;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 100 dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    Alert.Action actiongroupement100;

    /**
     * Permet de préciser si l'on doit afficher un message en cas de dépassement du nombre de groupements de 100 autorisé
     */
    public boolean showPopupGroupement100;

    /**
     * Le message à afficher lorsque le nombre de groupement de 100 dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    Alert popupGroupement100;

    /**
     * Le nombre maximal de groupement de 100b autorisé dans la boite
     */
    int nbmaxGroupement100b;

    /**
     * La rétroaction a effectuer lorsque le nombre de groupement de 100b dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    Alert.Action actiongroupement100b;

    /**
     * Permet de préciser si l'on doit afficher un message en cas de dépassement du nombre de groupements de 100b autorisé
     */
    public boolean showPopupGroupement100b;

    /**
     * Le message à afficher lorsque le nombre de groupement de 100b dans la boite dépasse le nombre de groupement de 100 autorisé
     */
    Alert popupGroupement100b;

    /**
     * Méthode permettant de récupérer le nom du contenant
     * @return le nom du contenant
     */
    public abstract String getNom ();


    /**
     * Méthode permettant de récupérer le nom associé à un RelativeLayout d'un contenant
     * @param r le RelativeLayout d'un contenant
     * @return le nom du contenant
     */
    public static String getNomStatic (RelativeLayout r){
        String n = "";
        String[] list ={""};
        String desc = (String)r.getContentDescription(); // les noms des contenants sont dans la descritpion du relativeLayout, de la forme nom;objetsautorisés
        if(desc != null) list = desc.split(";");
        if(list.length > 0) n = list[0];
        return n;
    }

    /**
     * Méthode permettant de récupérer le RelativeLayout associé au contenant
     * @return le RelativeLayout associé au contenant
     */
    public abstract RelativeLayout getRelativeLayout();

    /**
     * Méthode permettant de désélectionner le contenant
     */
    public void unSelected(){
        selected = false;
    }



}
