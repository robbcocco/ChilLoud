package com.starkoverflow.chilloud.Library;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.starkoverflow.chilloud.R;

public class AddLibraryActivity extends AppCompatActivity {
    private String name;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_library);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final TextInputEditText nameView = (TextInputEditText) findViewById(R.id.library_settings_name);
        final TextInputEditText descriptionView = (TextInputEditText) findViewById(R.id.library_settings_description);
        Button delete = (Button) findViewById(R.id.library_settings_delete);
        Button done = (Button) findViewById(R.id.library_settings_done);

        final int position = getIntent().getIntExtra("position", -1);
        LibraryFactory library = getIntent().getParcelableExtra("library");

        if (position != -1) {
            toolbar.setTitle("Edit library");
            delete.setVisibility(View.VISIBLE);
            nameView.setText(library.getName());
            descriptionView.setText(library.getDescription());
        } else {
            toolbar.setTitle("Add library");
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameView.getText().toString();
                description = descriptionView.getText().toString();
                if (!name.isEmpty()) {
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
}
