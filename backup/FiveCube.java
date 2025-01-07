package com.example.user.rotobld;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class FiveCube extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_cube);
    }
    public void goBack (View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }

    public long timeStopped5x5XCenters = 0;
    public long timeStopped5x5TCenters = 0;
    public long timeStopped5x5Midges = 0;
    public long timeStopped5x5Wings = 0;
    public long timeStopped5x5Corners = 0;
    public int switchPieceCounter = 0;
    public long time5x5 = 0;


    public void  setUpCube5x5(View view) {


        Globals.five.ScrambleCurrent5x5Cube();
        String scrambleString = Globals.five.getScramble();
        String solutionPairs = Globals.five.getSolutionPairs(false, false);

        String displayAll = scrambleString + "\n" +"\n" + solutionPairs;
        TextView display = (TextView) findViewById(R.id.textView3);

        display.setText(displayAll);


    }

    public void switchPiece5x5(View v)
    {

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer4);
        if (switchPieceCounter == 0)
        {
            timeStopped5x5TCenters = chronometer.getBase() - SystemClock.elapsedRealtime();
            switchPieceCounter++;
        }
        else if(switchPieceCounter == 1)
        {
            timeStopped5x5XCenters = chronometer.getBase() - SystemClock.elapsedRealtime() - timeStopped5x5TCenters;
            switchPieceCounter++;
        }
       else  if(switchPieceCounter == 2)
        {
            timeStopped5x5Wings = chronometer.getBase() - SystemClock.elapsedRealtime() - timeStopped5x5XCenters - timeStopped5x5TCenters;
            switchPieceCounter++;
        }
       else
        {
            timeStopped5x5Midges = chronometer.getBase() - SystemClock.elapsedRealtime() - timeStopped5x5Wings -timeStopped5x5XCenters - timeStopped5x5TCenters;
            switchPieceCounter++;
        }

    }
    public void StartStopWatch (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer4);
        chronometer.setBase(SystemClock.elapsedRealtime() + time5x5);
        chronometer.start();
    }

    public void StopStopWatch (View v)
    {

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer4);
        time5x5 = chronometer.getBase() - SystemClock.elapsedRealtime();
        if(switchPieceCounter == 4)
        {
            timeStopped5x5Corners = chronometer.getBase() - SystemClock.elapsedRealtime() - timeStopped5x5Midges - timeStopped5x5Wings - timeStopped5x5TCenters - timeStopped5x5XCenters;
            switchPieceCounter = 0;
        }
        String displayAll = "time is :" + (double)time5x5/-1000 +"\n" +
                "time for TCenters : " + (double)timeStopped5x5TCenters/-1000 + "\n" +
                "time for XCenters : " + (double)timeStopped5x5XCenters/-1000 + "\n" +
                "time for Wings : " + (double)timeStopped5x5Wings/-1000 + "\n" +
                "time for Midges : " + (double)timeStopped5x5Midges/-1000 + "\n" +
                "time for Corners : " + (double)timeStopped5x5Corners/-1000;

        chronometer.stop();
        TextView display = (TextView) findViewById(R.id.textView7);
        display.setText(displayAll);
    }

    public void ResesStopWatch (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer4);
        chronometer.setBase(SystemClock.elapsedRealtime());
        time5x5 = 0;
    }

}
