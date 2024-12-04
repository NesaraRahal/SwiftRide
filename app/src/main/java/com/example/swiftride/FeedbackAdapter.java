package com.example.swiftride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {
    private List<Booking> bookings;
    private Context context;
    private int userId;  // Add userId to ensure feedback is filtered for the logged-in user

    // Constructor to receive the userId
    public FeedbackAdapter(Context context, List<Booking> bookings, int userId) {
        this.context = context;
        this.bookings = bookings;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Set booking information
        holder.busIdTextView.setText(String.valueOf(booking.getBusId()));
        holder.startPointTextView.setText(booking.getStartPoint());
        holder.destinationPointTextView.setText(booking.getDestinationPoint());
        holder.seatNumberTextView.setText(String.valueOf(booking.getSeatNumber()));

        // Display feedback if available for the logged-in user and the current booking
        List<Feedback> feedbackList = booking.getFeedbackList();
        if (feedbackList != null && !feedbackList.isEmpty()) {
            for (Feedback feedback : feedbackList) {
                if (feedback.getUserId() == userId) {
                    // If feedback exists for the logged-in user, show it in the UI
                    holder.feedbackEditText.setText(feedback.getFeedbackText());
                    holder.ratingBar.setRating(feedback.getRating());
                    holder.submitFeedbackButton.setEnabled(false);  // Disable feedback submission if feedback already exists
                }
            }
        }

        // Submit feedback if it doesn't exist for the logged-in user
        holder.submitFeedbackButton.setOnClickListener(v -> {
            String feedbackText = holder.feedbackEditText.getText().toString();
            int rating = (int) holder.ratingBar.getRating();

            // Check if feedback is already provided, if not, submit it
            boolean feedbackExists = false;
            if (feedbackList != null) {
                for (Feedback feedback : feedbackList) {
                    if (feedback.getUserId() == userId) {
                        feedbackExists = true;
                        break;
                    }
                }
            }

            if (!feedbackExists) {
                // Save feedback to database
                Feedback feedback = new Feedback();
                feedback.setUserId(userId);  // Use logged-in user ID
                feedback.setBusId(booking.getBusId());
                feedback.setFeedbackText(feedbackText);
                feedback.setRating(rating);
                feedback.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

                DBHandler dbHelper = new DBHandler(context);
                dbHelper.insertFeedback(feedback);
                // Optionally, refresh the list or update UI to reflect new feedback
                // feedbackList.add(feedback);  // Add feedback to local list (not strictly necessary)
                notifyDataSetChanged();  // Refresh the RecyclerView to show the new feedback
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView busIdTextView, startPointTextView, destinationPointTextView, seatNumberTextView;
        EditText feedbackEditText;
        RatingBar ratingBar;
        Button submitFeedbackButton;

        public ViewHolder(View itemView) {
            super(itemView);
            busIdTextView = itemView.findViewById(R.id.busIdTextView);
            startPointTextView = itemView.findViewById(R.id.startPointTextView);
            destinationPointTextView = itemView.findViewById(R.id.destinationPointTextView);
            seatNumberTextView = itemView.findViewById(R.id.seatNumberTextView);
            feedbackEditText = itemView.findViewById(R.id.feedbackEditText);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            submitFeedbackButton = itemView.findViewById(R.id.submitFeedbackButton);
        }
    }
}
