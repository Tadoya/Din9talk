package io.github.tadoya.din9talk;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    private FirebaseAuth mAuth;

    static String Token="";
    String userUid;
    static boolean isStaySignIn=true;
    static Button startButton;
    static Button signInButton;

    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference("users");
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startButton = (Button) findViewById(R.id.startButton);
        signInButton = (Button) findViewById(R.id.sign_in_button);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null) {
            signInButton.setText("Sign Out");
            userUid = mAuth.getCurrentUser().getUid();
            userEmail = mAuth.getCurrentUser().getEmail();

            Log.d("signCycle","Start_currentuser's email:" + userEmail);
            Log.d("signCycle","Start_currentuser:"+ userUid);
        }

        if(signInButton.getText().equals("Sign Out")){
            getTokenatStart(userUid, userEmail);
        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signInButton.getText().equals("Sign In")) {
                    Intent signIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signIntent);
                }
                else{
                    reSign();
                }
            }
        });


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chattingIntent = new Intent(getApplicationContext(), ChattingActivity.class);
                chattingIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(chattingIntent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isStaySignIn){
            Log.d("signCycle","destroy:"+isStaySignIn);
            FirebaseAuth.getInstance().signOut();
        }
    }


    public void getTokenatStart(String userUid, final String userEmail){
        // Read a message from the database(Once)
        myRef.child(userUid).child("Token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String DBToken = (String) dataSnapshot.getValue();
                Log.d("signCycle","get from database token:"+DBToken);
                if(Token.equals(DBToken)) {
                    startButton.setEnabled(true);
                    Toast.makeText(StartActivity.this, "Hello!"+'\n'+userEmail, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(StartActivity.this, "Already, signing in..\n Please resign in!", Toast.LENGTH_LONG).show();
                    reSign();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"token reading error");
            }
        });
    }
    public void reSign(){
        startButton.setEnabled(false);
        signInButton.setText("Sign In");
        signInButton.setBackgroundColor(Color.parseColor("#80cbc4"));
        FirebaseAuth.getInstance().signOut();
    }
}
