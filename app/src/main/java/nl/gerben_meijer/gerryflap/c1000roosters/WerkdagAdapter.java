package nl.gerben_meijer.gerryflap.c1000roosters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import nl.gerben_meijer.gerryflap.c1000roosters.C1000.Werkdag;

/**
 * Created by Gerryflap on 2015-06-01.
 */
public class WerkdagAdapter extends BaseAdapter {

        private ArrayList<Werkdag> mData = new ArrayList<>();
        private LayoutInflater mInflater;
        private Activity activity;

        public WerkdagAdapter(Activity activity) {
            mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final Werkdag item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Werkdag getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addAll(Collection<Werkdag> werkdagCollection){
                for (Werkdag werkdag: werkdagCollection){
                    mData.add(werkdag);
                }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_view_layout, null);
                holder = new ViewHolder();
                holder.view = (LinearLayout) convertView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            ((TextView)holder.view.findViewById(R.id.day)).setText(mData.get(position).getDag() + " " + mData.get(position).getDatum());
            //((TextView)holder.view.findViewById(R.id.day)).setTextColor(0xFFFFFFFF);
            ((TextView)holder.view.findViewById(R.id.start)).setText("Van: " + mData.get(position).getStart());
            ((TextView)holder.view.findViewById(R.id.end)).setText("Tot: " + mData.get(position).getEind());
            ((TextView)holder.view.findViewById(R.id.pauze)).setText("Pauze: " + mData.get(position).getPauze());
            holder.view.setPadding(20,20,20,20);
            /*if(position%2 == 0) {
                holder.view.setBackgroundColor(0xFFAAAAAA);
            } else {
                holder.view.setBackgroundColor(0xFFBBBBBB);
            }
            */
            return convertView;
        }

    public class ViewHolder{
        public LinearLayout view;
    }

}
