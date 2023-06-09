package com.example.mydaviplata;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class extraActivity extends AppCompatActivity {

    private TextView codigo, cvc, nomusu;
    private BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        codigo = findViewById(R.id.barra);
        cvc = findViewById(R.id.cvc);
        nomusu = findViewById(R.id.banco);
        baseDatos = new BaseDatos(this);


        String names = getIntent().getStringExtra("name");
        nomusu.setText(names);

        codigo();
        cvc();
    }


    private void codigo() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;
        int count = 4;
        StringBuilder numbersBuilder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            String formattedNumber = String.format("%04d", randomNumber);
            numbersBuilder.append(formattedNumber);

            if ((i + 1) % 4 == 0 && i != count - 1) {
                numbersBuilder.append(" ");
            }
        }

        String numbersString = numbersBuilder.toString();

        String separarNumeros = baseDatos.separarNumeros(numbersString, 4);
        codigo.setText(separarNumeros);
    }

    private void cvc() {
        Random random = new Random();
        int min = 0;
        int max = 99;
        int count = 16;
        StringBuilder numbersBuilder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(max - min + 1) + min;
            String formattedNumber = String.format("%02d", randomNumber);
            numbersBuilder.append(formattedNumber);
        }

        String numbersString = numbersBuilder.toString();
        cvc.setText(numbersString);
    }
}