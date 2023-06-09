package com.example.mydaviplata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EleccionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion);
    }

    public void crear (View view){
        Intent crear = new Intent(this, RegistroActivity.class);
        startActivity(crear);
    }

    public void tengo (View view) {
        Intent tengo = new Intent(this, LoginActivity.class);
        startActivity(tengo);
    }
}