package dev.dsluo.polls.ui.join;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import dev.dsluo.polls.R;

/**
 * Activity to join a group.
 *
 * @author David Luo
 */
public class JoinActivity extends AppCompatActivity {

    private JoinViewModel viewModel;

    private TextInputLayout groupIdFieldContainer;
    private EditText groupIdField;
    private Button joinIdButton;
    private Button joinQrButton;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        viewModel = ViewModelProviders.of(this).get(JoinViewModel.class);

        groupIdFieldContainer = findViewById(R.id.group_id_container);
        groupIdField = findViewById(R.id.group_id);
        joinIdButton = findViewById(R.id.join_button);
        joinQrButton = findViewById(R.id.join_with_qr_button);

        joinIdButton.setOnClickListener(view -> {
            String groupId = groupIdField.getText().toString();
            viewModel.joinGroup(groupId, isSuccessful -> {
                if (isSuccessful) {
                    Toast.makeText(this, "Joined group.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    groupIdFieldContainer.setError("Invalid group ID.");
                }
            });
        });
        joinQrButton.setOnClickListener(view -> startScanner());
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
            return;
        }
        viewModel.joinGroup(scanContent, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Joined group.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Could not join group.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
