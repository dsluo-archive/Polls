package dev.dsluo.polls.ui.home.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dev.dsluo.polls.R;

/**
 * Shows the question and allows user to vote on the question.
 * Also shows the results of the poll.
 *
 * @author David Luo
 */
public class PollDetailFragment extends Fragment {

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
        return inflater.inflate(R.layout.poll_detail_fragment, container, false);
    }
}
