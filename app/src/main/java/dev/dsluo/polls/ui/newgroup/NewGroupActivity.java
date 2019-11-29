package dev.dsluo.polls.ui.newgroup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import dev.dsluo.polls.R;

public class NewGroupActivity extends AppCompatActivity {

    private NewGroupViewModel viewModel;

    private Button save;

    private EditText groupName;
    private EditText groupDescription;

    private TextInputLayout groupNameContainer;
    private TextInputLayout groupDescriptionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        viewModel = ViewModelProviders.of(this).get(NewGroupViewModel.class);

        Button cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);
        groupNameContainer = findViewById(R.id.group_name_container);
        groupDescriptionContainer = findViewById(R.id.group_description_container);

        cancel.setOnClickListener(v -> finish());
        save.setOnClickListener(v -> handleSaveGroup());
    }

    private void handleSaveGroup() {
        String name = groupName.getText().toString();
        if (name.length() == 0) {
            groupNameContainer.setError("*required");
            return;
        } else {
            groupNameContainer.setErrorEnabled(false);
        }

        String description = groupDescription.getText().toString();

        viewModel.createNewGroup(name, description, isSuccessful -> {
            if (isSuccessful)
                finish();
            else
                Snackbar.make(save, R.string.group_creation_error, Snackbar.LENGTH_LONG).show();
        });
    }
}
