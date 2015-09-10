package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.C1000Login;
import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;
import nl.gerben_meijer.gerryflap.c1000roosters.data.DatabaseCommunicator;

/**
 * Created by Gerryflap on 2015-05-30.
 */
public class AsyncLoader extends AsyncTask<WerkdagAdapter, Object, List<Werkdag>>{
    private SwipeRefreshLayout layout;
    WerkdagAdapter params;
    C1000Login c1000Login;
    Activity activity;

    public AsyncLoader(RoosterActivity activity, C1000Login c1000Login){
        this.c1000Login = c1000Login;
        this.activity = activity;
    }

    public AsyncLoader(RoosterActivity roosterActivity, C1000Login login, SwipeRefreshLayout layout) {
        this(roosterActivity, login);
        this.layout = layout;
    }

    public void onPreExecute(){
        TextView status = (TextView) activity.findViewById(R.id.statusView);

        if(status != null){
            if(c1000Login.getStatus() != C1000Login.STATUS_LOGGED_OUT){
                c1000Login.setStatus(C1000Login.STATUS_LOADING_SCHEDULE);
            }
            status.setText(c1000Login.getStatusString());
        }
    }


    protected List<Werkdag> doInBackground(WerkdagAdapter[] params) {
        this.params = params[0];
        List<Werkdag> werkdagen = c1000Login.getWeek();

        if(c1000Login.getStatus() == C1000Login.STATUS_SHEDULE_LOADED) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 6);
            werkdagen.addAll(c1000Login.getWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR)));
            calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000 * 60 * 60 * 24 * 7);
            werkdagen.addAll(c1000Login.getWeek(calendar.get(Calendar.WEEK_OF_YEAR), calendar.get(Calendar.YEAR)));
        }

        if(c1000Login.getStatus() == C1000Login.STATUS_SHEDULE_LOADED) {
            DatabaseCommunicator.getInstance().clear();
            for (Werkdag werkdag : werkdagen) {
                DatabaseCommunicator.getInstance().insertWerkdag(werkdag);
            }
        }

        c1000Login.setWerkdagList(werkdagen);

        return werkdagen;
    }

    protected void onPostExecute(List<Werkdag> result) {
        TextView status = (TextView) activity.findViewById(R.id.statusView);
        if(status != null){
            status.setText(c1000Login.getStatusString());
        }
        System.out.println(layout);
        if (layout != null){
            layout.setRefreshing(false);
        }
        params.clear();
        params.addAll(result);
        params.notifyDataSetChanged();

    }
}
