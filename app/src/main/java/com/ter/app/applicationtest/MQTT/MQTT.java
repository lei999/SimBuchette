package com.ter.app.applicationtest.MQTT;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ter.app.applicationtest.Contenants.ListContenant;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Groupement;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Grandes_Boites;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Terminer;
import com.ter.app.applicationtest.FenetresActivity.Exercice_Simple;
import com.ter.app.applicationtest.FenetresActivity.Nom_Eleve;
import com.ter.app.applicationtest.Parser.Parser;
import com.ter.app.applicationtest.feedbacks.Alert;
import com.ter.app.applicationtest.feedbacks.Traces;
import com.ter.app.applicationtest.parametresExercices.Config;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by myriam on 02/07/15.
 */
public class MQTT {
    public class MQTTMessage {
        public String topic, message;

        public MQTTMessage(String topic, String message) {
            this.topic = topic;
            this.message = message;
        }
    }


    enum TOPIC {INSTRUCTIONS}
    enum ContentType {START_DEFAULT, PAUSE, RESUME, START_MODIFIED}


    //region Properties
    private static MQTT INSTANCE = new MQTT();

    private String host = "tcp://10.0.0.5:1883";
    private String userName = "user";
    private String password = "password";

    private MqttClient client;
    private MqttConnectOptions connectOptions;
    private String clientId;

    private Handler handler;

    private String studentName;

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    private String messageContent;
    private String messageTopic;
    private String[] messageContentSplitString;
    private String messageContentType;

    private Activity currentActivity;

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public static MQTT getInstance() {
        return INSTANCE;
    }
    //endregion

