package com.example.swiftride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReservationInfoAdapter extends ArrayAdapter<ReservationInfo> {
    public ReservationInfoAdapter(Context context, List<ReservationInfo> reservationInfoList) {
        super(context, 0, reservationInfoList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_item, parent, false);
        }

        ReservationInfo info = getItem(position);

        TextView passengerCountTextView = convertView.findViewById(R.id.passengerCountTextView);
        TextView startPointTextView = convertView.findViewById(R.id.startPointTextView);
        TextView destinationPointTextView = convertView.findViewById(R.id.destinationPointTextView);
        TextView busIdTextView = convertView.findViewById(R.id.busIdTextView);

        passengerCountTextView.setText("Passengers: " + info.getPassengerCount());
        startPointTextView.setText("Start: " + info.getStartPoint());
        destinationPointTextView.setText("Destination: " + info.getDestinationPoint());
        busIdTextView.setText("Bus ID: " + info.getBusId());

        return convertView;
    }
}