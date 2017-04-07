package com.sfu276assg1.yancao.carbontracker;


import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.sfu276assg1.yancao.UI.MainActivity;

import java.util.List;

public class JourneyListViewAdapter extends ArrayAdapter<Journey> {
    private MainActivity activity;
    private List<Journey> journeys;

    public JourneyListViewAdapter(MainActivity context, int resource, List<Journey> journeys) {
        super(context, resource, journeys);
        this.activity = context;
        this.journeys = journeys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JourneyListViewAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            // get all UI view
            holder = new JourneyListViewAdapter.ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (JourneyListViewAdapter.ViewHolder) convertView.getTag();
        }

        if (journeys.get(position).getRoute().getType().equals("Drive")) {
            holder.name.setText(journeys.get(position).getCar().getNickname());
            holder.icon.setImageResource(journeys.get(position).getCar().getIcon());
        }
        else if (journeys.get(position).getRoute().getType().equals("Public Transit")) {
            holder.name.setText(journeys.get(position).getRoute().getType());
            holder.icon.setImageResource(R.drawable.bus_ic);
        }
        else {
            holder.name.setText(journeys.get(position).getRoute().getType());
            holder.icon.setImageResource(R.drawable.bike_ic);
        }

        if (journeys.get(position).getRoute().getName().equals("")) {
            holder.routeName.setText("");
        }
        else {
            holder.routeName.setText(journeys.get(position).getRoute().getName());
        }

        if (CarbonModel.getInstance().getUnitChoice() == 0) {
            holder.carbon_icon.setImageResource(R.drawable.ic_tree);
            holder.carbon.setText(Html.fromHtml("<b>" + journeys.get(position).calculateCarbonTreeYear() + "</b>" + " tree days"));
        }
        else {
            holder.carbon_icon.setImageResource(R.drawable.ic_cloud);
            holder.carbon.setText(Html.fromHtml("<b>" + journeys.get(position).calculateCarbon() + "</b>" + " kg/CO" + "<sub>" + "<small>" + "2" + "</small>" +"</sub>"));
        }
        holder.distance.setText(Html.fromHtml("<b>" + journeys.get(position).getRoute().getDistance() + "</b>" + " km"));


        //handling buttons event
        holder.btnEditJourney.setOnClickListener(onEditJourneyListener(position, holder));
        holder.btnEditDate.setOnClickListener(onEditDateListener(position, holder));
        holder.btnDeleteJourney.setOnClickListener(onDeleteListener(position, holder));

        return convertView;
    }

    private View.OnClickListener onEditJourneyListener(final int position, final JourneyListViewAdapter.ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = journeys.get(position).getId();
                activity.editJourney(id);
            }
        };
    }

    private View.OnClickListener onEditDateListener(final int position, final JourneyListViewAdapter.ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = journeys.get(position).getId();
                activity.editDate(id);
            }
        };
    }

    private View.OnClickListener onDeleteListener(final int position, final JourneyListViewAdapter.ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = journeys.get(position).getId();
                CarbonModel.getInstance().removeJourney(id);
                CarbonModel.getInstance().getDb().deleteJourneyRow(id);
                Toast.makeText(activity, Integer.toString(id),Toast.LENGTH_SHORT).show();
                holder.swipeLayout.close();
                activity.updateAdapter();
            }
        };
    }

    private class ViewHolder {
        private TextView name;
        private TextView carbon;
        private TextView distance;
        private TextView routeName;
        private ImageView icon;
        private ImageView carbon_icon;
        private View btnEditJourney;
        private View btnEditDate;
        private View btnDeleteJourney;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout_journey);
            btnEditJourney = v.findViewById(R.id.edit_journey);
            btnEditDate = v.findViewById(R.id.edit_date);
            btnDeleteJourney = v.findViewById(R.id.delete_journey);
            name = (TextView) v.findViewById(R.id.journeyName);
            routeName = (TextView) v.findViewById(R.id.journeyRouteName);
            carbon = (TextView) v.findViewById(R.id.journeyCarbon);
            distance = (TextView) v.findViewById(R.id.journeyDistance);
            icon = (ImageView) v.findViewById(R.id.journeyIcon);
            carbon_icon = (ImageView) v.findViewById(R.id.journeyCarbonIcon);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        }
    }
}
