package com.example.project1_chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.project1_chatapp.databinding.FragmentAccountBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

public class AccountFragment extends Fragment {
    private static final String TAG = "account fragment";
    AccountFragment.AccountFragmentListener mListener;
    FragmentAccountBinding binding;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;

    private void setupUI() {
        getUserAccountInfo();
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == Activity.RESULT_OK) {
                if(result.getData() != null) {
                    Intent data = result.getData();
                    Uri selectedImage = data.getData();
                    updateProfilePicture(selectedImage);
                }
            }
        }
    });

    private void getUserAccountInfo() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userUid = user.getUid();

        db.collection("users")
                .document(userUid)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "user data ---->" + value.getData());

                        String firstName = value.getString("firstName");
                        String lastName = value.getString("lastName");
                        String city = value.getString("city");
                        String gender = value.getString("gender");

                        binding.editTextAcctFirstName.setText(firstName);
                        binding.editTextAcctLastName.setText(lastName);
                        binding.editTextAcctCity.setText(city);
                        if (gender.matches("female")) {
                            binding.radioButtonFemale.setChecked(true);
                        } else if (gender.matches("male")) {
                            binding.radioButtonMale.setChecked(true);
                        }


                    }
                });


    }

    private void updateUserProfile() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();
        String firstName = binding.editTextAcctFirstName.getText().toString();
        String lastName = binding.editTextAcctLastName.getText().toString();
        String city = binding.editTextAcctCity.getText().toString();
        int checkedId = binding.radioGroupAcctGender.getCheckedRadioButtonId();
        String gender = "female";
        if (checkedId == R.id.radioButtonFemale) {
            gender = "female";
        } else if (checkedId == R.id.radioButtonMale) {
            gender = "male";
        }

        HashMap<String, Object> updatedUser = new HashMap<>();

        updatedUser.put("firstName", firstName);
        updatedUser.put("lastName", lastName);
        updatedUser.put("gender", gender);
        updatedUser.put("city", city);

        db.collection("users")
                .document(id)
                .set(updatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "User account was successfully updated!");
                        Log.d(TAG, "onSuccess: " + updatedUser);
                        mListener.goToChatrooms();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error updating user account" + e);
                    }
                });

    }

    public void updateProfilePicture(Uri imageURI) {
        binding.imageViewAcctProfilePic.setImageURI(imageURI);

        //storage/emulated/0/Download/yourImage.jpg
        FirebaseUser user = mAuth.getCurrentUser();
        StorageReference storageReference = storage.getReference();
        StorageReference profileImgRef = storageReference.child("images/" + user.getUid());
        UploadTask uploadTask = profileImgRef.putFile(imageURI);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        //setupUI();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");
        storage = FirebaseStorage.getInstance();

        getUserAccountInfo();

        binding.buttonChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*//storage/emulated/0/Download/yourImage.jpg
                FirebaseUser user = mAuth.getCurrentUser();
                StorageReference storageReference = storage.getReference();
                StorageReference profileImgRef = storageReference.child("images/" + user.getUid());*/

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //Intent intent = new Intent(Intent.ACTION_PICK);
                //intent.setType("image/*");
                startForResult.launch(intent);
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserProfile();
            }
        });
    }

    // onActivityResult() handles callbacks from the photo picker.
    @Override
    public void onActivityResult(
            int requestCode, int resultCode, final Intent data) {

        if (resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            binding.imageViewAcctProfilePic.setImageURI(selectedImage);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (AccountFragment.AccountFragmentListener) context;
    }

    public interface AccountFragmentListener {
        void goToChatrooms();
    }
}