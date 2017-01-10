package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button_instructions_onClick(View view) {
        Intent intent = new Intent(this, InstructionsActivity.class);

        startActivity(intent);
    }
}
