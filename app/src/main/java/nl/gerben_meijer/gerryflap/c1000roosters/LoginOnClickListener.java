package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Gerryflap on 2015-05-31.
 */
public class LoginOnClickListener implements View.OnClickListener {
    SettingsActivity activity;

    public LoginOnClickListener(SettingsActivity activity) {
        this.activity = activity;
        if(this.activity.getLogin() != null){
            ((TextView) activity.findViewById(R.id.statusView)).setText("You are logged in");
        }
    }

    @Override
    public void onClick(View v) {
        CharSequence username = ((EditText) activity.findViewById(R.id.editText)).getText();
        CharSequence password = ((EditText) activity.findViewById(R.id.editText2)).getText();
        String usernameString = username.toString();
        String passwordString = password.toString();

        System.out.println(usernameString);
        AsyncLogin login = new AsyncLogin(usernameString, passwordString, activity);
        login.execute();
    }
}
