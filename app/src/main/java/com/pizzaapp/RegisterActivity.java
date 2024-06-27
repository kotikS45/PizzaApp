package com.pizzaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.pizzaapp.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.signUpBtn.setOnClickListener(this::registerButtonListener);
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void registerButtonListener(View v) {
        if (binding.emailEt.getText().toString().isEmpty() || binding.passwordEt.getText().toString().isEmpty()
                || binding.usernameEt.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fields connote be empty", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.emailEt.getText().toString(), binding.passwordEt.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        HashMap<String, String> userInfo = new HashMap<>();
                        userInfo.put("email", binding.emailEt.getText().toString());
                        userInfo.put("username", binding.usernameEt.getText().toString());
                        userInfo.put("image", "");
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Users")
                                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                .setValue(userInfo);
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}