package com.example.csci310_project1_keshavsoni;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Minesweeper minesweeper;

    private TextView flagTextView;
    private TextView timeTextView;
    private ImageButton modeImageButton;
    private GridLayout gridLayout;

    private boolean isFlagMode = false;
    private Button[][] gridButtons;

    private Handler timerHandler = new Handler();
    private int timeCount = 0;
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            timeCount++;
            timeTextView.setText(String.valueOf(timeCount));
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minesweeper = new Minesweeper();

        flagTextView = findViewById(R.id.flagTextView);
        timeTextView = findViewById(R.id.timeCountTextView);
        modeImageButton = findViewById(R.id.modeImageButton);
        gridLayout = findViewById(R.id.gridLayout);

        timerHandler.postDelayed(timerRunnable, 1000);

        int rows = minesweeper.getRows();
        int cols = minesweeper.getCols();
        gridButtons = new Button[rows][cols];

        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                final int row = i;
                final int col = j;

                Button button = new Button(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.columnSpec = GridLayout.spec(j, 1f);
                params.rowSpec = GridLayout.spec(i, 1f);
                params.setMargins(1, 1, 1, 1);

                button.setLayoutParams(params);

                button.setBackgroundColor(Color.GREEN);

                gridLayout.addView(button);

                gridButtons[row][col] = button;

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isFlagMode) {
                            minesweeper.toggleFlag(row, col);
                            updateButton(row, col);
                            updateFlagTextView();
                            checkGameStatus();
                        } else {
                            minesweeper.clickCell(row, col);
                            updateViewGrid();
                            checkGameStatus();
                        }

                        if (minesweeper.getGameStatus() != 0) {
                            goToResultPage();
                        }
                    }
                });
            }
        }

        modeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFlagMode = !isFlagMode;
                if (isFlagMode) {
                    modeImageButton.setImageResource(R.drawable.flag_icon);
                } else {
                    modeImageButton.setImageResource(R.drawable.dig_icon);
                }
            }
        });

        updateFlagTextView();
    }

    private void updateFlagTextView() {
        flagTextView.setText(String.valueOf(minesweeper.getMines() - minesweeper.getFlags()));
    }

    private void updateViewGrid() {
        for (int i = 0; i < minesweeper.getRows(); i++) {
            for (int j = 0; j <  minesweeper.getCols(); j++) {
                updateButton(i, j);
            }
        }
    }

    private void updateButton(int row, int col) {
        int cellStatus = minesweeper.getDisplayGrid()[row][col];

        Button button = gridButtons[row][col];

        if (cellStatus == 0) {
            button.setText("");
            button.setBackgroundColor(Color.GREEN);
        } else if (cellStatus == 1) {
            int value = minesweeper.getMainGrid()[row][col];
            if (value == 0) {
                button.setText("");
            } else if (value > 0) {
                button.setText(String.valueOf(value));
            }
            button.setBackgroundColor(Color.GRAY);
        } else if (cellStatus == 2) {
            button.setText("");
            button.setBackgroundResource(R.drawable.flag_icon);
        }
    }

    private void checkGameStatus() {
        minesweeper.checkWin();
        int gameStatus = minesweeper.getGameStatus();
        if (gameStatus == 1) {
            timerHandler.removeCallbacks(timerRunnable);
            updateViewGrid();
        } else if (gameStatus == 2) {
            timerHandler.removeCallbacks(timerRunnable);
            revealAllMines();
        }
    }

    private void revealAllMines() {
        int[][] mainGrid = minesweeper.getMainGrid();
        int rows = minesweeper.getRows();
        int cols = minesweeper.getCols();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mainGrid[i][j] == -1) {
                    gridButtons[i][j].setText("M");
                    gridButtons[i][j].setBackgroundColor(Color.RED);
                }
            }
        }
    }

    private void goToResultPage() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("gameStatus", minesweeper.getGameStatus());
        intent.putExtra("timeCount", timeCount);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
