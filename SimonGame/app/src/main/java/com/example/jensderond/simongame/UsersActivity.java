package com.example.jensderond.simongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by jensderond on 16/01/2017.
 */

public class UsersActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private Realm realm;
    private Button buttonNewUser, buttonHelp;
    private ArrayList<Player> arrayListUsers = new ArrayList<>();
    private UsersListAdapter usersListAdapter;
    private ListView lvUsers;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        init();
    }

    public void init(){
        buttonNewUser       = (Button)          findViewById(R.id.button_newUser);
        buttonHelp          = (Button)          findViewById(R.id.button_help_user);
        lvUsers             = (ListView)        findViewById(R.id.lvUsers);
        lvUsers.setOnItemClickListener(this);
        lvUsers.setOnItemLongClickListener(this);
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        buttonNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(intent);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UsersActivity.this,
                        getApplicationContext().getString(R.string.help_user_delete), Toast.LENGTH_LONG).show();
            }
        });

//        arrayAdapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                arrayListUsers );

        realm = Realm.getDefaultInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String username = arrayListUsers.get(i).getName();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("cur_user", username);
        editor.apply();
        Log.d("User Clicked", username);

        Toast.makeText(UsersActivity.this,
                getApplicationContext().getString(R.string.cUser) + " " + username + " " + getApplicationContext().getString(R.string.selected) + "!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        final String username = arrayListUsers.get(i).getName();
        new CountDownTimer(500, 500) {

            public void onTick(long millisUntilFinished) {
                //here you can have your logic to set text to edittext
                Log.d("Timer", String.valueOf(millisUntilFinished));
            }

            public void onFinish() {

                Log.d("Timer", "finished");
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UsersActivity.this);

                // set title
                alertDialogBuilder.setTitle(getApplicationContext().getString(R.string.delete)  + " " +  username);

                // set dialog message
                alertDialogBuilder
                        .setMessage(getApplicationContext().getString(R.string.select_yes_to ) + " " + username + " " + getApplicationContext().getString(R.string.to_delete))
                        .setCancelable(false)
                        .setPositiveButton(getApplicationContext().getString(R.string.yes),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                Log.d("Delete user", username);
                                deleteUser(username);
                            }
                        })
                        .setNegativeButton(getApplicationContext().getString(R.string.no),new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                Log.d("Cancel delete user", username);
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        }.start();
        return true;
    }

    public boolean deleteUser(final String username){
        try {
            String cur_user = sharedPref.getString("cur_user", "");
            if(cur_user.equals(username)){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("cur_user", "");
                editor.apply();
            }

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    //DELETE PLAYER
                    RealmResults<Player> players = realm.where(Player.class).equalTo("name", username).findAll();
                    players.deleteAllFromRealm();

                    //DELETE HIGHSCORES
                    RealmResults<Highscore> highscores = realm.where(Highscore.class).equalTo("player", username).findAll();
                    highscores.deleteAllFromRealm();


                    Toast.makeText(UsersActivity.this,
                            getApplicationContext().getString(R.string.cUser)  + " " +  username + " " + getApplicationContext().getString(R.string.deleted) + "!", Toast.LENGTH_SHORT).show();
                }
            });
            refreshData();
            return true;
        }
        catch (RealmException e){
            Log.d("RealmException", e.getMessage());
            return false;
        }
    }

    public void refreshDataView(ArrayList<Player> players) {

        usersListAdapter = new UsersListAdapter(this, players);
        lvUsers.setAdapter(usersListAdapter);
        usersListAdapter.notifyDataSetChanged();
    }

    public void refreshData(){

        RealmResults<Player> result = realm.where(Player.class).findAll();
        arrayListUsers.clear();
        if ( result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Log.d("Player name", result.get(i).getName());
                arrayListUsers.add(result.get(i));
            }
        }
        refreshDataView(arrayListUsers);
    }
}
