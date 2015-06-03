package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;
import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;

/**
 * Created by Gerryflap on 2015-05-30.
 */
public class AsyncLogin extends AsyncTask<Void, Object, Boolean>{
    String username;
    String password;
    C1000Login c1000Login;
    Activity activity;
    boolean load;

    public AsyncLogin(Activity activity, C1000Login c1000Login){
        this.c1000Login = c1000Login;
        this.activity = activity;
        SharedPreferences sharedPreferences = activity.getSharedPreferences("nl.gerben_meijer.gerryflap.c1000roosters", Context.MODE_PRIVATE);
        this.username = sharedPreferences.getString("username", "");
        this.password = sharedPreferences.getString("password", "");
        load = true;

    }

    public AsyncLogin(String username, String password, Activity activity){
        this.username = username;
        this.password = password;
        this.activity = activity;
        this.c1000Login = new C1000Login();
        load = false;
    }

    public void onPreExecute(){
        TextView status = (TextView) activity.findViewById(R.id.statusView);
        if(status != null){
            status.setText(c1000Login.getStatusString());
        }
    }

    protected Boolean doInBackground(Void... params) {
        this.c1000Login.login(username, password);
        boolean loggedIn = c1000Login.isLoggedIn();
        if(!loggedIn){
            return false;
        }
        String accountId = null;
        if(load) {
            SharedPreferences sharedPreferences = activity.getSharedPreferences("nl.gerben_meijer.gerryflap.c1000roosters", Context.MODE_PRIVATE);
            accountId = sharedPreferences.getString("account_id", null);
        }
        if(accountId == null) {
            c1000Login.getWeek();
        }

        if (accountId == null && c1000Login.getAccountId() != null){
            SharedPreferences sharedPreferences = activity.getSharedPreferences("nl.gerben_meijer.gerryflap.c1000roosters", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("account_id", c1000Login.getAccountId());
            editor.apply();
            return true;
        } else if( accountId == null){
            return false;
        }
        return true;

    }

    protected void onPostExecute(Boolean result) {
        TextView status = (TextView) activity.findViewById(R.id.statusView);
        if(status != null){
            status.setText(c1000Login.getStatusString());
        }

        if(!load && result){
            SharedPreferences sharedPreferences = activity.getSharedPreferences("nl.gerben_meijer.gerryflap.c1000roosters", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.apply();

        }

        if (load && result){
            if(activity instanceof RoosterActivity){
                AsyncLoader loader = new AsyncLoader(c1000Login, activity);
                loader.execute(((RoosterActivity) activity).getAdapter());
            }

        }

        if(!result){
            AlertDialog alertDialog = new AlertDialog.Builder(this.activity).create();
            alertDialog.setTitle("Login failed");
            alertDialog.setMessage("The login failed, please check your network connection and retry");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Swek
                }
            });
            alertDialog.show();

        }
    }

}
