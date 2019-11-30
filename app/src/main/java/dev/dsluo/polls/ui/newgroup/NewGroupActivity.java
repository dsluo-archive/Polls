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

    private EditText groupName;
    private TextInputLayout groupNameContainer;

    private EditText groupDescription;
    private TextInputLayout groupDescriptionContainer;

    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        viewModel = ViewModelProviders.of(this).get(NewGroupViewModel.class);

        groupName = findViewById(R.id.group_name);
        groupNameContainer = findViewById(R.id.group_name_container);

        groupDescription = findViewById(R.id.group_description);
        groupDescriptionContainer = findViewById(R.id.group_description_container);

        save = findViewById(R.id.save);
        Button cancel = findViewById(R.id.cancel);


        save.setOnClickListener(v -> handleSaveGroup());
        cancel.setOnClickListener(v -> finish());
    }

    private void handleSaveGroup() {
        String name = groupName.getText().toString();
        if (name.length() == 0) {
            groupNameContainer.setError(getString(R.string.required_hint));
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
