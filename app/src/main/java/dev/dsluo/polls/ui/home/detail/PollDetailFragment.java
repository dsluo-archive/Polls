package dev.dsluo.polls.ui.home.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import dev.dsluo.polls.R;

/**
 * Shows the question and allows user to vote on the question.
 * Also shows the results of the poll.
 *
 * @author David Luo
 */
public class PollDetailFragment extends Fragment {
    public static final String ARG_GROUP_ID = "GROUP_ID";
    public static final String ARG_POLL_ID = "POLL_ID";

    private PollDetailViewModel viewModel;
    private TextView question;
    private RadioGroup choiceGroup;
    private Button vote;
    private Button results;

    private String pollId;
    private String groupId;

    public static PollDetailFragment newInstance(String groupId, String pollId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_GROUP_ID, groupId);
        arguments.putString(ARG_POLL_ID, pollId);

        PollDetailFragment fragment = new PollDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public PollDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.groupId = getArguments().getString(ARG_GROUP_ID);
            this.pollId = getArguments().getString(ARG_POLL_ID);
        }
    }

    /**
     * TODO: this fragment
     *
     * @param inflater           {@inheritDoc}
     * @param container          {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poll_detail_fragment, container, false);
        question = view.findViewById(R.id.question_title);
        choiceGroup = view.findViewById(R.id.poll_choices);
        vote = view.findViewById(R.id.vote);
        results = view.findViewById(R.id.results);

        vote.setEnabled(false);
        choiceGroup.setOnCheckedChangeListener((group, checkedId) -> vote.setEnabled(true));

        vote.setOnClickListener(v -> {
            int chosenId = choiceGroup.getCheckedRadioButtonId();
            RadioButton chosen = choiceGroup.findViewById(chosenId);

            if (chosen == null) {
                Snackbar.make(v, "Failed to cast vote. No choice chosen.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            viewModel.vote((String) chosen.getText(), isSuccessful -> {
                if (isSuccessful) {
                    viewModel.setShowingResults(true);
                } else {
                    Snackbar.make(v, "Failed to cast vote.", Snackbar.LENGTH_SHORT).show();
                }
            });
        });

        results.setOnClickListener(v -> viewModel.setShowingResults(true));

        return view;
    }

    private void showResults() {
        for (int i = 0; i < choiceGroup.getChildCount(); i++) {
            RadioButton choiceButton = (RadioButton) choiceGroup.getChildAt(i);
            choiceButton.setEnabled(false);

            String key = (String) choiceButton.getText();
            int count = viewModel.getPoll().getValue().choices.get(key);
            choiceButton.setText(String.format("%s - %d votes", key, count));
        }
        vote.setVisibility(View.GONE);
        results.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(PollDetailViewModel.class);

        viewModel.setPoll(groupId, pollId);

        viewModel.getPoll().observe(this, poll -> {
            question.setText(poll.question);
            choiceGroup.removeAllViews();
            for (String choice : poll.choices.keySet()) {
                RadioButton choiceButton = new RadioButton(getContext());
                choiceButton.setText(choice);
                choiceGroup.addView(choiceButton);
            }
        });

        viewModel.listenShowingResults().observe(this, showingResults -> {
            if (!showingResults)
                return;
            showResults();
        });


    }
}
