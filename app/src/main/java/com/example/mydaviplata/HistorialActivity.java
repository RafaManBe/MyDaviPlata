package com.example.mydaviplata;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private ListView historialListView;
    private BaseDatos baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        baseDatos = new BaseDatos(this);
        historialListView = findViewById(R.id.historial1);
        mostrarTransacciones();
    }

    private void mostrarTransacciones() {
        String celdu2 = getIntent().getStringExtra("wposs");
        SQLiteDatabase db = baseDatos.getReadableDatabase();
        String consulta = "SELECT * FROM transacciones WHERE cedula_emisor = ? OR cedula_receptor = ?" + " ORDER BY fecha DESC ";
        Cursor cursor = db.rawQuery(consulta, new String[]{celdu2, celdu2});
        List<String> transacciones = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                String cedulaEmisor = cursor.getString(cursor.getColumnIndexOrThrow("cedula_emisor"));
                String cedulaReceptor = cursor.getString(cursor.getColumnIndexOrThrow("cedula_receptor"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                double monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto"));

                String tipoTransaccion;
                if (cedulaEmisor.equals(celdu2)) {
                    tipoTransaccion = "Enviado";
                } else if (cedulaReceptor.equals(celdu2)) {
                    tipoTransaccion = "Recibido";
                } else {
                    continue;
                }

                String transaccion = fecha + " - " + tipoTransaccion + " - " + monto;
                transacciones.add(transaccion);
            } while (cursor.moveToNext());
        }

        if (transacciones.isEmpty()) {
            Toast.makeText(this, "No se encontraron transacciones", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transacciones);
            historialListView.setAdapter(adapter);
        }

        // Cierra el cursor y libera los recursos
        cursor.close();
    }



}