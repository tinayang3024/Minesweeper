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
    public void initBoard() {
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
    public Gameboard update_board(int input_row, int input_col) {
        GameStatus status = check_game_status(input_row, input_col);
        update_helper(input_row, input_row);
    }

    public void update_helper(int cur_row, int cur_col) {
        // check if loc is valid
        if (cur_row >= row || cur_row < 0){
            return;
        }
        if (cur_col >= col || cur_col < 0){
            return;
        }
        // two base cases:
        if (data[cur_row][cur_col].explored){
            // 1. revisiting node
            return;
        }
        if (data[cur_row][cur_col].surrounding_mines > 0){
            // 2. hitting boundary
            return;
        }
        // regular case:
        // 1. mark as explored/visited
        data[cur_row][cur_col].explored = true;
        // 2. explore around
        update_helper(cur_row+1, cur_row+1);
        update_helper(cur_row-1, cur_row+1);
        update_helper(cur_row+1, cur_row-1);
        update_helper(cur_row-1, cur_row-1);
        return;
    }
    public GameStatus check_game_status(int input_row, int input_col){
        // 1. check if lose
        if (data[input_row][input_col].is_mine){
            return GameStatus.LOSE;
        }

        // 2. check if win
        data[input_row][input_col].explored = true;
        boolean gameover = true;
        for (int row = 0; row < row; row++){
            for (int col = 0; col < col; col++){
                if (!data[row][col].explored && !data[row][col].is_mine){
                    gameover = false;
                }
            }
        }
        data[input_row][input_col].explored = false;
        if(gameover){
            return GameStatus.WIN;
        }else{
            return GameStatus.CONTINUE;
        }
    }
}
