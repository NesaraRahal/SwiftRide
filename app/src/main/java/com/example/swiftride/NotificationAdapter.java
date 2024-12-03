package com.example.swiftride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private List<Notification> notifications;
    private Consumer<Integer> acceptCallback; // Callback for accepting swaps
    private Consumer<Integer> rejectCallback; // Callback for rejecting swaps

    public NotificationAdapter(Context context, List<Notification> notifications,
                               Consumer<Integer> acceptCallback,
                               Consumer<Integer> rejectCallback) {
        this.context = context;
        this.notifications = notifications;
        this.acceptCallback = acceptCallback;
        this.rejectCallback = rejectCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);


        holder.acceptButton.setOnClickListener(v -> {
            acceptCallback.accept(notification.getNotificationId());
        });

        holder.rejectButton.setOnClickListener(v -> {
            rejectCallback.accept(notification.getNotificationId());
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMessage;
        Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    public void removeNotification(int position) {
        notifications.remove(position);  // Remove the notification from the list
        notifyItemRemoved(position);  // Notify the adapter
    }

}
