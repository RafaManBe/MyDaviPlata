package com.example.mydaviplata;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.SQLClientInfoException;

public class BaseDatos extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "administracion";
    private static final int VERSION_BD = 1; // Incrementa la versión de la base de datos

    public BaseDatos(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table datos (nombre text, apellido text, cedula text primary key, celular text, correo text, contraseña text, confirmar text, saldo text)");
        db.execSQL("CREATE TABLE transacciones (id INTEGER PRIMARY KEY AUTOINCREMENT, cedula_emisor TEXT, monto REAL, fecha TEXT, cedula_receptor TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* if (oldVersion < 2) {
            // Si la versión anterior es menor a 2, se ejecutan las modificaciones necesarias
            db.execSQL("ALTER TABLE transacciones ADD tipo_envio TEXT");
        }
        if (oldVersion < 3) {
            // Si la versión anterior es menor a 3, se ejecutan las modificaciones necesarias
            db.execSQL("UPDATE transacciones SET tipo_envio = 'enviado' WHERE cedula_emisor IS NOT NULL");
            db.execSQL("UPDATE transacciones SET tipo_envio = 'recibido' WHERE cedula_receptor IS NOT NULL");
        }*/
    }



    public double consultarSaldo(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();
        double saldo = 0;

        Cursor cursor = db.query("datos", new String[]{"saldo"}, "cedula = ?", new String[]{cedula}, null, null, null);
        if (cursor.moveToFirst()) {
            saldo = cursor.getDouble(0);
        }
        cursor.close();

        return saldo;
    }


    public void actualizarSaldo(String cedula, double nuevoSaldo) {
        SQLiteDatabase db = this.getWritableDatabase(); // instancia de lectura

        ContentValues values = new ContentValues();
        values.put("saldo", nuevoSaldo);

        db.update("datos", values, "cedula = ?", new String[]{cedula});

        db.close();
    }
    public boolean usuarioRegistrado(String cedula) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("datos", null, "cedula = ?", new String[]{cedula}, null, null, null); // Consulta a la base de datos
        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }
    public String separarNumeros(String numero, int separacion) {
        StringBuilder rafa = new StringBuilder();

        for (int i = 0; i < numero.length(); i += separacion) {
            int endIndex = Math.min(i + separacion, numero.length());
            String grupo = numero.substring(i, endIndex);
            rafa.append(grupo);
            if (endIndex < numero.length()) {
                rafa.append(" ");
            }
        }
        return rafa.toString();
    }

}
