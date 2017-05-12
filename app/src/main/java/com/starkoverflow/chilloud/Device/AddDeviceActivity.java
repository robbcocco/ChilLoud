package com.starkoverflow.chilloud.Device;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.starkoverflow.chilloud.R;

public class AddDeviceActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private String name;
    private Bitmap picture;
    private ImageView picturePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add device");

        ImageView pictureOverlay = (ImageView) findViewById(R.id.device_settings_picture_overlay);
        picturePreview = (ImageView) findViewById(R.id.device_settings_picture);
        final TextInputEditText nameView = (TextInputEditText) findViewById(R.id.device_settings_name);
        Button done = (Button) findViewById(R.id.device_settings_done);

        pictureOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameView.getText().toString();
                if (!name.isEmpty()) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("name", name);
                    resultIntent.putExtra("picture", picture);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    nameView.setError("Insert a name");
                }
            }
        });

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            picture = (Bitmap) extras.get("data");
            picturePreview.setImageBitmap(picture);
        }
    }
}
