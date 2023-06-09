package com.example.mydaviplata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private EditText Nombre, Apellido,Celular, Cedula, Correo,Contraseña,Confirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Nombre = findViewById(R.id.etNombre);
        Apellido = findViewById(R.id.etApellido);
        Cedula = findViewById(R.id.etCedula);
        Celular = findViewById(R.id.etCelular);
        Correo = findViewById(R.id.etCorreo);
        Contraseña = findViewById(R.id.etContraseña);
        Confirmar = findViewById(R.id.etConfirmar);

    }

    public void registrar (View view){

        String nombre = Nombre.getText().toString();
        String apellido = Apellido.getText().toString();
        String cedula = Cedula.getText().toString();
        String celular = Celular.getText().toString();
        String correo = Correo.getText().toString();
        String contraseña = Contraseña.getText().toString();
        String confirmar = Confirmar.getText().toString();

        if (nombre.isEmpty()||apellido.isEmpty()||cedula.isEmpty()||celular.isEmpty()||correo.isEmpty()||contraseña.isEmpty()||confirmar.isEmpty()){
            //Toast.makeText(this, "Por favor completa los campos vacios", Toast.LENGTH_LONG).show();
            Nombre.setError("Por favor completa los campos vacios");
            Apellido.setError("Por favor completa los campos vacios");
            Cedula.setError("Por favor completa los campos vacios");
            Celular.setError("Por favor completa los campos vacios");
            Correo.setError("Por favor completa los campos vacios");
            Contraseña.setError("Por favor completa los campos vacios");
            Confirmar.setError("Por favor completa los campos vacios");
        }
        else if(!isValidEmail(correo)){
            //Toast.makeText(this, "Correo invalido, por favor verificalo", Toast.LENGTH_LONG).show();
            Correo.setError("Correo invalido, por favor verificalo");
        }
        else if (!contraseña.equals(confirmar)) {
            Toast.makeText(this, "La contraseña y la confirmación no coinciden", Toast.LENGTH_LONG).show();
        } else if (contraseña.length() != 4 || confirmar.length() !=4 ) {
            Toast.makeText(this, "La contraseña debe tener exactamente cuatro caracteres", Toast.LENGTH_LONG).show();
        }else if (celular.length() !=10 ) {
            //Toast.makeText(this, "El numero telefonico debe tener 10 digitos", Toast.LENGTH_LONG).show();
            Celular.setError("El numero telefonico debe tener 10 digitos");
        } else if (!validar_registro(cedula, celular)) {



            Toast.makeText(this,"La cedula o el telefono ya se encuentra registrado", Toast.LENGTH_LONG).show();
            /*Cedula.setError("La cedula ya se encuentra registrado");
            Celular.setError("El celular ya se encuentra registrado");*/
        } else{

            Intent registrar = new Intent(this, EleccionActivity.class);
            startActivity(registrar);

            BaseDatos admin = new BaseDatos(this);
            SQLiteDatabase bd = admin.getWritableDatabase();

            ContentValues registro = new ContentValues();
            registro.put("nombre", nombre);
            registro.put("apellido", apellido);
            registro.put("cedula", cedula);
            registro.put("celular",celular);
            registro.put("correo", correo);
            registro.put("contraseña", contraseña);
            registro.put("confirmar", confirmar);
            registro.put("saldo", 1000000);

            bd.insert("datos", null, registro);
            bd.close();

            Nombre.setText("");
            Apellido.setText("");
            Cedula.setText("");
            Celular.setText("");
            Correo.setText("");
            Contraseña.setText("");
            Confirmar.setText("");
            Toast.makeText(this,"Datos guardados exitosamente", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9_.]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();

    }
    public boolean validar_registro(String cedula, String celular) {
        BaseDatos admin = new BaseDatos(this);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String consulta = "SELECT * FROM datos WHERE cedula = ? OR celular = ?";
        Cursor cursor = bd.rawQuery(consulta, new String[]{cedula, celular});

        boolean existe = cursor.moveToFirst();
        cursor.close();
        bd.close();

        return !existe;
    }
}