package dev.dsluo.polls.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dev.dsluo.polls.R;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {
    public static class PollViewHolder extends RecyclerView.ViewHolder {

        public CardView card;

        public PollViewHolder(@NonNull CardView view) {
            super(view);
            this.card = view;
        }
    }

    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_card, parent, false);
        return new PollViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }

}
