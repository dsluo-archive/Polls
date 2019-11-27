package dev.dsluo.polls.ui.home.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Poll;

public class PollAdapter extends RecyclerView.Adapter<PollAdapter.PollViewHolder> {
    private List<Poll> polls;

    public PollAdapter(List<Poll> polls) {
        this.polls = polls;
    }

    public PollAdapter() {
        this.polls = Collections.emptyList();
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
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
        Poll poll = polls.get(position);
        holder.question.setText(poll.question);
    }

    @Override
    public int getItemCount() {
        return polls.size();
    }

    public static class PollViewHolder extends RecyclerView.ViewHolder {

        public final CardView card;
        public final TextView question;

        public PollViewHolder(@NonNull CardView view) {
            super(view);
            card = view;
            question = card.findViewById(R.id.poll_question);
        }
    }
}
