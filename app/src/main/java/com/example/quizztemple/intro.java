package com.example.quizztemple;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;

import java.util.ArrayList;

public class intro extends AppCompatActivity {
    FragmentManager fragmentManager;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        b1 = findViewById(R.id.button3);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fragmentManager = getSupportFragmentManager();
        PaperOnboardingFragment paperOnboardingFragment = PaperOnboardingFragment.newInstance(getDataForOnBoarding());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,paperOnboardingFragment);
        fragmentTransaction.commit();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(intro.this,MainActivity.class);
                startActivity(i);
            }
        });




    }

    private ArrayList<PaperOnboardingPage> getDataForOnBoarding() {
        PaperOnboardingPage src1 = new PaperOnboardingPage("Categories","Explore fun and informative quiz categories", Color.parseColor("#FFFFFF"),R.drawable.intro1,R.drawable.ic_baseline_home_24);
        PaperOnboardingPage src2 = new PaperOnboardingPage("Leaderboard","Compete with friends get to the top of leaderboard", Color.parseColor("#FFFFFF"),R.drawable.intro2,R.drawable.ic_baseline_leaderboard_24);
        PaperOnboardingPage src3 = new PaperOnboardingPage("Wallet","Maximize your wallet to earn money", Color.parseColor("#FFFFFF"),R.drawable.intro3,R.drawable.ic_baseline_account_balance_wallet_24);
        PaperOnboardingPage src4 = new PaperOnboardingPage("Profile","Update your profile", Color.parseColor("#FFFFFF"),R.drawable.intro4,R.drawable.ic_baseline_account_circle_24);
        PaperOnboardingPage src5 = new PaperOnboardingPage("Lucky spin","Try your luck and earn points", Color.parseColor("#FFFFFF"),R.drawable.intro5,R.drawable.ic_baseline_account_circle_24);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(src1);
        elements.add(src2);
        elements.add(src3);
        elements.add(src4);
        elements.add(src5);
        return elements;
    }
}