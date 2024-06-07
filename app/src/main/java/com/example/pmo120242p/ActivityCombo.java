package com.example.pmo120242p;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import Configuracion.Personas;
import Configuracion.SQLiteConexion;
import Configuracion.Trans;

public class ActivityCombo extends AppCompatActivity {
    SQLiteConexion conexion;
    Spinner combopersonas;
    EditText nombres, apellidos, correo;
    ArrayList<Personas> lista;
    ArrayList<String> Arreglo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo);

        conexion = new SQLiteConexion(this, Trans.DBname, null, Trans.Version);
        combopersonas = (Spinner) findViewById(R.id.spinner);
        nombres = (EditText) findViewById(R.id.cbnombre);
        apellidos = (EditText) findViewById(R.id.cbapellido);
        correo = (EditText) findViewById(R.id.cbcorreo);

        ObtenerInfo();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arreglo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combopersonas.setAdapter(adapter);

        combopersonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la persona seleccionada
                Personas personaSeleccionada = lista.get(position);

                // Llenar los EditText con los datos de la persona seleccionada
                nombres.setText(personaSeleccionada.getNombres());
                apellidos.setText(personaSeleccionada.getApellidos());
                correo.setText(personaSeleccionada.getCorreo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });
    }

    private void ObtenerInfo() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        lista = new ArrayList<>();
        Arreglo = new ArrayList<>();

        Cursor cursor = db.rawQuery(Trans.SelectAllPerson, null);

        while (cursor.moveToNext()) {
            Personas persona = new Personas();
            persona.setId(cursor.getInt(0));
            persona.setNombres(cursor.getString(1));
            persona.setApellidos(cursor.getString(2));
            persona.setEdad(cursor.getInt(3));
            persona.setCorreo(cursor.getString(4));

            lista.add(persona);
        }

        cursor.close();
        db.close();
        FillData();
    }

    private void FillData() {
        Arreglo = new ArrayList<>();
        for (Personas persona : lista) {
            Arreglo.add(persona.getId() + " " + persona.getNombres() + " " + persona.getApellidos());
        }
    }
}