package com.starkoverflow.chilloud.Library;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.starkoverflow.chilloud.R;

public class AddLibraryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String name;
    private String description;
    private boolean isLogged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextInputEditText nameView = (TextInputEditText) findViewById(R.id.library_settings_name);
//        final TextInputEditText descriptionView = (TextInputEditText) findViewById(R.id.library_settings_description);
        final Button login = (Button) findViewById(R.id.library_settings_login);
        final Button logged = (Button) findViewById(R.id.library_settings_logged);
        Button delete = (Button) findViewById(R.id.library_settings_delete);
        Button done = (Button) findViewById(R.id.library_settings_done);

        Spinner spinner = (Spinner) findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.services_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final int position = getIntent().getIntExtra("position", -1);
        LibraryFactory library = getIntent().getParcelableExtra("library");

        if (position != -1) {
            toolbar.setTitle("Edit library");
            delete.setVisibility(View.VISIBLE);
            nameView.setText(library.getName());
//            descriptionView.setText(library.getDescription());
        } else {
            toolbar.setTitle("Add library");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLogged=true;
                login.setVisibility(View.GONE);
                logged.setVisibility(View.VISIBLE);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameView.getText().toString();
                if (!name.isEmpty()) {
                    if (isLogged) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("name", name);
                        resultIntent.putExtra("description", description);
                        if (position != -1) {
                            resultIntent.putExtra("delete", false);
                            resultIntent.putExtra("position", position);
                        }
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    } else {
                        login.setError("You need to log in");
                    }
                } else {
                    nameView.setError("Insert a name");
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("delete", true);
                resultIntent.putExtra("position", position);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
         description = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
