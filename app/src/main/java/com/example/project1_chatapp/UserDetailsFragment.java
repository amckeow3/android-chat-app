package com.example.project1_chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UserDetailsFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseFirestore database;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "ARG_USER_ID";

    // TODO: Rename and change types of parameters
    private String userID;

    public UserDetailsFragment() {
        // Required empty public constructor
    }


    public static UserDetailsFragment newInstance(String user) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    Button backButton;
    TextView userFirstName, userLastName, userCity, userGender;
    ImageView profilePicImageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.user_details_txt);

        userFirstName = view.findViewById(R.id.textViewUserDetailsFirstName);
        userLastName = view.findViewById(R.id.textViewUserDetailsLastName);
        userCity = view.findViewById(R.id.textViewUserDetailsCity);
        userGender = view.findViewById(R.id.textViewUserDetailsGender);
        profilePicImageView = view.findViewById(R.id.imageViewAcctProfilePic);

        backButton = view.findViewById(R.id.buttonUserDetailsBack);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(userID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String firstName = value.getString("firstName");
                        String lastName = value.getString("lastName");
                        String city = value.getString("city");
                        String gender = value.getString("gender");

                        userFirstName.setText(firstName);
                        userLastName.setText(lastName);
                        userCity.setText(city);
                        userGender.setText(gender);
                    }
                });

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference profilePic = storage.getReference().child("images/").child(userID);
        if(profilePic != null){
            profilePic.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Glide.with(getActivity())
                                .load(task.getResult())
                                .into(profilePicImageView);
                    }
                }
            });
        } else {
            profilePicImageView.setImageResource(R.drawable.ic_person);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToUserList();
            }
        });
    }

    UserDetailsFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (UserDetailsFragmentListener) context;
    }

    interface UserDetailsFragmentListener {
        void backToUserList();
    }
}