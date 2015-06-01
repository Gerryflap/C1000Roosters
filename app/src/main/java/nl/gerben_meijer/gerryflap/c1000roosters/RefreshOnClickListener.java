package nl.gerben_meijer.gerryflap.c1000roosters;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Gerryflap on 2015-05-31.
 */
public class RefreshOnClickListener implements View.OnClickListener {
    RoosterActivity activity;

    public RefreshOnClickListener(RoosterActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {

        AsyncLoader login = new AsyncLoader(activity.getLogin(), activity);
        login.execute();
    }
}

