package dev.dsluo.polls.ui.home.group;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.ui.home.share.ShareFragment;

/**
 * Fragment to display list of polls for specified group. By default, no group is displayed.
 *
 * @author David Luo
 */
public class GroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private PollListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView groupName;
    private TextView groupDescription;
    private Button share;

    private GroupViewModel viewModel;

    /**
     * Set the {@link Group} to display.
     *
     * @param group A valid instance of {@link Group}.
     */
    public void setActiveGroup(Group group) {
        viewModel.setActiveGroup(group);
    }

    public LiveData<Group> getActiveGroup() {
        return viewModel.getActiveGroup();
    }

    /**
     * Inflate the fragment layout.
     *
     * @param inflater           {@inheritDoc}
     * @param container          {@inheritDoc}
     * @param savedInstanceState {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_fragment, container, false);


        recyclerView = view.findViewById(R.id.poll_recycler);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PollListAdapter();
        recyclerView.setAdapter(adapter);

        groupName = view.findViewById(R.id.group_name);
        groupDescription = view.findViewById(R.id.group_description);
        share = view.findViewById(R.id.share_button);

        share.setOnClickListener(button -> {
            Group group = getActiveGroup().getValue();
            ShareFragment shareFragment = ShareFragment.newInstance(group.groupId);

            int orientation = getResources().getConfiguration().orientation;

            int fragmentContainer = orientation == Configuration.ORIENTATION_PORTRAIT
                    ? R.id.main_fragment_container
                    : R.id.detail_fragment_container;

            getFragmentManager().beginTransaction()
                    .replace(fragmentContainer, shareFragment)
                    .addToBackStack(null)
                    .commit();
        });
        return view;
    }

    /**
     * Initialize the {@link androidx.lifecycle.ViewModel} and start listening for polls.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(GroupViewModel.class);

        viewModel.getPolls().observe(getViewLifecycleOwner(), polls -> {
            adapter.setPolls(polls);
            adapter.notifyDataSetChanged();
        });

        viewModel.getActiveGroup().observe(getViewLifecycleOwner(), group -> {

            CharSequence name;
            CharSequence description;
            int shareVisibility;
            int descriptionStyle;

            if (group == null) {
                name = getText(R.string.no_group);
                description = getText(R.string.no_group_hint);
                descriptionStyle = Typeface.ITALIC;
                shareVisibility = View.INVISIBLE;
            } else {
                adapter.setGroupId(group.groupId);

                name = group.name;
                if (group.description == null || group.description.length() == 0) {
                    description = getText(R.string.no_description);
                    descriptionStyle = Typeface.ITALIC;
                } else {
                    description = group.description;
                    descriptionStyle = Typeface.NORMAL;
                }
                shareVisibility = View.VISIBLE;
            }

            groupName.setText(name);
            groupDescription.setText(description);
            groupDescription.setTypeface(null, descriptionStyle);
            share.setVisibility(shareVisibility);
        });
    }
}
