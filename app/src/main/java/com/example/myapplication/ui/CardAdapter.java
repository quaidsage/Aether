package com.example.myapplication.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardItem> items;
    private final OnCardClickListener onCardClickListener;

    public interface OnCardClickListener {
        void onCardClick(CardItem cardItem);
    }

    public CardAdapter(List<CardItem> items, OnCardClickListener onCardClickListener) {
        this.items = items;
        this.onCardClickListener = onCardClickListener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        CardItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView imageView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textTitle);
            descriptionTextView = itemView.findViewById(R.id.textDistance);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    CardItem cardItem = items.get(position);
                    onCardClickListener.onCardClick(cardItem);

                }
            });
        }

        public void bind(CardItem item) {
            titleTextView.setText(item.getTitle());
            descriptionTextView.setText(item.getDescription());
            imageView.setImageResource(item.getImageResId());
        }
    }
}