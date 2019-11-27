package dev.dsluo.polls.ui.home.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;

public class PollListFragment extends Fragment {

    private RecyclerView recyclerView;
    private PollAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private PollListViewModel viewModel;

    public void setActiveGroup(Group group) {
        viewModel.setActiveGroup(group);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.poll_list_fragment, container, false);

        viewModel = ViewModelProviders.of(this).get(PollListViewModel.class);

        recyclerView = view.findViewById(R.id.poll_recycler);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PollAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getPolls().observe(this, polls -> {
            adapter.setPolls(polls);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}
