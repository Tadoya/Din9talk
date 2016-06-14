package io.github.tadoya.din9talk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.quickstart.auth.ChooserActivity;


public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";
    public static String Token="";
    private boolean isToken=false;
    Button getTokenButton;
    Button signInButton;
    Button signOutButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getTokenButton = (Button) findViewById(R.id.getTokenButton);
        signInButton = (Button) findViewById(R.id.sign_in_button);
        signOutButton = (Button) findViewById(R.id.sign_out_button);

        // Read a message from the database(Once)
        myRef.child("tadoya").child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tadoyaToken = (String) dataSnapshot.getValue();
                Log.d(TAG,tadoyaToken);
                Token = tadoyaToken;
                isToken = true;
                getTokenButton.setText("START");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"token reading error");
                isToken = false;
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIntent = new Intent(getApplicationContext(), ChooserActivity.class);
                startActivity(signIntent);
            }
        });


        getTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Token.isEmpty() && !isToken)  {
                    Token = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "InstanceID token: " + Token);
                    Toast.makeText(StartActivity.this, "Token = " +Token, Toast.LENGTH_SHORT).show();
                    getTokenButton.setText("START");
                    isToken = true;

                    // Write a message to the database
                    myRef.child("tadoya").child("token").setValue(Token);
                }
                else {
                    Intent chattingIntent = new Intent(getApplicationContext(), ChattingActivity.class);
                    chattingIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(chattingIntent);
                    finish();
                }
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
    }
}
