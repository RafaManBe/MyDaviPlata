package com.example.mydaviplata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    private EditText Cedula, Contraseña;
    private BaseDatos Tdatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Cedula = findViewById(R.id.etCedulaLogin);
        Contraseña = findViewById(R.id.etClave);
        Tdatos = new BaseDatos(this);
    }
    public void cambiar(View view){
        Cedula.setText("");
        Contraseña.setText("");
    }

    public void ingresar(View view) {
        String cedulaI = Cedula.getText().toString();
        String contraseñaI = Contraseña.getText().toString();

        if ( contraseñaI.isEmpty()) {
            Contraseña.setError("Por favor completa los campos vacíos");
        }
        if (cedulaI.isEmpty()) {
            Cedula.setError("Por favor completa los campos vacíos");
        } else if (validateLogin(cedulaI, contraseñaI)) {
            // Las credenciales son válidas, puedes continuar con el flujo del programa
            SQLiteDatabase enviar = Tdatos.getReadableDatabase();
            String rafa = "SELECT saldo, nombre FROM datos WHERE cedula = ?";
            Cursor manbe = enviar.rawQuery(rafa, new String[]{cedulaI});
            ArrayList<String> made = new ArrayList<>();
            ArrayList<String> kike = new ArrayList<>();
            String name = "";
            while (manbe.moveToNext()) {
                String saldo = manbe.getString(manbe.getColumnIndexOrThrow("saldo"));
                made.add(saldo);
                String nombre = manbe.getString(manbe.getColumnIndexOrThrow("nombre"));
                kike.add("Hola " + nombre);
                name = nombre;


            }
            manbe.close();
            enviar.close();


            Intent cuerpo = new Intent(this, CuerpoActivity.class);
            cuerpo.putStringArrayListExtra("made", made);
            cuerpo.putStringArrayListExtra("kike", kike);
            cuerpo.putExtra("cedulaI", cedulaI);
            cuerpo.putExtra("name", name);
            startActivity(cuerpo);
            Contraseña.setText("");
        } else {

            Toast.makeText(this, "Cédula o contraseña incorrecta", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateLogin(String cedulaI, String contraseñaI) {
        SQLiteDatabase db = Tdatos.getReadableDatabase();
        String[] projection = {"cedula", "contraseña"};
        String selection = "cedula = ? AND contraseña = ?";
        String[] selectionArgs = {cedulaI, contraseñaI};

        Cursor cursor = db.query(
                "datos",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isValidLogin = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isValidLogin;
    }
}