package com.example.swiftride;

import android.app.AlertDialog;
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

    public FeedbackAdapter(Context context, List<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.busIdTextView.setText(String.valueOf(booking.getBusId()));
        holder.startPointTextView.setText(booking.getStartPoint());
        holder.destinationPointTextView.setText(booking.getDestinationPoint());
        holder.seatNumberTextView.setText(String.valueOf(booking.getSeatNumber()));

        holder.submitFeedbackButton.setOnClickListener(v -> {
            // Get user input for feedback
            String feedbackText = holder.feedbackEditText.getText().toString();
            int rating = (int) holder.ratingBar.getRating();

            // Save feedback
            Feedback feedback = new Feedback();
            feedback.setUserId(1); // Assuming logged-in user ID (change accordingly)
            feedback.setBusId(booking.getBusId());
            feedback.setFeedbackText(feedbackText);
            feedback.setRating(rating);
            feedback.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

            DBHandler dbHelper = new DBHandler(context);
            dbHelper.insertFeedback(feedback);
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

