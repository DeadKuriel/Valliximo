package com.charlessoftware.valliximo;

import androidx.annotation.NonNull; // en lugar de import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    // Declarar variables
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Obtener instancia de autenticación de Firebase
        auth = FirebaseAuth.getInstance();
        // Obtener referencias de objetos
        btnSignIn = (Button) findViewById(R.id.sign_in_botton);
        btnSignUp = (Button) findViewById(R.id.sign_up_botton);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        // Click para ir a la clase de resetear contraseña
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });
        // Click para ir a la clase de login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Click para registrarse
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los editText
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                // Validar si se ingreso el correo electronico
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "¡Introducir la direccion de correo electrónico!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validar si se ingreso la contraseña
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "¡Introducir la contraseña!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validar longitud de contraseña
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Contraseña demasiado corta, ingrese un minimo de 6 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                // Crear el usuario
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // Mostrar mensaje si la Authenticacion falla
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Autenticación fallida" + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}