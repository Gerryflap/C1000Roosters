package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
        String accountId = extras.getString("accountId");

        login = new C1000Login(session);
        login.setAccountId(accountId);

        setContentView(R.layout.settings);
        TextView status = (TextView) findViewById(R.id.statusView);
        if(status != null){
            status.setText(login.getStatusString());
        }

        LoginOnClickListener loginOnClickListener = new LoginOnClickListener(this);
        this.findViewById(R.id.button).setOnClickListener(loginOnClickListener);
    }

    public C1000Login getLogin() {
        return login;
    }
}
