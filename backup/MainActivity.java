package com.example.user.rotobld;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Button;
import android.os.SystemClock;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialAllCubes();
    }
    public void nextActivity (View view) {

        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);


    }
    public void goTo3x3Bld (View view)
    {
        Intent intent = new Intent(this, ThreeCube.class);
        startActivity(intent);
    }
    public void goTo3x3Statics (View view)
    {
        Intent intent = new Intent(this, Statics3x3.class);
        startActivity(intent);
    }
    public void goTo4x4Bld(View view)
    {
        Intent intent = new Intent(this, FourCube.class);
        startActivity(intent);
    }

    public void goTo5x5Bld(View view)
    {
        Intent intent = new Intent(this, FiveCube.class);
        startActivity(intent);
    }
    public void initialAllCubes()
    {

        String [] cornerScheme ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ" + '\u05F3',"ת","ש","ג" + '\u05F3'};
        String [] edgeScheme ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ" + '\u05F3',"ת","ש","ג" + '\u05F3'};
        String [] wingScheme5x5 ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","ש","ת","1","2"};
        String [] wingScheme4x4 ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","ש","ת","1","2"};
        String [] XCenterScheme ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ" + '\u05F3',"ת","ש","ג" + '\u05F3'};
        String [] TCenterScheme ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","ש","ת","צ" + '\u05F3',"ג" + '\u05F3'};
        String [] midgesScheme ={"א","ב","ג","ד","ה","ו","ז","ח","ט","י","כ","ל","מ","נ","ס","ע","פ","צ","ק","ר","צ" + '\u05F3',"ת","ש","ג" + '\u05F3'};


        Globals.three.setCornerScheme(cornerScheme);
        Globals.three.setEdgeScheme(edgeScheme);
        Globals.three.setCornerBuffer("א");
        Globals.three.setEdgeBuffer("ג");

        Globals.five.setCornerScheme(cornerScheme);
        Globals.five.setEdgeScheme(midgesScheme);
        Globals.five.setWingScheme(wingScheme5x5);
        Globals.five.setXCenterScheme(XCenterScheme);
        Globals.five.setTCenterScheme(TCenterScheme);

        Globals.five.setCornerBuffer("א");
        Globals.five.setEdgeBuffer("ג");
        Globals.five.setWingBuffer("ט");
        Globals.five.setXCenterBuffer("א");
        Globals.five.setTCenterBuffer("א");


        Globals.four.setCornerScheme(cornerScheme);
        Globals.four.setWingScheme(wingScheme4x4);
        Globals.four.setXCenterScheme(XCenterScheme);

        Globals.four.setCornerBuffer("א");
        Globals.four.setWingBuffer("ט");
        Globals.four.setXCenterBuffer("א");


        

    }
 
}
