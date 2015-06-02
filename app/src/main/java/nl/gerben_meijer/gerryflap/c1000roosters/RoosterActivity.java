package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toolbar;

import java.text.AttributedCharacterIterator;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;
import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;


public class RoosterActivity extends Activity {
    private WerkdagAdapter adapter;
    private C1000Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooster);


        ListView dagen = (ListView) this.findViewById(R.id.list);
        adapter = new WerkdagAdapter(this);
        dagen.setAdapter(adapter);

        login = new C1000Login();
        AsyncLogin asyncLogin = new AsyncLogin(this, login);
        asyncLogin.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rooster, menu);
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
            Intent intent = new Intent(this.getApplicationContext(), SettingsActivity.class);
            intent.putExtra("session", login.getSession());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public WerkdagAdapter getAdapter() {
        return adapter;
    }

    public C1000Login getLogin() {
        return login;
    }
}
