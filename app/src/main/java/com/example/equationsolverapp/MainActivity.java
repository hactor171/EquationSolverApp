package com.example.equationsolverapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    ImageView equationImage;
    TextView detectedText;
    Button detectTextOnImage;
    Button loadImage;

    private static final int Perm_Code = 1001;
    private static final int Image_Code = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equationImage = (ImageView) findViewById(R.id.imageView);
        detectedText = (TextView) findViewById(R.id.textView);
        detectTextOnImage = (Button) findViewById(R.id.button);
        loadImage = (Button) findViewById(R.id.load_button);

        detectTextOnImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RecognizeText();

            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckPermission();

            }
        });
    }

    public void CheckPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, Perm_Code);
            }
            else
            {
                //permission granted
                loadImageFromGallery();
            }
        }
        else
        {
            loadImageFromGallery();
        }
    }

    public void loadImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Image_Code);
    }

    public void RecognizeText() {

        Context context = getApplicationContext();
        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

        Bitmap bitmap = ((BitmapDrawable) equationImage.getDrawable()).getBitmap();
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<TextBlock> sparseArray = textRecognizer.detect(frame);


        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < sparseArray.size(); i++)
        {
            TextBlock textBlock = sparseArray.get(i);

            String line = textBlock.getValue();

            stringBuilder.append(line);
        }

        detectedText.setText(stringBuilder);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Code && resultCode == RESULT_OK) {
            equationImage.setImageURI(data.getData());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Perm_Code:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    loadImageFromGallery();
                }
                else
                {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}