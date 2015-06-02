package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;


public class SettingsActivity extends ActionBarActivity {
    C1000Login login;


    public SettingsActivity(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String session = extras.getString("session");
        login = new C1000Login(session);
        setContentView(R.layout.settings);

        LoginOnClickListener loginOnClickListener = new LoginOnClickListener(this);
        this.findViewById(R.id.button).setOnClickListener(loginOnClickListener);
    }

    public C1000Login getLogin() {
        return login;
    }
}
