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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener instancia de autenticación de Firebase
        auth = FirebaseAuth.getInstance();
        // Si la instancia es distinta a null
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        // Establecer la vista ahora
        setContentView(R.layout.activity_login);
        // Obtener la referencia de los controles
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        // Obtener instancia de autenticación de Firebase
        auth = FirebaseAuth.getInstance();
        // Click del boton registrar en la aplicación
        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
        // Click en el boton resetear la contraseña de usuario
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });
        // Click para acceder
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener valores de los editText
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                // Validar si el login ha sido ingresado
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Introducir la direccion de correo electrónico!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Validar si se ingreso la contraseña
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Introducir la contraseña!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //ProgressBar visible
                progressBar.setVisibility(View.VISIBLE);
                //Autenticar usuario existente
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                // Si el inicio de sesion falla, muestra un mensaje al usuario. Si el inicio de sesion tiene exito
                                // el Auth de estado de autenticación sera notificado y la logica para manejar el
                                // usuario registrado puede ser manejado en el Auth.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // Ocurrio un problema
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimun_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}