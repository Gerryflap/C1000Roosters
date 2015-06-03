package nl.gerben_meijer.gerryflap.c1000roosters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;
import nl.gerben_meijer.gerryflap.c1000roosters.data.DatabaseCommunicator;


public class RoosterActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    private WerkdagAdapter adapter;
    private C1000Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooster);

        new DatabaseCommunicator(this);
        ListView dagen = (ListView) this.findViewById(R.id.list);
        adapter = new WerkdagAdapter(this);
        dagen.setAdapter(adapter);
        ((SwipeRefreshLayout) findViewById(R.id.refresh)).setOnRefreshListener(this);


        if(savedInstanceState == null || savedInstanceState.getString("session") == null) {
            login = new C1000Login();
            login.setWerkdagList(DatabaseCommunicator.getInstance().getWerkdagList());
            adapter.clear();
            adapter.addAll(login.getWerkdagList());

            AsyncLogin asyncLogin = new AsyncLogin(this, login);
            asyncLogin.execute();
        } else {
            login = new C1000Login(savedInstanceState.getString("session"));
            login.setAccountId(savedInstanceState.getString("accountId"));
            login.setWerkdagList(DatabaseCommunicator.getInstance().getWerkdagList());
            adapter.clear();
            adapter.addAll(login.getWerkdagList());
        }

    }


    public void onResume(){
        super.onResume();
        login.setWerkdagList(DatabaseCommunicator.getInstance().getWerkdagList());
        adapter.clear();
        adapter.addAll(login.getWerkdagList());

        if(!login.isLoggedIn()){
            (new AsyncLogin(this, login)).execute();
        }
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
            intent.putExtra("accountId", login.getAccountId());

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveInstanceState(Bundle outState){
        outState.putString("session", login.getSession());
        outState.putString("accountId", login.getAccountId());
    }

    public WerkdagAdapter getAdapter() {
        return adapter;
    }

    public C1000Login getLogin() {
        return login;
    }

    @Override
    public void onRefresh() {
        if(login.isLoggedIn()){
            AsyncLoader asyncLoader = new AsyncLoader(this, login, (SwipeRefreshLayout) this.findViewById(R.id.refresh));
            asyncLoader.execute(adapter);
        } else {
            AsyncLogin asyncLogin = new AsyncLogin(this, login, (SwipeRefreshLayout) this.findViewById(R.id.refresh));
            asyncLogin.execute();
        }
    }
}
