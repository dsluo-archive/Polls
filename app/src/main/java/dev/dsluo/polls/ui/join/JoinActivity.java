package dev.dsluo.polls.ui.join;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Activity to join a group.
 *
 * @author David Luo
 * <p>
 * tbh this probably doesn't need to be its own activity
 */
public class JoinActivity extends AppCompatActivity {

    private JoinViewModel viewModel;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(JoinViewModel.class);
        startScanner();
    }

    /**
     * Launch the scanner app.
     */
    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    /**
     * Get result of scanner app.
     *
     * @param requestCode {@inheritDoc}
     * @param resultCode  {@inheritDoc}
     * @param data        {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        handleScanResult(scanResult.getContents());
    }

    /**
     * Handle result of scan.
     *
     * @param scanContent The text from the QR code.
     */
    private void handleScanResult(String scanContent) {
        if (scanContent == null) {
            // user pressed back button
            finish();
            return;
        }
        viewModel.joinGroup(scanContent, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Joined group.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Could not join group.", Toast.LENGTH_SHORT).show();
                startScanner();
            }
        });
    }
}
