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
import java.util.ArrayList;

enum GameStatus {
    WIN,
    LOSE,
    CONTINUE
}

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

            timer.setText(String.format("Timer: %02d:%02d", minutes, seconds));
            timer_handler.postDelayed(this,500);
        }
    };
    Runnable timer_zero = new Runnable() {
        @Override
        public void run() {

            timer.setText(String.format("Timer: 00:00"));
            timer_handler.postDelayed(this,500);
        }
    };

    //Gameboard, roll take user input
    int max_row = 3;
    int max_col = 3;
    Gameboard game = new Gameboard();
    ArrayList<View> game_buttons = new ArrayList<View>();
    boolean first_click = true;
    //on click logic
    OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.button_reset){
                //restart timer
                start_time= System.currentTimeMillis();
                timer_handler.removeCallbacks(timer_runnable);
                timer_handler.post(timer_zero);
                //restart game
                game.initBoard();
                //reset all button view
                for (int i = 0; i < game_buttons.size(); i++) {
                    System.out.println(game_buttons.get(i).getTag().toString());
                    ((Button) game_buttons.get(i)).setText("");
                    ((Button) game_buttons.get(i)).setEnabled(true);
                }
                first_click = true;
            }
            else {
                if (first_click) {
                    first_click = false;
                    start_time = System.currentTimeMillis();
                    timer_handler.removeCallbacks(timer_zero);
                    timer_handler.post(timer_runnable);
                }
                //gameboard action, get button ID -> row/col

                String button_id = view.getTag().toString();
                int row = Integer.parseInt(button_id.substring(0,3));
                int col = Integer.parseInt(button_id.substring(3,6));
                GameStatus game_status = game.update_board(row,col);

                //loop through gameboard then update view

                for (int i = 0; i < game_buttons.size(); i++) {
                    //update button image and disable button if needed
                    int status = game.check_status(i/max_row,i%max_col);
                    if (status != -2){
                        ((Button) game_buttons.get(i)).setEnabled(false);
                    }
                    if (status == -1){
                        ((Button) game_buttons.get(i)).setText("X");
                    }
                    else if (status == 0){
                        ((Button) game_buttons.get(i)).setText(Integer.toString(status));
                    }
                    else if (status > 0){
                        ((Button) game_buttons.get(i)).setText(Integer.toString(status));
                    }
                }
                if (game_status != GameStatus.CONTINUE){
                    //disable all buttons
                    for (int i = 0; i < game_buttons.size(); i++) {
                        ((Button) game_buttons.get(i)).setEnabled(false);
                    }
                    start_time= System.currentTimeMillis();
                    timer_handler.removeCallbacks(timer_runnable);
                }
                if (game_status == GameStatus.WIN){

                }
                if (game_status == GameStatus.LOSE){
                    //((Button) game_buttons.get(row  * max_row + col )).setText("X");
                }
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
        timer_handler.post(timer_zero);
        //game start (take input)
        game.initBoard();

        Button reset_button = (Button) view.findViewById(R.id.button_reset);
        reset_button.setOnClickListener(onClickListener);
        view.findViewsWithText(game_buttons, "gameboard", View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        for (int i = 0; i < game_buttons.size(); i++) {
            game_buttons.get(i).setOnClickListener(onClickListener);
        }
    }






}