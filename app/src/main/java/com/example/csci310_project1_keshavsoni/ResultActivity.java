package com.example.csci310_project1_keshavsoni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private TextView timeCountTextView;
    private Button restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = findViewById(R.id.resultTextView);
        timeCountTextView = findViewById(R.id.timeCountTextView);
        restartButton = findViewById(R.id.restartButton);

        Intent intent = getIntent();
        int gameStatus = intent.getIntExtra("gameStatus", 0);
        int timeUsed = intent.getIntExtra("timeCount", 0);

        if (gameStatus == 1) {
            resultTextView.setText("You Won.\nGood Job!");
        } else if (gameStatus == 2) {
            resultTextView.setText("You Lost.\nTry Again!");
        } else {
            resultTextView.setText("Game Over");
        }

        timeCountTextView.setText("Used " + timeUsed + " seconds");

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
