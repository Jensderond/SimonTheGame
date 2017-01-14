package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button instructionsButton, aboutusButton, classicbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        aboutusButton = (Button) findViewById(R.id.button_about_us);
        instructionsButton = (Button) findViewById(R.id.button_instructions);
        classicbutton = (Button) findViewById(R.id.button_start);

        setOnClickListeners();
    }

    public void setOnClickListeners(){
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });
        aboutusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        classicbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(),ClassicActivity.class);
                startActivity(intent);
            }
        });
    }
}
