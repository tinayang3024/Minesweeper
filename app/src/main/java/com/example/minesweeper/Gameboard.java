package com.example.minesweeper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class Gameboard {
    int row = 9;
    int col = 9;
    int num_mines = 10;
    Node[][] data = new Node[9][9];

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initBoard(
            ) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                data[i][j] = new Node();
            }
        }

        Random rand = new Random();
        int rand_pos_bound = row * col;
        ArrayList<Integer> taken_pos = new ArrayList<Integer>();

        for (int mine_count = 0; mine_count < num_mines; mine_count++) {
            boolean duplicate = true;
            int rand_pos = 0;
            while (duplicate) {
                duplicate = false;
                rand_pos = rand.nextInt(rand_pos_bound);
                for (int i = 0; i < taken_pos.size(); i++) {
                    if (rand_pos == taken_pos.get(i)) {
                        duplicate = true;
                        break;
                    }
                }
            }
            taken_pos.add(rand_pos);
        }

        for (int i = 0; i < taken_pos.size(); i++) {
            int mine_row = Math.floorDiv(taken_pos.get(i), col);
            int mine_col = taken_pos.get(i) % col;

            data[mine_row][mine_col].is_mine = true;
            for (int j = -1; j <= 1; j++) {
                if ((mine_row == 0 && j == -1) || (mine_row == row - 1 && j == 1) ) {
                    continue;
                }
                for (int k = -1; k <=1; k++) {
                    if ((mine_col == 0 && k == -1) || (mine_col == col - 1 && k == 1) ) {
                        continue;
                    }
                    data[mine_row+j][mine_col+k].surrounding_mines++;
                }
            }
            data[mine_row][mine_col].surrounding_mines= -1;
        }

    }
}
