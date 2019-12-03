package dev.dsluo.polls.ui.home.group;

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

/**
 * Adapter for {@link GroupFragment}'s recycler.
 *
 * @author David Luo
 */
public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.PollViewHolder> {
    private List<Poll> polls;

    /**
     * Constructor
     *
     * @param polls The list of polls to display.
     */
    public PollListAdapter(List<Poll> polls) {
        this.polls = polls;
    }

    /**
     * Constructor, with default no polls displayed.
     */
    public PollListAdapter() {
        this.polls = Collections.emptyList();
    }

    /**
     * Sets the polls to be displayed. <strong>DOES NOT</strong> call {@link #notifyDataSetChanged()}.
     * This must be called explicitly.
     *
     * @param polls {@inheritDoc}
     */
    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    /**
     * Makes a new {@link PollViewHolder} and inflate its layout for the recycler.
     *
     * @param parent   {@inheritDoc}
     * @param viewType {@inheritDoc}
     * @return A new {@link PollViewHolder}
     */
    @NonNull
    @Override
    public PollViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_card, parent, false);
        return new PollViewHolder(cardView);
    }


    /**
     * Fills out the views in the {@link PollViewHolder} with information from {@link #polls}.
     *
     * @param holder   A {@link PollViewHolder}
     * @param position The index in {@link #polls} that is being bound.
     */
    @Override
    public void onBindViewHolder(@NonNull PollViewHolder holder, int position) {
        Poll poll = polls.get(position);
        holder.question.setText(poll.question);
    }

    /**
     * {@inheritDoc}
     *
     * @return The size of {@link #polls}
     */
    @Override
    public int getItemCount() {
        return polls.size();
    }

    /**
     * {@link RecyclerView.ViewHolder} for {@link PollListAdapter}
     */
    public static class PollViewHolder extends RecyclerView.ViewHolder {

        public final CardView card;
        public final TextView question;

        /**
         * Constructor
         *
         * @param view The {@link CardView} to be held.
         */
        public PollViewHolder(@NonNull CardView view) {
            super(view);
            card = view;
            question = card.findViewById(R.id.poll_question);
        }
    }
}
