package com.example.project1_chatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1_chatapp.databinding.FragmentChatroomBinding;
import com.example.project1_chatapp.databinding.FragmentCreateChatroomBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreateChatroomFragment extends Fragment {
    private static final String TAG = "create chat fragment";
    FragmentCreateChatroomBinding binding;
    CreateChatroomFragmentListener mListener;
    private FirebaseAuth mAuth;

    private void createChatroom() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        HashMap<String, Object> chatroom = new HashMap<>();
        String chatroomName = binding.editTextChatroomName.getText().toString();
        chatroom.put("name", chatroomName);

        db.collection("chatrooms")
                .whereEqualTo("name", chatroomName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().isEmpty()) {
                            Log.d(TAG, "onFailure: snap null");
                            Log.d(TAG, "onFailure: No Chatroom with this name was found");
                            db.collection("chatrooms")
                                    .add(chatroom)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "onSuccess: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Error adding new chatroom" + e);
                                        }
                                    });

                            db.collection("users")
                                    .document(userId)
                                    .collection("chatrooms")
                                    .add(chatroom)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "onSuccess: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Error adding new chatroom to user data" + e);
                                        }
                                    });

                            mListener.goToChatrooms();
                        } else {
                            Log.d(TAG, "onComplete snap not null: Chatroom will this name already exists");
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Create Chatroom Error")
                                    .setMessage("A chatroom will this name already exists!")
                                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d(TAG, "onClick: Ok clicked");
                                        }
                                    });
                            builder.create().show();
                        }
                    }
                });
    }

    private void setupUI() {
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChatroom();
            }
        });
    }

    public CreateChatroomFragment() {
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
        binding = FragmentCreateChatroomBinding.inflate(inflater, container, false);
        setupUI();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mListener = (CreateChatroomFragment.CreateChatroomFragmentListener) context;
    }

    public interface CreateChatroomFragmentListener{
        void goToChatrooms();
    }
}