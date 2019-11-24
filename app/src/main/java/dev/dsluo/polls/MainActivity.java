package dev.dsluo.polls;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_LOGIN = 123;

    private DrawerLayout drawer;
    private NavigationView navigation;

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer);
        navigation = findViewById(R.id.navigation);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null)
            showLogin();
        else
            showHome();

    }

    private void showLogin() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_LOGIN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                showHome();
            } else {
                Snackbar.make(drawer, "Could not sign in.", Snackbar.LENGTH_SHORT);
                showLogin();
            }
        }
    }

    private void showHome() {
        FirebaseUser user = auth.getCurrentUser();
        Snackbar.make(drawer, "signed in as " + user.getDisplayName(), Snackbar.LENGTH_LONG);
    }

}