    private MQTT() {
        clientId = String.valueOf(new Random().nextInt(10000));
        init();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                try {
                    client.subscribe(new String[]{
                            "INSTRUCTIONS"

                    });
                } catch (Exception e) {
                }
                // Ici, la tablette reçoit un message
                handleArrivingMessage(msg);
                return false;
            }
        });
        startReconnect();
    }

    private void init() {
        try {
            client = new MqttClient(host, clientId, new MemoryPersistence());
            connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            connectOptions.setUserName(userName);
            connectOptions.setPassword(password.toCharArray());
            connectOptions.setConnectionTimeout(120);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                //On traite un message à envoyer
                @Override
                public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
                    String s = new String(message.getPayload());
                    Message msg = new Message();
                    msg.obj = new MQTTMessage(topic.getName(), message.toString());
                    handler.sendMessage(msg);
                }

                @Override
                public void deliveryComplete(MqttDeliveryToken token) {
                }
            });
        } catch (Exception e) {
        }
    }

    public boolean sendMessage(String topic, String message) {
       if(!Nom_Eleve.autonome) {
           MqttTopic mqttTopic = client.getTopic(topic);
           MqttMessage mqttMessage = new MqttMessage();
           mqttMessage.setPayload(message.getBytes());
           try {
               Log.d("MQTT", "TOPIC : " + topic + "; MESSAGE : " + message);
               mqttTopic.publish(mqttMessage);
               return true;
           } catch (MqttException e) {
               return false;
           }
       }else{
           return false;
       }
    }

    private boolean isMessageEmpty(Message message) {
        return message.obj == null;
    }



    private void handleArrivingMessage(Message message) {
        if (!isMessageEmpty(message)) {

            MQTTMessage mqttMessage = (MQTTMessage) message.obj;
            messageTopic = mqttMessage.topic;
            if(messageTopic.equals(TOPIC.INSTRUCTIONS.name())){
                Log.d("MQTT reçu", mqttMessage.message);

                messageContent = mqttMessage.message;
                try {
                    JSONObject object = new JSONObject(messageContent);
                    String contentType = object.getString("contentType");
                    String stringIdEleve = object.getString("idEleve");
                    int idEleve = new Integer(stringIdEleve);
                    String code_exercice ="";
                    try {
                        code_exercice = object.getString("code_exercice");
                    }catch (JSONException e ){
                        e.printStackTrace();
                    }
                    Log.d("MQTT test", "id ?" + idEleve + " : " + Traces.id);
                    if (idEleve == Traces.id){
                        Log.d("MQTT test", "id ok");
                        if(contentType.equals(ContentType.START_DEFAULT.name()))
                        {
                            Log.d("MQTT test", "contentType ok");
                            if(currentActivity instanceof Nom_Eleve) {
                                Log.d("MQTT test", "activity ok");
                                Nom_Eleve.dialog.cancel();
                                Parser p = new Parser(currentActivity, code_exercice);
                                ((Nom_Eleve) currentActivity).startExercice(code_exercice);
                            }
                            if(currentActivity instanceof Exercice_Terminer) {
                                Exercice_Terminer.dialog.cancel();
                                Parser p = new Parser(currentActivity, code_exercice);
                                ((Exercice_Terminer) currentActivity).startExercice(code_exercice);
                            }
                            if(currentActivity instanceof Exercice_Groupement) {
                                if(!Exercice_Groupement.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        miseAjour();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Groupement.attente = builder.create();
                                        Exercice_Groupement.attente.show();
                                    }
                                    else{
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        changeExercice(code_exercice);
                                    }

                                }
                            }
                            if(currentActivity instanceof Exercice_Grandes_Boites) {
                                if(!Exercice_Grandes_Boites.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        miseAjour();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Grandes_Boites.attente = builder.create();
                                        Exercice_Grandes_Boites.attente.show();
                                    }
                                    else{
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        changeExercice(code_exercice);
                                    }
                                }
                            }
                            if(currentActivity instanceof Exercice_Simple) {
                                if(!Exercice_Simple.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        miseAjour();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Simple.attente = builder.create();
                                        Exercice_Simple.attente.show();
                                    }
                                }
                                else{
                                    Parser p = new Parser(currentActivity, code_exercice);
                                    changeExercice(code_exercice);
                                }
                            }
                        }
                        if(contentType.equals(ContentType.START_MODIFIED.name()))
                        {

                            if(currentActivity instanceof Nom_Eleve) {
                                Nom_Eleve.dialog.cancel();
                                Parser p = new Parser(currentActivity, code_exercice);
                                recuperationRegles(object);
                                ((Nom_Eleve) currentActivity).startExercice(code_exercice);
                            }
                            if(currentActivity instanceof Exercice_Terminer) {
                                Exercice_Terminer.dialog.cancel();
                                Parser p = new Parser(currentActivity, code_exercice);
                                recuperationRegles(object);
                                ((Exercice_Terminer) currentActivity).startExercice(code_exercice);
                            }
                            if(currentActivity instanceof Exercice_Groupement) {
                                if(!Exercice_Groupement.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Groupement.attente = builder.create();
                                        Exercice_Groupement.attente.show();
                                    }
                                    else{
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        changeExercice(code_exercice);
                                    }
                                }
                            }
                            if(currentActivity instanceof Exercice_Grandes_Boites) {
                                if(!Exercice_Grandes_Boites.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Grandes_Boites.attente = builder.create();
                                        Exercice_Grandes_Boites.attente.show();
                                    }
                                    else{
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        changeExercice(code_exercice);
                                    }
                                }
                            }
                            if(currentActivity instanceof Exercice_Simple) {
                                if(!Exercice_Simple.onPause) {
                                    if (code_exercice.equals(Traces.nomExercice)) {
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                                        builder.setTitle("Attention")
                                                .setMessage("Les règles ou les contraintes ont changées")
                                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                        dialog.dismiss();
                                                    }


                                                })
                                                .setCancelable(false);
                                        Exercice_Simple.attente = builder.create();
                                        Exercice_Simple.attente.show();
                                    }
                                    else{
                                        Parser p = new Parser(currentActivity, code_exercice);
                                        recuperationRegles(object);
                                        changeExercice(code_exercice);
                                    }
                                }
                            }
                        }
                        if(contentType.equals(ContentType.PAUSE.name()))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                            builder.setTitle("Ecran d'attente")
                                    .setMessage("Exercice en Pause")
                                    .setCancelable(false);
                            if(currentActivity instanceof Exercice_Groupement) {
                                if(!Exercice_Groupement.onPause) {
                                    Exercice_Groupement.onPause = true;
                                    Exercice_Groupement.attente = builder.create();
                                    Exercice_Groupement.attente.show();
                                }
                            }
                            if(currentActivity instanceof Exercice_Simple) {
                                if(!Exercice_Simple.onPause) {
                                    Exercice_Simple.onPause = true;
                                    Exercice_Simple.attente = builder.create();
                                    Exercice_Simple.attente.show();
                                }
                            }
                            if(currentActivity instanceof Exercice_Grandes_Boites) {
                                if(!Exercice_Grandes_Boites.onPause) {
                                    Exercice_Simple.onPause = true;
                                    Exercice_Grandes_Boites.attente = builder.create();
                                    Exercice_Grandes_Boites.attente.show();
                                }
                            }
                        }
                        if(contentType.equals(ContentType.RESUME.name()))
                        {
                            if(currentActivity instanceof Exercice_Groupement) {
                                Exercice_Groupement.onPause = false;
                                Exercice_Groupement.attente.cancel();
                            }
                            if(currentActivity instanceof Exercice_Simple) {
                                Exercice_Simple.onPause = false;
                                Exercice_Simple.attente.cancel();
                            }
                            if(currentActivity instanceof Exercice_Grandes_Boites) {
                                Exercice_Simple.onPause = false;
                                Exercice_Grandes_Boites.attente.cancel();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void changeExercice(String exercice) {
        Traces.resetTracesEleve();
        Traces.nomExercice = exercice;
        if(Config.utilisation_zone_groupements){
            Intent intent = new Intent(currentActivity, Exercice_Groupement.class);
            // démarre l'activité suivante
            currentActivity.startActivity(intent);
            Exercice_Groupement.changed = true;

        }
        else{
            if(Config.exercice_grandes_boites){
                Intent intent = new Intent(currentActivity, Exercice_Grandes_Boites.class);
                // démarre l'activité suivante
                currentActivity.startActivity(intent);
                Exercice_Grandes_Boites.changed = true;
            }
            else {
                Intent intent = new Intent(currentActivity, Exercice_Simple.class);
                // démarre l'activité suivante
                currentActivity.startActivity(intent);
                Exercice_Simple.changed = true;
            }
        }
    }

   public void recuperationRegles(JSONObject object){
        try {
            JSONObject regles = object.getJSONObject("regles_groupements");
            try{
                JSONObject groupement10 = regles.getJSONObject("groupement_10");
                if(groupement10 !=  null) {
                    String groupement_10_possible = groupement10.getString("autorise");
                    if(groupement_10_possible != null) {
                        if (groupement_10_possible.equals("oui"))
                            Config.groupement10_possible = true;

                        if (groupement_10_possible.equals("non"))
                            Config.groupement10_possible = false;
                    }

                    String comportement_si_erreur = groupement10.getString("comportement_si_erreur");
                    if(comportement_si_erreur != null){
                        if (comportement_si_erreur.equals("autorisation_action"))
                            Config.regle_groupement10_action = Alert.Action.AUTORISATION_ACTION;
                        if (comportement_si_erreur.equals("refus_action"))
                            Config.regle_groupement10_action = Alert.Action.REFUS_ACTION;
                        if (comportement_si_erreur.equals("autorisation_et_message"))
                            Config.regle_groupement10_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                        if (comportement_si_erreur.equals("refus_et_message"))
                            Config.regle_groupement10_action = Alert.Action.REFUS_ET_MESSAGE;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try{
                JSONObject groupement100 = regles.getJSONObject("groupement_100");if(groupement100 !=  null) {
                    String groupement_100_possible = groupement100.getString("autorise");
                    if(groupement_100_possible != null) {
                        if (groupement_100_possible.equals("oui"))
                            Config.groupement100_possible = true;

                        if (groupement_100_possible.equals("non"))
                            Config.groupement100_possible = false;
                    }

                    String comportement_si_erreur = groupement100.getString("comportement_si_erreur");
                    if(comportement_si_erreur != null){
                        if (comportement_si_erreur.equals("autorisation_action"))
                            Config.regle_groupement100_action = Alert.Action.AUTORISATION_ACTION;
                        if (comportement_si_erreur.equals("refus_action"))
                            Config.regle_groupement100_action = Alert.Action.REFUS_ACTION;
                        if (comportement_si_erreur.equals("autorisation_et_message"))
                            Config.regle_groupement100_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                        if (comportement_si_erreur.equals("refus_et_message"))
                            Config.regle_groupement100_action = Alert.Action.REFUS_ET_MESSAGE;
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }

            try{
                JSONObject groupement100b = regles.getJSONObject("groupement_100b");
                    String groupement_100b_possible = groupement100b.getString("autorise");
                    if(groupement_100b_possible != null) {
                        if (groupement_100b_possible.equals("oui"))
                            Config.groupement_simple = true;

                        if (groupement_100b_possible.equals("non"))
                            Config.groupement_simple = false;

                    }

                    String comportement_si_erreur = groupement100b.getString("comportement_si_erreur");
                    if(comportement_si_erreur != null){
                        if (comportement_si_erreur.equals("autorisation_action"))
                            Config.regle_groupement100b_action = Alert.Action.AUTORISATION_ACTION;
                        if (comportement_si_erreur.equals("refus_action"))
                            Config.regle_groupement100b_action = Alert.Action.REFUS_ACTION;
                        if (comportement_si_erreur.equals("autorisation_et_message"))
                            Config.regle_groupement100b_action = Alert.Action.AUTORISATION_ET_MESSAGE;
                        if (comportement_si_erreur.equals("refus_et_message"))
                            Config.regle_groupement100b_action = Alert.Action.REFUS_ET_MESSAGE;
                    }
            }catch (JSONException e) {
                    e.printStackTrace();
                }




        } catch (JSONException e) {
            e.printStackTrace();
        }
       try {
           JSONObject contraintes = object.getJSONObject("contraintes");
           try{
               JSONObject boite_1 = contraintes.getJSONObject("boite_1");
               JSONObject contraintes_boite = boite_1.getJSONObject("contraintes_boite");
               JSONObject buchette = contraintes_boite.getJSONObject("buchette");
               String comportement_si_depassement = buchette.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite0_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupBuchette = false;
                           ListContenant.boite0.actionbuchette = Config.boite0_action_depassement_buchette;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite0_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actionbuchette = Config.boite0_action_depassement_buchette;
                           ListContenant.boite0.showPopupBuchette = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite0_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actionbuchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite0.showPopupBuchette = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite0_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actionbuchette = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite0.showPopupBuchette = true;
                       }
                   }
               }

               JSONObject groupement_10 = contraintes_boite.getJSONObject("groupement_10");
               comportement_si_depassement = groupement_10.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite0_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement10 = false;
                           ListContenant.boite0.actiongroupement10 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite0_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actiongroupement10 = Alert.Action.REFUS_ACTION;
                           ListContenant.boite0.showPopupGroupement10 = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")){
                   Config.boite0_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actiongroupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite0.showPopupGroupement10 = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite0_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.actiongroupement10 = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite0.showPopupGroupement10 = true;
                       }
                   }
               }

               JSONObject groupement_100 = contraintes_boite.getJSONObject("groupement_100");
               comportement_si_depassement = groupement_100.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite0_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100 = false;
                           ListContenant.boite0.actiongroupement100 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite0_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100 = false;
                           ListContenant.boite0.actiongroupement100 = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite0_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100 = true;
                           ListContenant.boite0.actiongroupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite0_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100 = true;
                           ListContenant.boite0.actiongroupement100 = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }

               JSONObject groupement_100b = contraintes_boite.getJSONObject("groupement_100b");
               comportement_si_depassement = groupement_100b.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite0_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100b = false;
                           ListContenant.boite0.actiongroupement100b = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")) {
                   Config.boite0_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100b = false;
                           ListContenant.boite0.actiongroupement100b = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite0_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100b = true;
                           ListContenant.boite0.actiongroupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite0_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite0 != null) {
                           ListContenant.boite0.showPopupGroupement100b = true;
                           ListContenant.boite0.actiongroupement100b = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }

           } catch (JSONException e) {
               e.printStackTrace();
           }

           try{
               JSONObject boite_2 = contraintes.getJSONObject("boite_2");
               JSONObject contraintes_boite = boite_2.getJSONObject("contraintes_boite");
               JSONObject buchette = contraintes_boite.getJSONObject("buchette");
               String comportement_si_depassement = buchette.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite1_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupBuchette = false;
                           ListContenant.boite1.actionbuchette = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite1_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actionbuchette = Alert.Action.REFUS_ACTION;
                           ListContenant.boite1.showPopupBuchette = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite1_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actionbuchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite1.showPopupBuchette = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite1_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actionbuchette = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite1.showPopupBuchette = true;
                       }
                   }
               }

               JSONObject groupement_10 = contraintes_boite.getJSONObject("groupement_10");
               comportement_si_depassement = groupement_10.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite1_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement10 = false;
                           ListContenant.boite1.actiongroupement10 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite1_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actiongroupement10 = Alert.Action.REFUS_ACTION;
                           ListContenant.boite1.showPopupGroupement10 = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")){
                   Config.boite1_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actiongroupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite1.showPopupGroupement10 = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite1_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.actiongroupement10 = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite1.showPopupGroupement10 = true;
                       }
                   }
               }

               JSONObject groupement_100 = contraintes_boite.getJSONObject("groupement_100");
               comportement_si_depassement = groupement_100.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite1_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100 = false;
                           ListContenant.boite1.actiongroupement100 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite1_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100 = false;
                           ListContenant.boite1.actiongroupement100 = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite1_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100 = true;
                           ListContenant.boite1.actiongroupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite1_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100 = true;
                           ListContenant.boite1.actiongroupement100 = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }

               JSONObject groupement_100b = contraintes_boite.getJSONObject("groupement_100b");
               comportement_si_depassement = groupement_100b.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite1_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100b = false;
                           ListContenant.boite1.actiongroupement100b = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")) {
                   Config.boite1_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100b = false;
                           ListContenant.boite1.actiongroupement100b = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite1_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100b = true;
                           ListContenant.boite1.actiongroupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite1_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite1 != null) {
                           ListContenant.boite1.showPopupGroupement100b = true;
                           ListContenant.boite1.actiongroupement100b = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           try{
               JSONObject boite_3 = contraintes.getJSONObject("boite_3");
               JSONObject contraintes_boite = boite_3.getJSONObject("contraintes_boite");
               JSONObject buchette = contraintes_boite.getJSONObject("buchette");
               String comportement_si_depassement = buchette.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite2_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupBuchette = false;
                           ListContenant.boite2.actionbuchette = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite2_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actionbuchette = Alert.Action.REFUS_ACTION;
                           ListContenant.boite2.showPopupBuchette = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite2_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actionbuchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite2.showPopupBuchette = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite2_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actionbuchette = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite2.showPopupBuchette = true;
                       }
                   }
               }

               JSONObject groupement_10 = contraintes_boite.getJSONObject("groupement_10");
               comportement_si_depassement = groupement_10.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite2_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement10 = false;
                           ListContenant.boite2.actiongroupement10 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite2_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actiongroupement10 = Alert.Action.REFUS_ACTION;
                           ListContenant.boite2.showPopupGroupement10 = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")){
                   Config.boite2_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actiongroupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite2.showPopupGroupement10 = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite2_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.actiongroupement10 = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite2.popupGroupement10 = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement10_refuse);
                           ListContenant.boite2.showPopupGroupement10 = true;
                       }
                   }
               }

               JSONObject groupement_100 = contraintes_boite.getJSONObject("groupement_100");
               comportement_si_depassement = groupement_100.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite2_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100 = false;
                           ListContenant.boite2.actiongroupement100 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite2_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100 = false;
                           ListContenant.boite2.actiongroupement100 = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite2_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100 = true;
                           ListContenant.boite2.actiongroupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite2_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100 = true;
                           ListContenant.boite2.actiongroupement100 = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }

               JSONObject groupement_100b = contraintes_boite.getJSONObject("groupement_100b");
               comportement_si_depassement = groupement_100b.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite2_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100b = false;
                           ListContenant.boite2.actiongroupement100b = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")) {
                   Config.boite2_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100b = false;
                           ListContenant.boite2.actiongroupement100b = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite2_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100b = true;
                           ListContenant.boite2.actiongroupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite2_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite2 != null) {
                           ListContenant.boite2.showPopupGroupement100b = true;
                           ListContenant.boite2.actiongroupement100b = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           try{
               JSONObject boite_4 = contraintes.getJSONObject("boite_4");
               JSONObject contraintes_boite = boite_4.getJSONObject("contraintes_boite");
               JSONObject buchette = contraintes_boite.getJSONObject("buchette");
               String comportement_si_depassement = buchette.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite3_action_depassement_buchette = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupBuchette = false;
                           ListContenant.boite3.actionbuchette = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite3_action_depassement_buchette = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actionbuchette = Alert.Action.REFUS_ACTION;
                           ListContenant.boite3.showPopupBuchette = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite3_action_depassement_buchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actionbuchette = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite3.showPopupBuchette = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite3_action_depassement_buchette = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actionbuchette = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite3.showPopupBuchette = true;
                       }
                   }
               }

               JSONObject groupement_10 = contraintes_boite.getJSONObject("groupement_10");
               comportement_si_depassement = groupement_10.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite3_action_depassement_groupement10 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement10 = false;
                           ListContenant.boite3.actiongroupement10 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite3_action_depassement_groupement10 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actiongroupement10 = Alert.Action.REFUS_ACTION;
                           ListContenant.boite3.showPopupGroupement10 = false;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")){
                   Config.boite3_action_depassement_groupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actiongroupement10 = Alert.Action.AUTORISATION_ET_MESSAGE;
                           ListContenant.boite3.showPopupGroupement10 = true;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")){
                   Config.boite3_action_depassement_groupement10 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.actiongroupement10 = Alert.Action.REFUS_ET_MESSAGE;
                           ListContenant.boite3.showPopupGroupement10 = true;
                       }
                   }
               }

               JSONObject groupement_100 = contraintes_boite.getJSONObject("groupement_100");
               comportement_si_depassement = groupement_100.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")){
                   Config.boite3_action_depassement_groupement100 = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100 = false;
                           ListContenant.boite3.actiongroupement100 = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")){
                   Config.boite3_action_depassement_groupement100 = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100 = false;
                           ListContenant.boite3.actiongroupement100 = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite3_action_depassement_groupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100 = true;
                           ListContenant.boite3.actiongroupement100 = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite3_action_depassement_groupement100 = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100 = true;
                           ListContenant.boite3.actiongroupement100 = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }

               JSONObject groupement_100b = contraintes_boite.getJSONObject("groupement_100b");
               comportement_si_depassement = groupement_100b.getString("comportement_si_depassement");
               if (comportement_si_depassement.equals("autorisation_action")) {
                   Config.boite3_action_depassement_groupement100b = Alert.Action.AUTORISATION_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100b = false;
                           ListContenant.boite3.actiongroupement100b = Alert.Action.AUTORISATION_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_action")) {
                   Config.boite3_action_depassement_groupement100b = Alert.Action.REFUS_ACTION;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100b = false;
                           ListContenant.boite3.actiongroupement100b = Alert.Action.REFUS_ACTION;
                       }
                   }
               }
               if (comportement_si_depassement.equals("autorisation_et_message")) {
                   Config.boite3_action_depassement_groupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100b = true;
                           ListContenant.boite3.actiongroupement100b = Alert.Action.AUTORISATION_ET_MESSAGE;
                       }
                   }
               }
               if (comportement_si_depassement.equals("refus_et_message")) {
                   Config.boite3_action_depassement_groupement100b = Alert.Action.REFUS_ET_MESSAGE;
                   if (ListContenant.getLisContenant() != null) {
                       if (ListContenant.boite3 != null) {
                           ListContenant.boite3.showPopupGroupement100b = true;
                           ListContenant.boite3.actiongroupement100b = Alert.Action.REFUS_ET_MESSAGE;
                       }
                   }
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

       }catch (JSONException e ){
            e.printStackTrace();
            miseAjour();
       }

    }

    public void miseAjour(){

        // BOITE 0
        ListContenant.boite0.actionbuchette = Config.boite0_action_depassement_buchette;
        ListContenant.boite0.actiongroupement10 = Config.boite0_action_depassement_groupement10;
        ListContenant.boite0.actiongroupement100 = Config.boite0_action_depassement_groupement100;
        ListContenant.boite0.actiongroupement100b = Config.boite0_action_depassement_groupement100b;

        ListContenant.boite0.popupBuchette = new Alert(currentActivity, Config.boite0_message_si_depassement_buchette_autorise);
        ListContenant.boite0.popupBuchette_refus = new Alert(currentActivity, Config.boite0_message_si_depassement_buchette_refuse);

        ListContenant.boite0.popupGroupement10 = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement10_autorise);
        ListContenant.boite0.popupGroupement10_refus = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement10_refuse);

        ListContenant.boite0.popupGroupement100 = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement100_autorise);
        ListContenant.boite0.popupGroupement100_refus = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement100_refuse);

        ListContenant.boite0.popupGroupement100b = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement100b_autorise);
        ListContenant.boite0.popupGroupement100b_refus = new Alert(currentActivity, Config.boite0_message_si_depassement_groupement100b_refuse);



        // BOITE 1

        ListContenant.boite1.actionbuchette = Config.boite1_action_depassement_buchette;
        Log.d("MAJ", "" + Config.boite0_action_depassement_buchette);
        ListContenant.boite1.actiongroupement10 = Config.boite1_action_depassement_groupement10;
        ListContenant.boite1.actiongroupement100 = Config.boite1_action_depassement_groupement100;
        ListContenant.boite1.actiongroupement100b = Config.boite1_action_depassement_groupement100b;

        ListContenant.boite1.popupBuchette = new Alert(currentActivity, Config.boite1_message_si_depassement_buchette_autorise);
        ListContenant.boite1.popupBuchette_refus = new Alert(currentActivity, Config.boite1_message_si_depassement_buchette_refuse);

        ListContenant.boite1.popupGroupement10 = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement10_autorise);
        ListContenant.boite1.popupGroupement10_refus = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement10_refuse);

        ListContenant.boite1.popupGroupement100 = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement100_autorise);
        ListContenant.boite1.popupGroupement100_refus = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement100_refuse);

        ListContenant.boite1.popupGroupement100b = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement100b_autorise);
        ListContenant.boite1.popupGroupement100b_refus = new Alert(currentActivity, Config.boite1_message_si_depassement_groupement100b_refuse);



        // BOITE 2
        ListContenant.boite2.actionbuchette = Config.boite2_action_depassement_buchette;
        ListContenant.boite2.actiongroupement10 = Config.boite2_action_depassement_groupement10;
        ListContenant.boite2.popupGroupement10 = new Alert(currentActivity, Config.boite2_message_si_depassement_buchette_autorise);
        ListContenant.boite2.actiongroupement100 = Config.boite2_action_depassement_groupement100;
        ListContenant.boite2.actiongroupement100b = Config.boite2_action_depassement_groupement100b;


        ListContenant.boite2.popupBuchette = new Alert(currentActivity, Config.boite2_message_si_depassement_buchette_autorise);
        ListContenant.boite2.popupBuchette_refus = new Alert(currentActivity, Config.boite2_message_si_depassement_buchette_refuse);

        ListContenant.boite2.popupGroupement10 = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement10_autorise);
        ListContenant.boite2.popupGroupement10_refus = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement10_refuse);

        ListContenant.boite2.popupGroupement100 = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement100_autorise);
        ListContenant.boite2.popupGroupement100_refus = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement100_refuse);

        ListContenant.boite2.popupGroupement100b = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement100b_autorise);
        ListContenant.boite2.popupGroupement100b_refus = new Alert(currentActivity, Config.boite2_message_si_depassement_groupement100b_refuse);


        // BOITE 3
        ListContenant.boite3.actionbuchette = Config.boite3_action_depassement_buchette;
        ListContenant.boite3.actiongroupement10 = Config.boite3_action_depassement_groupement10;
        ListContenant.boite3.actiongroupement100 = Config.boite3_action_depassement_groupement100;
        ListContenant.boite3.actiongroupement100b = Config.boite3_action_depassement_groupement100b;

        ListContenant.boite3.popupBuchette = new Alert(currentActivity, Config.boite3_message_si_depassement_buchette_autorise);
        ListContenant.boite3.popupBuchette_refus = new Alert(currentActivity, Config.boite3_message_si_depassement_buchette_refuse);

        ListContenant.boite3.popupGroupement10 = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement10_autorise);
        ListContenant.boite3.popupGroupement10_refus = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement10_refuse);

        ListContenant.boite3.popupGroupement100 = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement100_autorise);
        ListContenant.boite3.popupGroupement100_refus = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement100_refuse);

        ListContenant.boite3.popupGroupement100b = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement100b_autorise);
        ListContenant.boite3.popupGroupement100b_refus = new Alert(currentActivity, Config.boite3_message_si_depassement_groupement100b_refuse);

    }





