package dev.dsluo.polls.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.leinardi.android.speeddial.SpeedDialView;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.ui.home.list.PollListFragment;
import dev.dsluo.polls.ui.newgroup.NewGroupActivity;
import dev.dsluo.polls.ui.newpoll.NewPollActivity;

/**
 * Home Activity. Displays current list of polls and list of groups in side navigation bar.
 *
 * @author David Luo
 */
public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigation;
    private SpeedDialView fab;

    private HomeViewModel viewModel;

    private PollListFragment listFragment;

    public static final int RC_NEW_POLL = 3843;
    public static final int RC_NEW_GROUP = 38938;

    /**
     * Initializes navbar and begins observation of changes to the {@link dev.dsluo.polls.data.models.User},
     * and their {@link Group}s.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        fab = findViewById(R.id.fab);

        listFragment = (PollListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.poll_list_fragment);

        // Fill out header info.
        View header = navigation.getHeaderView(0);
        ImageView profilePic = header.findViewById(R.id.profile_picture);
        TextView name = header.findViewById(R.id.name);
        TextView email = header.findViewById(R.id.email);

        viewModel.getUser().observe(this, user -> {
            if (user.photoURL != null)
                profilePic.setImageURI(user.photoURL);
            if (user.displayName != null)
                name.setText(user.displayName);
            if (user.email != null)
                email.setText(user.email);
        });

        // List groups.
        // TODO: use proper navigation methods
        Menu navMenu = navigation.getMenu();
        viewModel.getGroups().observe(this, groups -> {
            navMenu.clear();
            for (Group group : groups) {
                MenuItem menuItem = navMenu.add(group.name);
                menuItem.setOnMenuItemClickListener(item -> {
                    listFragment.setActiveGroup(group);
                    return true;
                });
            }
        });

        fab.inflate(R.menu.fab);
        fab.setOnActionSelectedListener(actionItem -> {
            if (actionItem.getId() == R.id.new_group) {
                startActivityForResult(
                        new Intent(this, NewGroupActivity.class),
                        RC_NEW_GROUP
                );
            } else if (actionItem.getId() == R.id.new_poll) {
                startActivityForResult(
                        new Intent(this, NewPollActivity.class),
                        RC_NEW_POLL
                );
            }
            return false;
        });
    }
}
