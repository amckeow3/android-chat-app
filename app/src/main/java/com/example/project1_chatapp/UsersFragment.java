package com.example.project1_chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project1_chatapp.databinding.FragmentUsersBinding;
import com.example.project1_chatapp.databinding.UserLineItemBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class UsersFragment extends Fragment {
    private static final String TAG = "users fragment";
    UsersFragment.UsersFragmentListener mListener;
    FragmentUsersBinding binding;
    ArrayList<User> users = new ArrayList<>();
    UsersListAdapter usersListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

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
                            user.setCity(document.getString("city"));
                            user.setGender(document.getString("gender"));
                            users.add(user);
                        }
                        Log.d(TAG, "Users Array Items ---------> " + users);
                        /*requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.recyclerViewUsers;
                                recyclerView.setHasFixedSize(false);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                usersListAdapter = new UsersListAdapter(users);
                                recyclerView.setAdapter(usersListAdapter);
                            }
                        });*/
                        usersListAdapter.notifyDataSetChanged();
                    }
                });

    }

    class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UsersViewHolder> {
        ArrayList<User> mUsers;

        public UsersListAdapter(ArrayList<User> data) {
            this.mUsers = data;
        }

        @NonNull
        @Override
        public UsersListAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            UserLineItemBinding binding = UserLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new UsersListAdapter.UsersViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull UsersListAdapter.UsersViewHolder holder, int position) {
            User user = mUsers.get(position);
            holder.setupUI(user);
        }

        @Override
        public int getItemCount() {
            return this.mUsers.size();
        }

        public class UsersViewHolder extends RecyclerView.ViewHolder {
            UserLineItemBinding mBinding;
            User mUser;
            int position;

            public UsersViewHolder(@NonNull UserLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(User user) {
                mUser = user;
                String firstName = mUser.getFirstName();
                String lastName = mUser.getLastName();
                mBinding.textViewUserFullName.setText(firstName + " " + lastName);
                mBinding.textViewUserCity.setText(mUser.getCity());
                mBinding.textViewUserGender.setText(mUser.getGender());
            }
        }
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

        recyclerView = binding.recyclerViewUsers;
        recyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        usersListAdapter = new UsersListAdapter(users);
        recyclerView.setAdapter(usersListAdapter);
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