package com.example.equationsolverapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {

    ImageView equationImage;
    TextView detectedText;
    Button detectTextOnImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        equationImage = (ImageView) findViewById(R.id.imageView);
        detectedText = (TextView) findViewById(R.id.textView);
        detectTextOnImage = (Button) findViewById(R.id.button);

        detectTextOnImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                RecognizeText();

            }
        });
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
            System.out.println(line);

        }

        detectedText.setText(stringBuilder);

    }
}