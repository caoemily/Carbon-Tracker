package com.sfu276assg1.yancao.carbontracker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.sfu276assg1.yancao.UI.SelectCarActivity;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Car> {

    private SelectCarActivity activity;
    private List<Car> cars;

    public ListViewAdapter(SelectCarActivity context, int resource, List<Car> cars) {
        super(context, resource, cars);
        this.activity = context;
        this.cars = cars;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.car_list, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(cars.get(position).getNickname());
        holder.make.setText(cars.get(position).getMake());
        holder.model.setText(cars.get(position).getModel());
        holder.year.setText(cars.get(position).getYear());
        holder.icon.setImageResource(cars.get(position).getIcon());


        //handling buttons event
        holder.btnEdit.setOnClickListener(onEditListener(position, holder));
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));

        return convertView;
    }

    private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editCar(position);
            }
        };
    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarbonModel.getInstance().getDb().deleteCarRow((position + 1));
                CarbonModel.getInstance().removeCar(position);
                holder.swipeLayout.close();
                activity.updateAdapter();
            }
        };
    }

    private class ViewHolder {
        private TextView name;
        private TextView make;
        private TextView model;
        private TextView year;
        private ImageView icon;
        private View btnDelete;
        private View btnEdit;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);
            btnEdit = v.findViewById(R.id.edit);
            name = (TextView) v.findViewById(R.id.carName);
            make = (TextView) v.findViewById(R.id.carMake);
            model = (TextView) v.findViewById(R.id.carModel);
            year = (TextView) v.findViewById(R.id.carYear);
            icon = (ImageView) v.findViewById(R.id.carIcon);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        }
    }
}