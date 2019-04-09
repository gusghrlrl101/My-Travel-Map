package com.example.mytravelmap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class AddItemActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private Drawable buttonImg;

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
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 10;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
                buttonImg = new BitmapDrawable(getResources(), bitmap);

                imageButton.setImageDrawable(buttonImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void addItem(View view) {
        EditText editTitle = (EditText) findViewById(R.id.editText_title);
        EditText editContent = (EditText) findViewById(R.id.editText_content);

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();

        if (buttonImg == null) {
            Toast toast = Toast.makeText(this, "사진을 정해주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else if (TextUtils.isEmpty(title)) {
            Toast toast = Toast.makeText(this, "제목을 적어주세요", Toast.LENGTH_SHORT);
            toast.show();
        } else if (TextUtils.isEmpty(content)) {
            Toast toast = Toast.makeText(this, "내용을 적어주세요", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}