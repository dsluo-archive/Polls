package dev.dsluo.polls.ui.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView navigation;

    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);


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
        Menu navMenu = navigation.getMenu();
        viewModel.getGroups().observe(this, groups -> {
            navMenu.clear();
            for (Group group : groups)
                navMenu.add(group.name);
        });

        navigation.setNavigationItemSelectedListener(menuItem -> {
            // todo
            return true;
        });
    }
}
