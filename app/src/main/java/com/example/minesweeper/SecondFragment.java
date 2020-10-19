package com.example.minesweeper;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

enum GameStatus {
    WIN,
    LOSE,
    CONTINUE
}

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    // Timer logic
    TextView timer;
    long start_time = 0;
    Handler timer_handler = new Handler();
    Runnable timer_runnable = new Runnable() {
        @Override
        public void run() {
            long ms = System.currentTimeMillis() - start_time;
            int seconds = (int) (ms/1000) % 60;
            int minutes = (int) (ms/1000) / 60;

            timer.setText(String.format("Timer: %d:%02d", minutes, seconds));
            timer_handler.postDelayed(this,500);
        }
    };

    //Gameboard
    Gameboard game = new Gameboard();

    //on click logic
    OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button_reset){
                //restart timer
                start_time= System.currentTimeMillis();
                timer_handler.postDelayed(timer_runnable, 0);
                //restart game
                game.initBoard();
            }
            else{
                start_time= System.currentTimeMillis();
                timer_handler.postDelayed(timer_runnable, 0);
                //gameboard action
                //call recursive function
                //if not lose, call display function to update gameboard
                //else, display lose page
            }
        }
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //timer start automatically
        start_time = System.currentTimeMillis();
        timer = (TextView) view.findViewById(R.id.text_view_timer);
        timer_handler.postDelayed(timer_runnable, 0);
        //game start
        game.initBoard();

        Button reset_button = (Button) view.findViewById(R.id.button_reset);
        reset_button.setOnClickListener(onClickListener);
        ArrayList<View> game_buttons = new ArrayList<View>();
        view.findViewsWithText(game_buttons, "gameboard", View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        for (int i = 0; i < game_buttons.size(); i++) {
            game_buttons.get(i).setOnClickListener(onClickListener);
        }
    }

    public Gameboard update_board(Gameboard gameboard, int input_row, int input_col) {
        GameStatus status = check_game_status(gameboard, input_row, input_col);
        update_helper(gameboard, input_row, input_row);
        return gameboard;
    }

    public void update_helper(Gameboard gameboard, int cur_row, int cur_col) {
        // check if loc is valid
        if (cur_row >= gameboard.row || cur_row < 0){
            return;
        }
        if (cur_col >= gameboard.col || cur_col < 0){
            return;
        }
        // two base cases:
        if (gameboard.data[cur_row][cur_col].explored){
            // 1. revisiting node
            return;
        }
        if (gameboard.data[cur_row][cur_col].surrounding_mines > 0){
            // 2. hitting boundary
            return;
        }
        // regular case:
        // 1. mark as explored/visited
        gameboard.data[cur_row][cur_col].explored = true;
        // 2. explore around
        update_helper(gameboard, cur_row+1, cur_row+1);
        update_helper(gameboard, cur_row-1, cur_row+1);
        update_helper(gameboard, cur_row+1, cur_row-1);
        update_helper(gameboard, cur_row-1, cur_row-1);
        return;
    }
    public GameStatus check_game_status(Gameboard gameboard, int input_row, int input_col){
        // 1. check if lose
        if (gameboard.data[input_row][input_col].is_mine){
            return GameStatus.LOSE;
        }

        // 2. check if win
        gameboard.data[input_row][input_col].explored = true;
        boolean gameover = true;
        for (int row = 0; row < gameboard.row; row++){
            for (int col = 0; col < gameboard.col; col++){
                if (!gameboard.data[row][col].explored && !gameboard.data[row][col].is_mine){
                    gameover = false;
                }
            }
        }
        gameboard.data[input_row][input_col].explored = false;
        if(gameover){
            return GameStatus.WIN;
        }else{
            return GameStatus.CONTINUE;
        }
    }




}