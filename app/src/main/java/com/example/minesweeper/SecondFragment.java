package com.example.minesweeper;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

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

        view.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //restart timer
                start_time= System.currentTimeMillis();
                timer_handler.postDelayed(timer_runnable, 0);
            }
        });
    }


}