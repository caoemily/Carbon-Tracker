package com.sfu276assg1.yancao.carbontracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by vu on 2017-03-16.
 */

public class CustomArrayAdapter extends ArrayAdapter<JourneyInfo> {
    private Context context;
    private int layoutID;

    public CustomArrayAdapter(Context context, int layoutId, List<JourneyInfo> items) {
        super(context, layoutId, items);
        this.context = context;
        this.layoutID = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JourneyInfo item = getItem(position);

        String name = item.getJourneyName();
        String journeyDate = item.getJourneyDate();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutID, parent,false);
        }

        TextView nameView = (TextView)convertView.findViewById(R.id.textJourneyName);
        TextView dateView = (TextView)convertView.findViewById(R.id.textJourneyDate);

        nameView.setText(name);
        dateView.setText(journeyDate);
        return convertView;
    }
}
