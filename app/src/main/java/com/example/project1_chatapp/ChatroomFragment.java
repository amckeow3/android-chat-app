package com.example.project1_chatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.project1_chatapp.databinding.FragmentChatroomBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ChatroomFragment extends Fragment {

    private static final String TAG = "hello fragment";
    ChatroomFragment.ChatroomFragmentListener mListener;
    FragmentChatroomBinding binding;
    private FirebaseAuth mAuth;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    AdapterView adapterView;

    public void setupUI() {
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().setTitle("Chatrooms");
        getUserAccountInfo();

        viewPager = binding.viewPager;

    }

    public static class MyAdapter extends FragmentStateAdapter {
        private static int NUM_ITEMS = 2;

        public MyAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_ITEMS;
        }

    }

    void getUserAccountInfo() {
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