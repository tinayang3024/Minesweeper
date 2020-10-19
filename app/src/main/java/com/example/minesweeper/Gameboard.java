package com.example.minesweeper;

import java.util.Random;
//import java.util.ArrayList;

public class Gameboard {
    int row = 9;
    int col = 9;
    int num_mines = 10;
    int[][] data = new Node[0][0];

    public void set_row(
            int target_row
    ) {
        row = target_row;
    }
    public void set_col(
            int target_col
    ) {
        col = target_col;
    }
    public void set_num_mines(
            int target_num_mines
    ) {
        num_mines = target_num_mines;
    }

}
