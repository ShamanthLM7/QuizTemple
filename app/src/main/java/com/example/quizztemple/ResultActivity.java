package com.example.quizztemple;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.example.quizztemple.databinding.ActivityResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        long points = correctAnswers * POINTS;

        binding.score.setText(String.format("%d/%d", correctAnswers, totalQuestions));
        binding.earnedCoins.setText(String.valueOf(points));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(points));

        binding.restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Intent.ACTION_SEND);
                i1.setType("text/plain");
                String data = "Hey I am using QuizTemple"+"\n"+"Check out my score"+"\n"+correctAnswers+"/"+totalQuestions+"\n"+"Earned points"+"\n"+points+"\n"+"Download this app to beat my score"+"\n"+"play.google.com";
                //String sub = "Check out my score";
                i1.putExtra(Intent.EXTRA_TEXT,data);
                //i1.putExtra(Intent.EXTRA_TEXT,sub);
                //i1.putExtra(Intent.EXTRA_TEXT,correctAnswers);
                //i1.putExtra(Intent.EXTRA_TEXT,points);

                startActivity(Intent.createChooser(i1,"Share using"));

            }
        });


    }
    @Override
    public void onBackPressed(){

    }
}
