package com.example.swiftride;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
public class BusAdapter extends RecyclerView.Adapter<BusAdapter.BusViewHolder> {
    private List<Bus> busList;

    public BusAdapter(List<Bus> busList) {
        this.busList = busList;
    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bus, parent, false);
        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        Bus bus = busList.get(position);
        holder.licensePlate.setText(bus.getLicensePlate());
        holder.route.setText(bus.getRouteNo() + ": " + bus.getStartRoute() + " -> " + bus.getDestinationRoute());
        holder.noSeats.setText("Seats: " + bus.getNoSeats());
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public static class BusViewHolder extends RecyclerView.ViewHolder {
        TextView licensePlate, route, noSeats;

        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            licensePlate = itemView.findViewById(R.id.tvLicensePlate);
            route = itemView.findViewById(R.id.tvRoute);
            noSeats = itemView.findViewById(R.id.tvNoSeats);
        }
    }
}
