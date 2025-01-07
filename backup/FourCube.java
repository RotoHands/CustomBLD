package com.example.user.rotobld;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

public class FourCube extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_cube);
    }

    public void goBack (View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


    }
    public long timeStopped4x4XCenters = 0;
    public long timeStopped4x4Wings = 0;
    public long timeStopped4x4Corners = 0;
    public int switchPieceCounter = 0;
    public long time4x4 = 0;


    public void  setUpCube4x4(View view) {


        Globals.four.ScrambleCurrent4x4Cube();
        String scrambleString = Globals.four.getScramble();
        String solutionPairs = Globals.four.getSolutionPairs(false, false);

        String displayAll = scrambleString + "\n" +"\n" + solutionPairs;
        TextView display = (TextView) findViewById(R.id.textView5);

        display.setText(displayAll);


    }

    public void StartStopWatch (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer3);
        chronometer.setBase(SystemClock.elapsedRealtime() + time4x4);
        chronometer.start();
    }

    public void StopStopWatch (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer3);

        time4x4 = chronometer.getBase() - SystemClock.elapsedRealtime();
        if(switchPieceCounter == 2)
        {
            timeStopped4x4Corners = time4x4 - timeStopped4x4XCenters - timeStopped4x4Wings;
        }
        String displayAll = "time is :" + (double)time4x4/-1000 + "\n" +
                "time for Centers : " + (double)timeStopped4x4XCenters +"\n" +
                "time for Wings : " + (double)timeStopped4x4Wings + "\n" +
                "time for Corners : " + (double)timeStopped4x4Corners + "\n";


        chronometer.stop();
        TextView display = (TextView) findViewById(R.id.textView6);
        display.setText(displayAll);
    }
    public void switchPiece4x4 (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer3);
        if (switchPieceCounter == 0)
        {
            timeStopped4x4XCenters = chronometer.getBase() - SystemClock.elapsedRealtime();
            switchPieceCounter++;
        }
        else if(switchPieceCounter == 1)
        {
            timeStopped4x4Wings = chronometer.getBase() - SystemClock.elapsedRealtime() - timeStopped4x4XCenters;
            switchPieceCounter++;
        }
    }

    public void ResesStopWatch (View v)
    {
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer3);
        chronometer.setBase(SystemClock.elapsedRealtime());
        time4x4 = 0;
    }


}
