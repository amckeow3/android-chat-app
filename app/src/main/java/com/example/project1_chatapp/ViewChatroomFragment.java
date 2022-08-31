package com.example.project1_chatapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project1_chatapp.databinding.FragmentChatroomBinding;
import com.example.project1_chatapp.databinding.FragmentViewChatroomBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewChatroomFragment extends Fragment {
    private static final String TAG = "view chatroom frag";
    ViewChatroomFragment.ViewChatroomFragmentListener mListener;
    FragmentViewChatroomBinding binding;
    private FirebaseAuth mAuth;

    private static final String ARG_PARAM_CHATROOM = "param1";

    Chatroom chatroomObject;
    String chatroomName;
    String chatroomId;
    Button leaveButton;
    TextView chatroomTitle, numViewers;
    EditText messageTextbox;
    int viewerCount;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ViewChatroomRecyclerViewAdapter adapter;
    ArrayList<Message> messageList;

    private void getMessages() {
        String topic = chatroomName;

        HashMap<String, Object> newMessage = new HashMap<>();
        /*
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setTopic(topic)
                .build();
         */
};

// Send a message to devices subscribed to the provided topic.


    private void setupUI() {
        Log.d(TAG, "onCreateView: " + chatroomName + chatroomId);
        getActivity().setTitle(chatroomName);
    }

    public ViewChatroomFragment() {
        // Required empty public constructor
    }

    public static ViewChatroomFragment newInstance(Chatroom chatroom) {
        ViewChatroomFragment fragment = new ViewChatroomFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CHATROOM, chatroom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chatroomObject = (Chatroom) getArguments().getSerializable(ARG_PARAM_CHATROOM);
            chatroomId = chatroomObject.getId();
            chatroomName = chatroomObject.getName();
        }
        getMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewChatroomBinding.inflate(inflater, container, false);
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
        mListener = (ViewChatroomFragment.ViewChatroomFragmentListener) context;
    }

    class ViewChatroomRecyclerViewAdapter extends RecyclerView.Adapter<ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder> {

        @NonNull
        @Override
        public ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewChatroomViewHolder extends RecyclerView.ViewHolder {

            public ViewChatroomViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

    public interface ViewChatroomFragmentListener {

    }
}