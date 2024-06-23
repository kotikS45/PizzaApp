package com.pizzaapp.bottomNav.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.pizzaapp.LoginActivity;
import com.pizzaapp.databinding.FragmentProfileBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    private Uri filePath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        getUserInfo();

        binding.imageView.setOnClickListener(v -> selectImage());

        binding.logoutCl.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return binding.getRoot();
    }

    private void getUserInfo() {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Object username = snapshot.child("username").getValue();
                        Object email = snapshot.child("email").getValue();
                        Object image = snapshot.child("image").getValue();

                        if (username != null) {
                            binding.usernameTv.setText(username.toString());
                        }
                        if (email != null) {
                            binding.emailTv.setText(email.toString());
                        }
                        if (image != null && !image.toString().isEmpty()) {
                            String imageUrl = image.toString();
                            if (!imageUrl.isEmpty()) {
                                Picasso.get()
                                        .load(imageUrl)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                        .into(binding.imageView);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    ActivityResultLauncher<Intent> selectImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK
                            && result.getData() != null
                            && result.getData().getData() != null) {
                        filePath = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media
                                    .getBitmap(requireContext().getContentResolver(), filePath);

                            binding.imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        uploadImage();
                    }
                }
            }
    );

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        selectImageActivityResultLauncher.launch(intent);
    }

    private void uploadImage() {
        if (filePath != null) {
            String uid  = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            FirebaseStorage.getInstance().getReference().child("images/" + uid)
                    .putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(getContext(), "Photo upload complete", Toast.LENGTH_SHORT).show();

                        FirebaseStorage.getInstance().getReference().child("images/" + uid).getDownloadUrl()
                                .addOnSuccessListener(uri -> FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("image").setValue(uri.toString()));
                    });
        }
    }
}
