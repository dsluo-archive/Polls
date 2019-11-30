package dev.dsluo.polls.ui.newpoll;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import dev.dsluo.polls.R;
import dev.dsluo.polls.data.models.Group;

/**
 * Activity for creating new polls.
 *
 * @author David Luo
 */
public class NewPollActivity extends AppCompatActivity {

    public static final int DEFAULT_NUM_CHOICES = 3;

    private NewPollViewModel viewModel;

    private EditText pollQuestion;
    private TextInputLayout pollQuestionContainer;

    private AutoCompleteTextView pollGroup;
    private TextInputLayout pollGroupContainer;
    private ArrayAdapter<Group> pollGroupArrayAdapter;

    private LinearLayout choicesContainer;
    private Button save;

    private int choiceCount = 0;

    private Group selectedGroup = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);

        viewModel = ViewModelProviders.of(this).get(NewPollViewModel.class);

        pollQuestion = findViewById(R.id.poll_question);
        pollQuestionContainer = findViewById(R.id.poll_question_container);

        pollGroup = findViewById(R.id.poll_group);
        pollGroupContainer = findViewById(R.id.poll_group_container);

        choicesContainer = findViewById(R.id.poll_choices_container);

        save = findViewById(R.id.save);
        Button cancel = findViewById(R.id.cancel);

        pollGroupArrayAdapter = new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item);
        pollGroup.setAdapter(pollGroupArrayAdapter);
        viewModel.getGroups()
                .observe(this, groups -> {
                    pollGroupArrayAdapter.clear();
                    pollGroupArrayAdapter.addAll(groups);
                    pollGroupArrayAdapter.notifyDataSetChanged();
                });

        pollGroup.setOnItemClickListener(
                (parent, view, position, id) -> selectedGroup = (Group) parent.getItemAtPosition(position)
        );

        for (int i = 0; i < DEFAULT_NUM_CHOICES; i++)
            appendChoiceField();

        save.setOnClickListener(v -> handleSavePoll());
        cancel.setOnClickListener(v -> finish());
    }

    /**
     * Adds another choice field to the form.
     */
    private void appendChoiceField() {
        TextInputLayout choice = (TextInputLayout) getLayoutInflater()
                .inflate(R.layout.poll_choice, choicesContainer, false);
        choice.setHint(getString(R.string.poll_choice_hint, ++choiceCount));
        EditText choiceInput = choice.getEditText();
        choiceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                TextInputLayout last = (TextInputLayout) choicesContainer.getChildAt(choicesContainer.getChildCount() - 1);
                if (last.getEditText().getText().toString().length() > 0)
                    appendChoiceField();
            }
        });
        choicesContainer.addView(choice);
    }

    /**
     * Handle what happens when the save button is clicked.
     * Handles form validation and invoking the corresponding database methods.
     */
    private void handleSavePoll() {
        int errorCount = 0;

        String question = pollQuestion.getText().toString();
        if (question.length() == 0) {
            pollQuestionContainer.setError(getString(R.string.required_hint));
            errorCount++;
        } else {
            pollQuestionContainer.setErrorEnabled(false);
        }

        if (selectedGroup == null) {
            pollGroupContainer.setError(getString(R.string.required_hint));
            errorCount++;
        } else {
            pollGroupContainer.setErrorEnabled(false);
        }

        if (errorCount > 0)
            return;


        List<String> choices = new ArrayList<>();

        for (int i = 0; i < choicesContainer.getChildCount(); i++) {
            TextInputLayout choiceContainer = (TextInputLayout) choicesContainer.getChildAt(i);
            String choice = choiceContainer.getEditText().getText().toString();
            if (choice.length() > 0)
                choices.add(choice);
        }

        viewModel.createNewPoll(selectedGroup, question, choices, isSuccessful -> {
            if (isSuccessful)
                finish();
            else
                Snackbar.make(save, R.string.poll_creation_error, Snackbar.LENGTH_LONG);
        });
    }
}
