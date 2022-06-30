package com.example.quizztemple;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.quizztemple.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    CountDownTimer spintimer;
    NotificationManager NmNotificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding  =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // setSupportActionBar(binding.toolbar);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       // transaction.replace(R.id.content,new HomeFragment());
       // transaction.commit();
        HomeFragment homeFragment = new HomeFragment();
        LeaderboardsFragment leaderboardsFragment = new LeaderboardsFragment();
        WalletFragment walletFragment = new WalletFragment();
        ProfileFragment profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();

        binding.spinn.setEnabled(false);
       /* if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("my","my",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }*/

        spintimer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                binding.spintim.setText(String.valueOf(l/1000));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                binding.spinn.setEnabled(true);
               /* if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("my", "my", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }*/
/*
                NotificationChannel channel = new NotificationChannel("my", "my", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
                String msg = "Try your luck on Spin wheel";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(
                        MainActivity.this,"my"
                );
                        builder.setSmallIcon(R.drawable.splash);
                        builder.setContentTitle("QuizTemple");
                        builder.setContentText(msg);
                        builder.setAutoCancel(true);
                        Intent result = new Intent(MainActivity.this,SpinnerActivity.class);
                        result.setAction(Intent.ACTION_MAIN);
                        */
/*result.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*

                        result.addCategory(Intent.CATEGORY_LAUNCHER);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,1,result,0);
                builder.setContentIntent(pendingIntent);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1,builder.build());
*/
                NotificationManager mNotificationManager;

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(MainActivity.this, "notify_001");

                Intent ii = new Intent(MainActivity.this, SpinnerActivity.class);
                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT);


                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText("Try your luck on spin the wheel!");
                bigText.setBigContentTitle("QuizTemple");
                bigText.setSummaryText("Text in detail");

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
                mBuilder.setContentTitle("QuizTemple");
                mBuilder.setContentText("Try your luck on spin the wheel!");
                mBuilder.setPriority(Notification.PRIORITY_MAX);
                mBuilder.setStyle(bigText);
                mBuilder.setAutoCancel(true);

                mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    String channelId = "Your_channel_id";
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    mNotificationManager.createNotificationChannel(channel);
                    mBuilder.setChannelId(channelId);
                }

                mNotificationManager.notify(0, mBuilder.build());
                //spintimer.start();

          /*      int requestID = (int) System.currentTimeMillis();

               // Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NmNotificationManager  = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent = new Intent(getApplicationContext(), ResultActivity.class);

//**add this line**
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

//**edit this line to put requestID as requestCode**
                PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 1,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("My Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("hi"))
                        .setContentText("hello").setAutoCancel(true);

                mBuilder.setContentIntent(contentIntent);
                NmNotificationManager.notify(1, mBuilder.build());*/

            }


        };
        spintimer.start();
        if(spintimer != null){
            binding.spinn.setEnabled(false);
        }

        binding.spinn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SpinnerActivity.class));
                spintimer.start();
                binding.spinn.setEnabled(false);
            }
        });
        binding.abt.setEnabled(true);
        binding.abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AboutActivity.class));
            }
        });

        binding.bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId())
                {
                    case R.id.home:
                      //  transaction.replace(R.id.content,new HomeFragment());
                       // transaction.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, homeFragment).commit();
                        return true;
                       // Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();


                    case R.id.rank:
                        //transaction.replace(R.id.content,new LeaderboardsFragment());
                       // transaction.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, leaderboardsFragment).commit();
                        return true;
                        //Toast.makeText(MainActivity.this, "leader", Toast.LENGTH_SHORT).show();
                        //break;

                    case R.id.wallet:
                       // transaction.replace(R.id.content,new WalletFragment());
                        //transaction.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, walletFragment).commit();
                        return true;
                        //Toast.makeText(MainActivity.this, "wall", Toast.LENGTH_SHORT).show();

                    case R.id.profile:
                       // transaction.replace(R.id.content,new ProfileFragment());
                        //transaction.commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, profileFragment).commit();
                        return true;

                }
                return false;
            }
        });



    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.home_menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.wallet)
//        {
//            Toast.makeText(this, "Wallat clicked", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,SpinnerActivity.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}