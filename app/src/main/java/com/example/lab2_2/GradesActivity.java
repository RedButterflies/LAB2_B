package com.example.lab2_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GradesActivity extends AppCompatActivity {

    Button calculateButton;

    private EditText[] editTexts;
    private String[] subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        calculateButton = findViewById(R.id.calculateButton);

        subjects = getResources().getStringArray(R.array.subjects);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Dodaj ikonę strzałki powrotu w pasku akcji
        }
        LinearLayout layout = findViewById(R.id.gradesLayout);

        if (savedInstanceState != null) {
            // If the activity is recreated, restore the previous state (e.g., entered grades)
            int numberOfGrades = savedInstanceState.getInt(getString(R.string.numberOfGrades));
            editTexts = new EditText[numberOfGrades];
            for (int i = 0; i < numberOfGrades; i++) {
                TextView textView = new TextView(this);
                textView.setText(subjects[i % subjects.length]);
                layout.addView(textView);

                EditText editText = new EditText(this);
                editText.setId(View.generateViewId());
                editText.setHint(getString(R.string.wprowadz_ocene) + (i + 1));
                editText.setText(savedInstanceState.getString(getString(R.string.ocena) + i, ""));
                layout.addView(editText);
                editTexts[i] = editText;
            }
        } else {
            // Otherwise, create new EditText fields
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int numberOfGrades = extras.getInt(getString(R.string.numberOfGrades));
                editTexts = new EditText[numberOfGrades];
                for (int i = 0; i < numberOfGrades; i++) {
                    TextView textView = new TextView(this);
                    textView.setText(subjects[i % subjects.length]);
                    layout.addView(textView);

                    EditText editText = new EditText(this);
                    editText.setId(View.generateViewId());
                    editText.setHint(getString(R.string.enter_grade) + (i + 1));
                    layout.addView(editText);
                    editTexts[i] = editText;
                }


                //Button calculateButton = new Button(this);
                //calculateButton.setText("Oblicz średnią");
                calculateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean allGradesEntered = true;
                        double sum = 0;
                        int count = 0;
                        for (EditText editText : editTexts) { // Loop through the array of EditTexts
                            String gradeText = editText.getText().toString();
                            if (TextUtils.isEmpty(gradeText)) {
                                allGradesEntered = false;
                                editText.setError(getString(R.string.please_enter_a_grade));
                            } else {
                                double grade = Double.parseDouble(gradeText);
                                if (grade >= 2 && grade <= 5) {
                                    sum += grade;
                                    count++;
                                } else {
                                    editText.setError(getString(R.string.grade_must_be_between_2_and_5));
                                    return; // Exit the onClick method immediately
                                }
                            }
                        }
                        if (allGradesEntered) {
                            double average = sum / count;
                            SharedPreferences preferences = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putFloat(getString(R.string.averagegrade), (float) average);
                            editor.apply();

                            Intent intent = new Intent();
                            intent.putExtra(getString(R.string.averagegrade),average);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Toast.makeText(GradesActivity.this, getString(R.string.please_enter_all_grades), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //layout.addView(calculateButton);
            }
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the custom back arrow ImageView
        ImageView customBackArrow = findViewById(R.id.customBackArrow);

        // Set click listener for the custom back arrow
        customBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the MainActivity
                Intent intent = new Intent(GradesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the entered grades when the activity is destroyed
        if (editTexts != null) {
            for (int i = 0; i < editTexts.length; i++) {
                if (editTexts[i] != null) {
                    outState.putString(getString(R.string.grade) + i, editTexts[i].getText().toString());
                }
            }
            outState.putInt(getString(R.string.numberOfGrades), editTexts.length);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
