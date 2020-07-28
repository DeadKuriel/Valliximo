package com.charlessoftware.valliximo;

import androidx.annotation.NonNull; // en lugar de import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    // Declaración de variables
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        // Obtener referencias de los objetos
        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Obtener la instancia de Firebase
        auth = FirebaseAuth.getInstance();
        // Click para regresar el Login
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valor del editText
                String email = inputEmail.getText().toString().trim();
                // Validar si se ingreso el correo electronico
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Ingrese su ID de correo electrónico registrado", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Mostrar progressBar
                progressBar.setVisibility(View.VISIBLE);
                // Resetear contraseña
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "¡Le hemos enviado instrucciones para restablecer su contraseña!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Error al enviar el correo electrónico de reinicio", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}