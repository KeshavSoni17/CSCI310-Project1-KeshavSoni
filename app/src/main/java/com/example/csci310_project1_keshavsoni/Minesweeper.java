package com.example.csci310_project1_keshavsoni;

import java.util.Random;

public class Minesweeper {
    private int[][] mainGrid; //0 - default, -1 - mine; int - adjacent mines
    private int[][] displayGrid; //0 - unopened; 1 - opened; 2 - flag

    private int rows = 12;
    private int cols = 10;
    private int mines = 4;
    private int flags = 0;

    private int gameStatus = 0; //0 - default; 1 - won; 2 - lost

    public Minesweeper() {
        mainGrid = new int[rows][cols];
        displayGrid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mainGrid[i][j] = 0;
                displayGrid[i][j] = 0;
            }
        }

        Random random = new Random();
        int currentMinesPlaced = 0;

        while (currentMinesPlaced < mines) {
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(cols);

            if (mainGrid[randomRow][randomCol] == 0) {
                mainGrid[randomRow][randomCol] = -1;
                currentMinesPlaced++;
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mainGrid[i][j] == -1) {
                    continue;
                }

                int mineCount = 0;
                for (int r = -1; r <= 1; r++) {
                    for (int c = -1; c <= 1; c++) {
                        int neighborRow = i + r;
                        int neighborCol = j + c;
                        if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols && mainGrid[neighborRow][neighborCol] == -1) {
                            mineCount++;
                        }
                    }
                }
                mainGrid[i][j] = mineCount;
            }
        }
    }

    public void toggleFlag(int row, int col){
        if(displayGrid[row][col] == 0)
        {
            if(flags < mines)
            {
                displayGrid[row][col] = 2;
                flags++;
            }
        }
        else if (displayGrid[row][col] == 2)
        {
            displayGrid[row][col] = 0;
            flags--;
        }
    }

    public void clickCell(int row, int col){
        if (displayGrid[row][col] == 2) {
            return;
        }

        if(mainGrid[row][col] == -1)
        {
            gameStatus = 2;
            return;
        }

        revealCell(row, col);
    }

    public void revealCell(int row, int col){
        if (row < 0 || row >= rows || col < 0 || col >= cols || displayGrid[row][col] == 1 || displayGrid[row][col] == 2) {
            return;
        }

        if(mainGrid[row][col] == -1) {
            gameStatus = 2;
            return;
        }

        displayGrid[row][col] = 1;

        if(mainGrid[row][col] > 0)
        {
            return;
        }

        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                int neighborRow = row + r;
                int neighborCol = col + c;

                revealCell(neighborRow, neighborCol);
            }
        }
    }

    public void checkWin() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mainGrid[i][j] != -1 && displayGrid[i][j] != 1) {
                    return;
                }
            }
        }

        gameStatus = 1;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getFlags() {
        return flags;
    }

    public int getGameStatus() {
        return gameStatus;
    }

    public int[][] getDisplayGrid() {
        return displayGrid;
    }

    public int[][] getMainGrid() {
        return mainGrid;
    }

    public int getMines() {
        return mines;
    }
}
