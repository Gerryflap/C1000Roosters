package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;
import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;

/**
 * Created by Gerryflap on 2015-05-30.
 */
public class AsyncLoader extends AsyncTask<WerkdagAdapter, Object, List<Werkdag>>{
    WerkdagAdapter params;
    C1000Login c1000Login;
    Activity activity;

    public AsyncLoader(C1000Login c1000Login, Activity activity){
        this.c1000Login = c1000Login;
        this.activity = activity;
    }

    protected List<Werkdag> doInBackground(WerkdagAdapter[] params) {
        this.params = params[0];

        List<Werkdag> werkdagen = c1000Login.getWeek();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7 );
        werkdagen.addAll(c1000Login.getWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR)));

        return werkdagen;
    }

    protected void onPostExecute(List<Werkdag> result) {
        params.addAll(result);
        params.notifyDataSetChanged();
    }
}
