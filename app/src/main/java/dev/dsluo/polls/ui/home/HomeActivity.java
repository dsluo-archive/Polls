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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.leinardi.android.speeddial.SpeedDialView;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;
import dev.dsluo.polls.ui.auth.LoginActivity;
import dev.dsluo.polls.ui.home.group.GroupFragment;
import dev.dsluo.polls.ui.join.JoinActivity;
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

    public static final String GROUP_FRAGMENT_TAG = "GROUP_FRAGMENT";
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

        if (savedInstanceState == null) {
            // We're initializing for the first time.

            // Main group fragment. Always shown.
            GroupFragment groupFragment = new GroupFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, groupFragment)
                    .addToBackStack(GROUP_FRAGMENT_TAG)
                    .commit();

            // Detail fragment. Only shown in landscape mode.
//            if (findViewById(R.id.detail_fragment_container) != null) {
//                PollDetailFragment detailFragment = new PollDetailFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.detail_fragment_container, detailFragment)
//                        .addToBackStack(GROUP_FRAGMENT_TAG)
//                        .commit();
//            }
        }

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);
        fab = findViewById(R.id.fab);

        // Fill out header info.
        View header = navigation.getHeaderView(0);
        ImageView profilePic = header.findViewById(R.id.profile_picture);
        TextView name = header.findViewById(R.id.name);
        TextView email = header.findViewById(R.id.email);

        // Start observing user to get/keep updated user info.
        viewModel.getUser().observe(this, user -> {
            if (user.photoURL != null)
                profilePic.setImageURI(user.photoURL);
            if (user.displayName != null)
                name.setText(user.displayName);
            if (user.email != null)
                email.setText(user.email);
        });

        // Populate navigation menu with groups.
        // TODO: use proper navigation methods
        Menu navMenu = navigation.getMenu();
        viewModel.getGroups().observe(this, groups -> {
            navMenu.clear();
            for (Group group : groups) {
                MenuItem menuItem = navMenu.add(group.name);
                menuItem.setCheckable(true);
                menuItem.setOnMenuItemClickListener(item -> {
                    GroupFragment groupFragment = (GroupFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.main_fragment_container);

                    // this only happens when fragment is not being displayed in portrait mode.
                    if (groupFragment == null)
                        return false;

                    Fragment detailFragment = getSupportFragmentManager()
                            .findFragmentById(R.id.detail_fragment_container);

                    if (detailFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .remove(detailFragment)
                                .commit();
                    }

                    groupFragment.setActiveGroup(group);
                    drawer.closeDrawers();
                    return true;
                });
            }
        });

        fab.inflate(R.menu.fab);
        fab.setOnActionSelectedListener(actionItem -> {
            Class newActivity;
            switch (actionItem.getId()) {
                case R.id.new_group:
                    newActivity = NewGroupActivity.class;
                    break;
                case R.id.new_poll:
                    newActivity = NewPollActivity.class;
                    break;
                case R.id.join:
                    newActivity = JoinActivity.class;
                    break;
                case R.id.logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    return false;
                default:
                    newActivity = null;
                    break;
            }

            if (newActivity != null) {
                startActivity(new Intent(this, newActivity));
            }
            return newActivity == null;
        });
    }
}
