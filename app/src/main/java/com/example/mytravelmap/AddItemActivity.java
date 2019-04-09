package com.example.mytravelmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddItemActivity extends AppCompatActivity {
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // ImageButton
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageHelper.selectImage(AddItemActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri imageUri = PickImageHelper.getPickImageResultUri(this, data);
            System.out.println(imageUri);

            try {
                InputStream is = this.getContentResolver().openInputStream(imageUri);
                BitmapFactory.Options options= new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                Drawable img = new BitmapDrawable(getResources(), bitmap);

                imageButton.setBackground(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}