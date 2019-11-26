package dev.dsluo.polls.ui.home;

import android.os.Bundle;
import android.view.Menu;

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

        Menu navMenu = navigation.getMenu();
        viewModel.getGroups().observe(this, groups -> {
            navMenu.clear();
            for (Group group : groups)
                navMenu.add(group.name);
        });
    }
}
