package com.example.project1_chatapp;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1_chatapp.databinding.FragmentChatroomBinding;
import com.example.project1_chatapp.databinding.FragmentViewChatroomBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ViewChatroomFragment extends Fragment {
    private static final String TAG = "view chatroom frag";
    ViewChatroomFragment.ViewChatroomFragmentListener mListener;
    FragmentViewChatroomBinding binding;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    private static final String ARG_PARAM_CHATROOM = "param1";

    Chatroom chatroomObject;
    String chatroomName;
    String chatroomId;
    Button leaveButton;
    FloatingActionButton sendButton;
    TextView chatroomTitle, numViewers;
    EditText messageTextbox;
    int viewerCount;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ViewChatroomRecyclerViewAdapter adapter;
    ArrayList<Message> messageList;

    private void setupUI() {
        getActivity().setTitle(chatroomName);

        binding.buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendMessage();
                //binding.editTextMessage.setText("");
                Log.d("qq", "send button pressed");
            }
        });
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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        messageList = new ArrayList<>();
        recyclerView = binding.messagesRecyclerView;
        recyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ViewChatroomRecyclerViewAdapter(messageList);
        recyclerView.setAdapter(adapter);
        getMessages();

        sendButton = view.findViewById(R.id.buttonSendMessage);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTextEntered = binding.editTextMessage.getText().toString();

                if(messageTextEntered.isEmpty()){
                    //alertdialog
                    AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                    b.setTitle("Error")
                            .setMessage("Please enter a valid message!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    b.create().show();
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();

                    DocumentReference docRef = db.collection("chatrooms")
                            .document(chatroomId)
                            .collection("messages")
                                    .document();

                    HashMap<String, Object> data = new HashMap<>();

                    data.put("id", docRef.getId());
                    data.put("message", messageTextEntered);
                    data.put("dateCreated", FieldValue.serverTimestamp());
                    data.put("creator", user.getDisplayName());
                    data.put("creatorID", user.getUid());
                    data.put("likes", 0);

                    docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            } else {

                            }
                        }
                    });


                }
            }
        });
    }

    void getMessages() {
        db.collection("chatrooms").document(chatroomId)
                .collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        messageList.clear();

                        for(QueryDocumentSnapshot doc : value){
                            Message m = doc.toObject(Message.class);
                            messageList.add(m);
                        }
                        //Log.d("qq", "messageList is: " + messageList.size());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ViewChatroomFragment.ViewChatroomFragmentListener) context;
    }

    class ViewChatroomRecyclerViewAdapter extends RecyclerView.Adapter<ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder> {
        ArrayList<Message> messageArrayList;

        public ViewChatroomRecyclerViewAdapter(ArrayList<Message> messages) {
            this.messageArrayList = messages;
        }

        @NonNull
        @Override
        public ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_line_item, parent, false);
            ViewChatroomViewHolder viewChatroomViewHolder = new ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder(view);

            return viewChatroomViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewChatroomRecyclerViewAdapter.ViewChatroomViewHolder holder, int position) {
            if(messageArrayList.size() != 0) {
                Message message = messageArrayList.get(position);
                holder.messageTextview.setText(message.getMessage());
                holder.posterName.setText(message.getCreator());

                if(message.getDateCreated() != null){
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    String dateStr = sdf.format(message.getDateCreated().toDate());

                    holder.postDate.setText(dateStr);
                } else {
                    holder.postDate.setText("N/A");
                }

                holder.numLikes.setText(Integer.toString(message.getLikes()));

                //adds delete button to user's posted comments only
                /*FirebaseUser user = mAuth.getCurrentUser();
                String id = user.getUid();

                if(message.getCreatorID().equals(id)){
                    holder.deleteButton.setClickable(true);
                    holder.deleteButton.setVisibility(View.VISIBLE);
                } else {
                    holder.deleteButton.setClickable(false);
                    holder.deleteButton.setVisibility(View.INVISIBLE);
                }*/
            }
        }

        @Override
        public int getItemCount() {
            return messageArrayList.size();
        }

        class ViewChatroomViewHolder extends RecyclerView.ViewHolder {
            TextView posterName, postDate, numLikes, messageTextview;
            ImageView deleteButton, likeButton;

            public ViewChatroomViewHolder(@NonNull View itemView) {
                super(itemView);
                posterName = itemView.findViewById(R.id.textViewUserName);
                postDate = itemView.findViewById(R.id.textViewPostDate);
                numLikes = itemView.findViewById(R.id.textViewChatNumLikes);
                messageTextview = itemView.findViewById(R.id.textViewChatMessage);
                deleteButton = itemView.findViewById(R.id.imageViewDeleteButton);
                likeButton = itemView.findViewById(R.id.imageViewLikeButton);

                
            }
        }
    }

    public interface ViewChatroomFragmentListener {

    }
}