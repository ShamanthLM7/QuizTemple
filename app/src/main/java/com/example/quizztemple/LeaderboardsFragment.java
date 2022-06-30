package com.example.quizztemple;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizztemple.databinding.FragmentLeaderboardsBinding;
//import com.example.quiztemple.databinding.FragmentLeaderboardsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class LeaderboardsFragment extends Fragment {



    public LeaderboardsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentLeaderboardsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        final ArrayList<com.example.quizztemple.User> users = new ArrayList<>();
        final com.example.quizztemple.LeaderboardsAdapter adapter = new com.example.quizztemple.LeaderboardsAdapter(getContext(), users);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.collection("users")
                .orderBy("coins", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    com.example.quizztemple.User user = snapshot.toObject(com.example.quizztemple.User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        });


        return binding.getRoot();
    }
}