/*    private void messageContentTypeToMethodRedirection(MessageContentTypes contentType) {
        switch (contentType) {
            case STUDENTS_NAMES_LIST:
                updateConnectionActivityLinearLayoutForStudentsNamesList(messageContentSplitString[Constants.SESSION_ID_INDEX], messageContentSplitString[Constants.STUDENTS_NAMES_INDEX]);
                break;
            case EXERCISE_STARTED:
                startExercise();
                break;ssageCo
            case GROUP_COMPOSITION:
                startCollectivePhase();
                break;
            case WORD_SELECTED:
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 1; i < messageContentSplitString.length; i++) {
                    stringBuilder.append(messageContentSplitString[i])
                            .append(Constants.MESSAGE_DELIMITER);
                }
                updateJustificationActivity(stringBuilder.toString());
                break;
            case WORD_CANCELED:
                wordJustificationCanceled(messageContentSplitString[Constants.WORD_CANCELED_STUDENTS_NAMES]);
                break;
            case WORD_JUSTIFIED:
                String studentsNames = "";
                String wordPosition = messageContentSplitString[1];
                for (int i = 2; i < messageContentSplitString.length; i++) {
                    String studentsAndJustification = messageContentSplitString[i];
                    String students = studentsAndJustification.split(Constants.MESSAGE_ITEM_DELIMITER)[0];
                    studentsNames += stussageCodents;
                }
                wordJustified(studentsNames, wordPosition);
                break;
            default:
                break;
        }
    }


    //region Methods from currentActivity
    private void updateConnectionActivityLinearLayoutForStudentsNamesList(String sessionId, String studentsNamesString) {

        if (currentActivity instanceof ConnectionActivity) {
            ((ConnectionActivity) currentActivity).updateLinearLayoutWithStudentsNamesString(sessionId, studentsNamesString);
        }
    }

    private void startExercise() {
        // instance d'un nom d'activity
        if (currentActivity instanceof ConnectionActivity) {
            ((ConnectionActivity) currentActivity).startExercise();
        }
    }

    private void startCollectivePhase() {
        for (int i = 1; i < messageContentSplitString.length; i++) {
            String groupInfo = messageContentSplitString[i];
            String names = groupInfo.split(Constants.MESSAGE_ITEM_DELIMITER)[0];
            String dictation = groupInfo.split(Constants.MESSAGE_ITEM_DELIMITER)[1];

            if (names.contains(studentName)) {
                if (currentActivity instanceof IndividualDictationActivity) {
                    ((IndividualDictationActivity) currentActivity).startCollectivePhase(names, dictation);
                } else if (currentActivity instanceof PreDictationActivity) {
                    ((PreDictationActivity) currentActivity).startCollectivePhase(names, dictation);
                } else if (currentActivity instanceof KeyboardActivity) {
                    ((KeyboardActivity) currentActivity).startCollectivePhase(names, dictation);
                }
            }
        }
    }


    private void updateJustificationActivity(String mqttMessage) {
        String[] studentAndWord = mqttMessage.split(Constants.MESSAGE_DELIMITER);
        String index = studentAndWord[0];
        String studentsAndVotes = "";
        String studentsVotes = "";
        for (int i = 1; i < studentAndWord.length; i++) {
            studentsAndVotes += studentAndWord[i].split(Constants.MESSAGE_ITEM_DELIMITER)[0]
                    + Constants.MESSAGE_ITEM_DELIMITER
                    + studentAndWord[i].split(Constants.MESSAGE_ITEM_DELIMITER)[1]
                    + Constants.MESSAGE_DELIMITER;
            studentsVotes += studentAndWord[i].split(Constants.MESSAGE_ITEM_DELIMITER)[1]
                    + Constants.MESSAGE_ITEM_DELIMITER;
        }
        if (studentsAndVotes.contains(studentName)) {
            if (currentActivity instanceof JustificationActivity) {
                ((JustificationActivity) currentActivity).createGuidedJustificationLayout(studentsAndVotes, studentsVotes, index);
            }
        }
    }

    private void wordJustificationCanceled(String studentsNames) {
        if (studentsNames.contains(studentName)) {
            if (currentActivity instanceof GroupDictationActivity) {
                ((GroupDictationActivity) currentActivity).dismissWaitDialog();
            }
        }
    }

    private void wordJustified(String studentsNames, String wordPosition) {
        if (studentsNames.contains(studentName)) {
            if (currentActivity instanceof GroupDictationActivity) {
                ((GroupDictationActivity) currentActivity).updateLayoutForWordJustified(wordPosition);
            }
        }
    }
    //endregion
    */

    private void startReconnect() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);
    }

    private void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.connect(connectOptions);
                    Message message = new Message();
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message message = new Message();
                    handler.sendMessage(message);
                }
            }
        }).start();
    }
}
