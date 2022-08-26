package com.example.project1_chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1_chatapp.databinding.FragmentUsersBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private static final String TAG = "users fragment";
    UsersFragment.UsersFragmentListener mListener;
    FragmentUsersBinding binding;
    ArrayList<User> users = new ArrayList<>();

    private void setupUI() {
        getUsersData();
    }

    private void getUsersData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        users.clear();
                        Log.d(TAG, "Chatrooms Info: " + value.getMetadata());
                        for (QueryDocumentSnapshot document: value) {
                            User user = new User();
                            user.setId(document.getId());
                            user.setFirstName(document.getString("firstName"));
                            user.setLastName(document.getString("lastName"));
                            user.setEmail(document.getString("email"));
                            users.add(user);
                        }
                        Log.d(TAG, "Users Array Items ---------> " + users);
                    }
                });
    }

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
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
        getActivity().setTitle("Users");
        mListener = (UsersFragment.UsersFragmentListener) context;
    }

    public interface UsersFragmentListener {
        //void goToLogin();
    }
}