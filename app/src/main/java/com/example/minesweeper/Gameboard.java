package com.example.minesweeper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

public class Gameboard {
    int row = 3;
    int col = 3;
    int num_mines = 1;
    Node[][] data = new Node[3][3];

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
    public GameStatus update_board(int input_row, int input_col) {
        System.out.println("in update board");
        System.out.println("ROW: " + input_row + "  COL:  " + input_col);
        update_helper(input_row, input_col);
        GameStatus status = check_game_status(input_row, input_col);
        if (status == GameStatus.CONTINUE){
            //System.out.println("ROW: " + input_row + "  COL:  " + input_col);
            //update_helper(input_row, input_col);
            System.out.println("!!!!!!CONTINUE!!!!!!");
            System.out.println("Mine Map:");
            print_board(true);
            System.out.println("Explored Map:");
            print_board(false);
        }else{
            if (status == GameStatus.WIN){
                System.out.println("!!!!!!YOU WIN!!!!!!");
            }else{
                System.out.println("!!!!!!YOU LOSE!!!!!!");
            }
        }
        return status;
    }

    public void update_helper(int cur_row, int cur_col) {
        // check if loc is valid
        if (cur_row >= row || cur_row < 0){
            return;
        }
        if (cur_col >= col || cur_col < 0){
            return;
        }
        System.out.println("setting" + cur_row + "x" + cur_col);
        System.out.println("explored " + data[cur_row][cur_col].explored);
        // two base cases:
        if (data[cur_row][cur_col].explored){
            // 1. revisiting node
            return;
        }
        if (data[cur_row][cur_col].surrounding_mines > 0){
            // 2. hitting boundary
            data[cur_row][cur_col].explored = true;
            return;
        }
        if (data[cur_row][cur_col].is_mine == true){
            // 3. hit mine
            data[cur_row][cur_col].explored = true;
            return;
        }
        // regular case:
        // 1. mark as explored/visited
        data[cur_row][cur_col].explored = true;
        // 2. explore around

        update_helper(cur_row-1, cur_col+1);
        update_helper(cur_row-1, cur_col);
        update_helper(cur_row-1, cur_col-1);
        update_helper(cur_row, cur_col+1);
        update_helper(cur_row, cur_col-1);
        update_helper(cur_row+1, cur_col+1);
        update_helper(cur_row+1, cur_col);
        update_helper(cur_row+1, cur_col-1);
        return;
    }
    public GameStatus check_game_status(int input_row, int input_col){
        // 1. check if lose
        if (data[input_row][input_col].is_mine){
            return GameStatus.LOSE;
        }

        // 2. check if win
        //data[input_row][input_col].explored = true;
        boolean gameover = true;
        for (int row_ = 0; row_ < row; row_++){
            for (int col_ = 0; col_ < col; col_++){
                if (!data[row_][col_].explored && !data[row_][col_].is_mine){
                    gameover = false;
                }
            }
        }
        //data[input_row][input_col].explored = false;
        if(gameover){
            return GameStatus.WIN;
        }else{
            return GameStatus.CONTINUE;
        }
    }
    public int check_status(int input_row, int input_col){
        //case 1: unexplored, (flagged or not)
        if (data[input_row][input_col].explored == false){
            return -2;
        }
        //case 2a: explored, if surrounding mines != 0, display number
          //case 2b: surrounding mines == 0, do not display
        else if (data[input_row][input_col].is_mine == false){
            return data[input_row][input_col].surrounding_mines;
        }
        //case 3: explored, mine
        else{
            return -1;
        }
    }
    public void print_board(boolean transparent){
        String str = "";
        System.out.println("-----------start--------------");
        System.out.println("size: " + row + "x" + col);
        str = "( ) ";
        for (int row_ = 0; row_ < row; row_++){
            str += (row_ + " ");
        }
        System.out.println(str+System.lineSeparator());
        for (int row_ = 0; row_ < row; row_++){
            str = "";
            str += ("(" + row_ + ") ");
            for (int col_ = 0; col_ < col; col_++){
                if(transparent){
                    if(data[row_][col_].is_mine == true){
                        str = str.concat("M");
                    }else{
                        str = str.concat("_");
                    }
                }else{
                    if(data[row_][col_].explored == true){
                        str = str.concat(String.valueOf(data[row_][col_].surrounding_mines));
                    }else{
                        str = str.concat("_");
                    }
                }
                str = str.concat(" ");
            }
            System.out.println(str+System.lineSeparator());
        }
        System.out.println("-----------end--------------");
        return;
    }

    public boolean flag_node(int row, int col){
        data[row][col].is_flagged = !data[row][col].is_flagged;
        return data[row][col].is_flagged;
    }
}
