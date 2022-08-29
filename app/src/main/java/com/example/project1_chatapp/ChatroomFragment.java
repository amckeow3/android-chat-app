package com.example.project1_chatapp;

import android.annotation.SuppressLint;
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

import com.example.project1_chatapp.databinding.ChatroomLineItemBinding;
import com.example.project1_chatapp.databinding.FragmentChatroomBinding;
import com.example.project1_chatapp.databinding.UserLineItemBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatroomFragment extends Fragment {

    private static final String TAG = "hello fragment";
    ChatroomFragment.ChatroomFragmentListener mListener;
    FragmentChatroomBinding binding;
    private FirebaseAuth mAuth;
    private TabLayout tabLayout;
    ArrayList<Chatroom> chatrooms = new ArrayList<>();
    ChatroomsListAdapter chatroomsListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

   private void setupUI() {
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().setTitle("Chatrooms");
        getUserAccountInfo();

        binding.tabLayoutChatroomOptions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: " + tab);
                int mSelectedPosition = 0;
                mSelectedPosition = tab.getPosition();
                Log.d(TAG, "onTabSelected: Position ==>" + mSelectedPosition);
                if (mSelectedPosition == 0) {
                    Log.d(TAG, "onTabSelected: My Chatrooms Selected");
                } else if (mSelectedPosition == 1) {
                    getChatroomsData();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    
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
                        String name = value.getString("firstName") + " " + value.getString("lastName");
                        binding.textViewHelloUserName.setText(name);
                    }
                });
    }

    private void getChatroomsData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("chatrooms")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        chatrooms.clear();
                        Log.d(TAG, "Chatrooms Info: " + value.getMetadata());
                        for (QueryDocumentSnapshot document: value) {
                            Chatroom chatroom = new Chatroom();
                            chatroom.setId(document.getId());
                            chatroom.setName(document.getString("name"));
                            chatrooms.add(chatroom);
                        }
                        Log.d(TAG, "Chatrooms Array Items ---------> " + chatrooms);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.chatroomsRecyclerView;
                                recyclerView.setHasFixedSize(false);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                chatroomsListAdapter = new ChatroomsListAdapter(chatrooms);
                                recyclerView.setAdapter(chatroomsListAdapter);
                            }
                        });
                    }
                });
    }

    class ChatroomsListAdapter extends RecyclerView.Adapter<ChatroomsListAdapter.ChatroomsViewHolder> {
        ArrayList<Chatroom> mChatrooms;

        public ChatroomsListAdapter(ArrayList<Chatroom> data) {
            this.mChatrooms = data;
        }

        @NonNull
        @Override
        public ChatroomsListAdapter.ChatroomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ChatroomLineItemBinding binding = ChatroomLineItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ChatroomsListAdapter.ChatroomsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatroomsListAdapter.ChatroomsViewHolder holder, int position) {
            Chatroom chatroom = mChatrooms.get(position);
            holder.setupUI(chatroom);
        }

        @Override
        public int getItemCount() {
            return this.mChatrooms.size();
        }

        public class ChatroomsViewHolder extends RecyclerView.ViewHolder {
            ChatroomLineItemBinding mBinding;
            Chatroom mChatroom;
            int position;

            public ChatroomsViewHolder(@NonNull ChatroomLineItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Chatroom chatroom) {
                mChatroom = chatroom;
                mBinding.textViewChatroomName.setText(mChatroom.getName());
            }
        }
    }

    public ChatroomFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatroomBinding.inflate(inflater, container, false);
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

        mListener = (ChatroomFragment.ChatroomFragmentListener) context;
    }

    public interface ChatroomFragmentListener {
        void goToLogin();
    }
}