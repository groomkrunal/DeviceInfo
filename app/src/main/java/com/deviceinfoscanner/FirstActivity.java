package com.deviceinfoscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class FirstActivity extends AppCompatActivity {
    private AppCompatTextView txtScan,textViewProgress;
    private RelativeLayout rlProgress;
    int status = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        init();

    }

    public void init(){
        txtScan= findViewById(R.id.txtScan);
        textViewProgress= findViewById(R.id.textView);
        rlProgress = findViewById(R.id.rlProgress);
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.circularprogressbar);
        final ProgressBar progressBar = findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);
        progressBar.setSecondaryProgress(100);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(drawable);

        txtScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtScan.setVisibility(View.GONE);
                rlProgress.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (status < 100) {
                            status += 1;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textViewProgress.setText(String.format("%d%%", status));
                                    progressBar.setProgress(status);
                                    Intent intent = new Intent(FirstActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            try {
                                Thread.sleep(16);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

        });
    }
}