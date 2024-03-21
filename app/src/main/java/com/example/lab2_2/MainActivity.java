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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, gradesEditText;
    Button gradesButton;
    private TextView averageTextView;
    Button superButton;
    Button notSoSuperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        gradesEditText = findViewById(R.id.gradesEditText);
        gradesButton = findViewById(R.id.gradesButton);
        averageTextView = findViewById(R.id.averageTextView);
        superButton = findViewById(R.id.superButton);
        notSoSuperButton = findViewById(R.id.notSoSuperButton);

        checkEmptyFields();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Dodaj ikonę strzałki powrotu w pasku akcji
        }

        gradesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gradesInput = gradesEditText.getText().toString().trim();
                if (!gradesInput.isEmpty()) {
                    int numberOfGrades = Integer.parseInt(gradesInput);
                    if (numberOfGrades >= 5 && numberOfGrades <= 15) {
                        Intent intent = new Intent(MainActivity.this, GradesActivity.class);
                        intent.putExtra(getString(R.string.numberOfGrades), numberOfGrades);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, R.string.grades_range_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.liczbe_ocen, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState != null) {
            firstNameEditText.setText(savedInstanceState.getString(getString(R.string.firstname)));
            lastNameEditText.setText(savedInstanceState.getString(getString(R.string.lastname)));
            gradesEditText.setText(savedInstanceState.getString(getString(R.string.grades)));
            gradesButton.setVisibility(savedInstanceState.getInt(getString(R.string.buttonvisibility)));
            averageTextView.setText(savedInstanceState.getString(getString(R.string.average_value))); // Przywracamy średnią ocen
        }

        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateFields();
                }
            }
        });

        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateFields();
                }
            }
        });

        gradesEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateFields();
                }
            }
        });

        // Restoring the average if it exists
        SharedPreferences preferences = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        float averageGrade = preferences.getFloat(getString(R.string.averagegrade), -1);
        if (averageGrade != -1) {
            displayAverage(averageGrade);
        }
        superButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getString(R.string.gratulacje_otrzymujesz_zaliczenie), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        notSoSuperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getString(R.string.wysy_am_podanie_o_zaliczenie_warunkowe), Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pobranie ImageView dla strzlki
        ImageView customForwardArrow = findViewById(R.id.customForwardArrow);

        // Click listener dla strzalki
        customForwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navwigacja do GradesActivity
                Intent intent = new Intent(MainActivity.this, GradesActivity.class);
                startActivity(intent);
            }
        });
    }
    private void displayAverage(float average) {
        averageTextView.setText(String.format(Locale.getDefault(), getString(R.string.average_2f), average));
        // Ustawienie widoczności elementów w zależności od średniej
        if (average >= 3) {
            superButton.setVisibility(View.VISIBLE);
            notSoSuperButton.setVisibility(View.GONE);
        } else {
            superButton.setVisibility(View.GONE);
            notSoSuperButton.setVisibility(View.VISIBLE);
        }

        // Ustawienie widoczności TextView na widoczny
        averageTextView.setVisibility(View.VISIBLE);

    }

    private void validateFields() {
        boolean isFirstNameValid = !TextUtils.isEmpty(firstNameEditText.getText().toString());
        boolean isLastNameValid = !TextUtils.isEmpty(lastNameEditText.getText().toString());
        boolean isGradesValid = false;
        try {
            int grades = Integer.parseInt(gradesEditText.getText().toString());
            isGradesValid = (grades >= 5 && grades <= 15);
        } catch (NumberFormatException e) {
            isGradesValid = false;
        }

        if (!isFirstNameValid) {
            showToastAndSetError(R.string.first_name_empty_error, firstNameEditText);
        }
        if (!isLastNameValid) {
            showToastAndSetError(R.string.last_name_empty_error, lastNameEditText);
        }
        if (!isGradesValid) {
            showToastAndSetError(R.string.grades_range_error, gradesEditText);
        }

        if (isFirstNameValid && isLastNameValid && isGradesValid) {
            gradesButton.setVisibility(View.VISIBLE);
        } else {
            gradesButton.setVisibility(View.GONE);
        }
    }


    private void showToastAndSetError(int messageResId, EditText editText) {
        Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
        editText.setError(getString(messageResId));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Zapisujemy stan
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.firstname), firstNameEditText.getText().toString());
        outState.putString(getString(R.string.lastname), lastNameEditText.getText().toString());
        outState.putString(getString(R.string.grades), gradesEditText.getText().toString());
        outState.putInt(getString(R.string.buttonvisibility), gradesButton.getVisibility());
        outState.putString(getString(R.string.average_string), averageTextView.getText().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                double average = data.getDoubleExtra(getString(R.string.averagegrade), 0.0);

                // Zapisanie average do SharedPreferences znowu, z uwzglednieniem tego, ze wartosc mogla zostac edytowana
                SharedPreferences preferences = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat(getString(R.string.averagegrade), (float) average);
                editor.apply();
                displayAverage((float) average);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Odbierz średnią ocen z SharedPreferences
        SharedPreferences preferences = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        float averageGrade = preferences.getFloat(getString(R.string.averagegrade), -1);
        if (averageGrade != -1) { // Sprawdź, czy średnia ocen została zapisana
            // Wyświetl średnią ocen
            averageTextView.setText(String.format(Locale.getDefault(), getString(R.string.average), averageGrade));
            // Wyświetl odpowiedni przycisk na podstawie średniej ocen
            if (averageGrade >= 3.0) {
                superButton.setVisibility(View.VISIBLE);
                notSoSuperButton.setVisibility(View.GONE);
                checkEmptyFields();
            } else {
                superButton.setVisibility(View.GONE);
                notSoSuperButton.setVisibility(View.VISIBLE);
                checkEmptyFields();
            }
        }
    }
    public void calculateAverage(View view) {
        // Pobieranie ocen i obliczanie średniej
        String gradesStr = gradesEditText.getText().toString();
        if (!TextUtils.isEmpty(gradesStr)) {
            String[] gradesArray = gradesStr.split(",");
            int sum = 0;
            for (String grade : gradesArray) {
                sum += Integer.parseInt(grade);
            }
            double average = (double) sum / gradesArray.length;
            averageTextView.setText(getString(R.string.average, average));

        } else {
            // Jeśli brak ocen, wyświetl komunikat
            Toast.makeText(this, getString(R.string.please_enter_grades), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkEmptyFields() {
        boolean isFirstNameEmpty = TextUtils.isEmpty(firstNameEditText.getText().toString().trim());
        boolean isLastNameEmpty = TextUtils.isEmpty(lastNameEditText.getText().toString().trim());
        boolean isGradesEmpty = TextUtils.isEmpty(gradesEditText.getText().toString().trim());

        if (isFirstNameEmpty || isLastNameEmpty || isGradesEmpty) {
            // Jesli chociaz jedno pole jest puste, to ukrywane sa buttons i srednia
            gradesButton.setVisibility(View.GONE);
            superButton.setVisibility(View.GONE);
            notSoSuperButton.setVisibility(View.GONE);
            averageTextView.setVisibility(View.GONE);
        }
        else {
            averageTextView.setVisibility(View.VISIBLE);
        }
    }


}



