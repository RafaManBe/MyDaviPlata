package com.example.mydaviplata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EnviarActivity extends AppCompatActivity {

    private EditText CedulaRecibe, PlataRecibe, CedulaRemitente;
    private BaseDatos bdatos;
    private Button btnEnviar;
    private TextView money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);


        CedulaRecibe = findViewById(R.id.cedularecibe);
        PlataRecibe = findViewById(R.id.plataenviada);
        //CedulaRemitente = findViewById(R.id.cedularemitente);
        bdatos = new BaseDatos(this);
        btnEnviar = findViewById(R.id.remitente);
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarConfirmacionDialog();
            }
        });
    }

    public void enviar(View view) {
        String cedulaRecibe = CedulaRecibe.getText().toString();
        String cedulaEnvia = getIntent().getStringExtra("remi");
        String plataR = PlataRecibe.getText().toString();


        if (plataR.isEmpty()||cedulaRecibe.isEmpty()) {
            CedulaRecibe.setError("No se permiten campos vacios");
            PlataRecibe.setError("No se permiten campos vacios");
            return;
        }/*else if (cedulaRecibe.isEmpty()) {
            CedulaRecibe.setError("No se permiten campos vacios");
            return;
        }*/
        double cantidad = Double.parseDouble(plataR);
        double saldoRemitente = bdatos.consultarSaldo(cedulaEnvia);
        double saldoDestinatario = bdatos.consultarSaldo(cedulaRecibe);

        if (cantidad < 1000) {
            PlataRecibe.setError("El saldo minimo permitido es $1000");
            return;

        }else if (saldoRemitente < cantidad) {
            PlataRecibe.setError("Saldo insuficiente");
            return;
        }else if (!bdatos.usuarioRegistrado(cedulaRecibe)) {
            CedulaRecibe.setError("El usuario destinatario no está registrado");
            return;
        }

        double nuevoSaldoRemitente = saldoRemitente - cantidad;
        double nuevoSaldoDestinatario = saldoDestinatario + cantidad;

        bdatos.actualizarSaldo(cedulaEnvia, nuevoSaldoRemitente);
        bdatos.actualizarSaldo(cedulaRecibe, nuevoSaldoDestinatario);

        Toast.makeText(this, "Transferencia realizada con éxito", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        intent.putExtra("actualiza", true);
        setResult(RESULT_OK, intent);
        finish();

        SQLiteDatabase db = bdatos.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String fecha = dateFormat.format(calendar.getTime());

        ContentValues values = new ContentValues();
        values.put("cedula_emisor", cedulaEnvia);
        values.put("cedula_receptor", cedulaRecibe);
        values.put("monto", plataR);
        values.put("fecha", fecha);

        db.insert("transacciones", null, values);
        db.close();

    }

    private void mostrarConfirmacionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmación")
                .setMessage("¿Estás seguro de enviar los datos?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enviar(null);
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
