package dev.dsluo.polls.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import dev.dsluo.polls.R;
import dev.dsluo.polls.ui.home.HomeActivity;

/**
 * Login Activity. Defers to {@link AuthUI} to handle login/registration details.
 *
 * @author David Luo
 */
public class LoginActivity extends AppCompatActivity {

    private static final int RC_LOGIN = 123;

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth auth;

    /**
     * Set up {@link AuthUI} and show it if user is not logged in. Else redirect to home page.
     *
     * @param savedInstanceState {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null)
            showLogin();
        else
            showHome();
    }

    /**
     * Show the {@link AuthUI}.
     */
    private void showLogin() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_LOGIN
        );
    }

    /**
     * Show {@link HomeActivity}.
     */
    private void showHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handle the result of {@link #showLogin()} and show the homepage or an error.
     *
     * @param requestCode Must be {@link #RC_LOGIN} to take action.
     * @param resultCode  {@inheritDoc}
     * @param data        {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_LOGIN) {
            if (resultCode == RESULT_OK) {
                showHome();
                finish();
            } else {
                Toast.makeText(this, "Could not sign in.", Toast.LENGTH_SHORT).show();
                showLogin();
            }
        }
    }

}
