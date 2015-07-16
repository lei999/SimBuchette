package com.ter.app.applicationtest.FenetresActivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ter.app.applicationtest.Contenants.Parametres;
import com.ter.app.applicationtest.R;

public class SelectionTablette extends ActionBarActivity {

    public static boolean quit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_tablette);

        Button a = (Button) findViewById(R.id.asus);
        Button s = (Button) findViewById(R.id.samsung);

        Button.OnClickListener asus = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parametres p = new Parametres(true);
                Intent intent = new Intent(SelectionTablette.this, Accueil.class);
                startActivity(intent);
            }
        };
        a.setOnClickListener(asus);
        Button.OnClickListener samsung = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parametres p = new Parametres(false);
                Intent intent = new Intent(SelectionTablette.this, Accueil.class);
                startActivity(intent);
            }
        };
        s.setOnClickListener(samsung);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection_tablette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode appélée lors que la fenêtre est affichée de nouveau
     */
    protected void onResume(){
        super.onResume();
        // si l'on est dans le processus pour quitter l'application
        if(quit){
            // on précise que cette fenêtre pourra se relancer
            quit = false;
            // on quitte cette fenêtre
            finish();
        }
    }
}
