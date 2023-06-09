package com.example.mydaviplata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class CuerpoActivity extends AppCompatActivity {

    private static final int REQUEST_TRANSFER = 1;
    private TextView saldo;
    private TextView Saludo;
    private ImageButton salir2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);


        Intent intent = getIntent();

        ArrayList<String> tabla = intent.getStringArrayListExtra("made");

        saldo = findViewById(R.id.saldo);


        StringBuilder stringBuilder = new StringBuilder();
        for (String fila : tabla) {
            stringBuilder.append(fila).append("\n");
        }
        saldo.setText(stringBuilder.toString());

        ArrayList<String> tabla1 = intent.getStringArrayListExtra("kike");
        Saludo = findViewById(R.id.saludo);
        StringBuilder fe = new StringBuilder();
        for (String fil : tabla1) {
            fe.append(fil).append("\n");
        }
        Saludo.setText(fe.toString());

        salir2 = findViewById(R.id.salida);
        salir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacionDialog();
            }
        });
    }

    public void tarjeta(View view) {
        String name = getIntent().getStringExtra("name");
        Intent intent = new Intent(this, extraActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
    }


    public void historial(View view) {
        String wposs = getIntent().getStringExtra("cedulaI");
        Intent intent = new Intent(this, HistorialActivity.class);
        intent.putExtra("wposs", wposs);

        startActivity(intent);

    }


    public void pasar(View view) {
        String remi = getIntent().getStringExtra("cedulaI");
        Intent intent = new Intent(this, EnviarActivity.class);
        intent.putExtra("remi", remi);
        startActivityForResult(intent, REQUEST_TRANSFER);

    }
    public void salir (View view){
        finish();
    }

    public void actualiza(String remi) {
        BaseDatos baseDatos = new BaseDatos(this);
        String saldo2 = String.valueOf(baseDatos.consultarSaldo(remi));
        saldo.setText(saldo2);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TRANSFER) {
            if (resultCode == RESULT_OK && data != null) {
                boolean actualiza = data.getBooleanExtra("actualiza", false);
                if (actualiza) {
                    String remi = getIntent().getStringExtra("cedulaI");
                    actualiza(remi);
                }

            }
        }
    }
    private void mostrarConfirmacionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Estás seguro de salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        salir(null);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }



}