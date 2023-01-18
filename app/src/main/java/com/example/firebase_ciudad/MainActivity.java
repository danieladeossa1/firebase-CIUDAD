package com.example.firebase_ciudad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText jetcodigo,jetnombre,jetciudad,jetcantidad;
    CheckBox jcbactivo;
    String codigo,nombre,ciudad,cantidad,codigoid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        jetcantidad=findViewById(R.id.etcantidad);
        jetciudad=findViewById(R.id.etciudad);
        jetcodigo=findViewById(R.id.etcodigo);
        jetnombre=findViewById(R.id.etnombre);
        jcbactivo=findViewById(R.id.cbactivo);

    }
    public void Adicionar(View view) {
        nombre = jetnombre.getText().toString();
        codigo = jetcodigo.getText().toString();
        ciudad = jetciudad.getText().toString();
        cantidad = jetcantidad.getText().toString();
        if (nombre.isEmpty() || codigo.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        } else {
            // Create a new city with a first and last name
            Map<String, Object> dato = new HashMap<>();
            dato.put("Codigo", codigo);
            dato.put("Nombre", nombre);
            dato.put("Ciudad", ciudad);
            dato.put("Cantidad", cantidad);
            dato.put("Activo", "Si");

            // Add a new document with a generated ID
            db.collection("Ciudad")
                    .add(dato)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(MainActivity.this, "Documento guardado", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                            Toast.makeText(MainActivity.this, "Error guardando documento", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void Consultar(View view){
        codigo=jetcodigo.getText().toString();
        if(codigo.isEmpty()){
            Toast.makeText(this, "Codigo requerido para consultar", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        }else{
            db.collection("Ciudad")
                    .whereEqualTo("Codigo",codigo)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    codigoid=document.getId();
                                    jetnombre.setText(document.getString("Nombre"));
                                    jetcantidad.setText(document.getString("Cantidad"));
                                    jetciudad.setText((document.getString("Ciudad")));
                                    if(document.getString("Activo").equals("Si"))
                                        jcbactivo.setChecked(true);
                                }
                            } else {
                                //Log.w(TAG, "Error getting documents.", task.getException());
                                Toast.makeText(MainActivity.this, "Error en consulta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void Anular(View view){
        nombre = jetnombre.getText().toString();
        codigo = jetcodigo.getText().toString();
        ciudad = jetciudad.getText().toString();
        cantidad = jetcantidad.getText().toString();
        if (nombre.isEmpty() || codigo.isEmpty() || ciudad.isEmpty() || cantidad.isEmpty()) {
            Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            jetcodigo.requestFocus();
        } else {
            // Create a new city with a first and last name
            Map<String, Object> dato = new HashMap<>();
            dato.put("Codigo", codigo);
            dato.put("Nombre", nombre);
            dato.put("Ciudad", ciudad);
            dato.put("Cantidad", cantidad);
            dato.put("Activo", "No");

            // Add a new document with a generated ID
            db.collection("Ciudad").document(codigoid)
                    .set(dato)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,"Estudiante actualizado correctmente...",Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error actualizando estudiante...",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void cancelar (View view){
        Limpiar_campos();
    }
    private void Limpiar_campos(){
        jetcodigo.setText("");
        jetcantidad.setText("");
        jetnombre.setText("");
        jetciudad.setText("");
        jcbactivo.setChecked(false);
        jetcodigo.requestFocus();
    }
}

