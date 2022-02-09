package com.ella.playgrounds;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends BaseActivity {

    public static final String PARK_PID = "PARK_PID";
    private String park_pid = "";
    private DatabaseReference myRef;
    private User user;
    private Park currentPark;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();
        getCurrentUser();
        getParkByPid(park_pid);
    }

    private void manageMessage() {
        myRef = database.getReference("Massages").child(park_pid);
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message massage = snapshot.getValue(Message.class);
                assert massage != null;
                massage.setKey(snapshot.getKey());
                messageList.add(massage);
                displayMassages(messageList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message massage = snapshot.getValue(Message.class);
                assert massage != null;
                massage.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<>();
                for (Message m : messageList) {
                    if (m.getKey().equals(massage.getKey()))
                        newMessages.add(massage);
                    else
                        newMessages.add(m);
                }
                messageList = newMessages;
                displayMassages(messageList);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message massage = snapshot.getValue(Message.class);
                assert massage != null;
                massage.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<>();
                for (Message m : messageList) {
                    if (!m.getKey().equals(massage.getKey()))
                        newMessages.add(m);
                }
                messageList = newMessages;
                displayMassages(messageList);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getCurrentUser() {
        //get current user from firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        user.setUid(currentUser.getUid());
        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                baseUser = user;
                readMessageFromInput();
                manageMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void readMessageFromInput() {
        ImageButton send_BTN = findViewById(R.id.send_BTN);
        TextInputLayout chat_edit_massage = findViewById(R.id.chat_edit_massage);
        send_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(Objects.requireNonNull(chat_edit_massage.getEditText()).getText().toString())) {
                    Message massage = new Message(chat_edit_massage.getEditText().getText().toString(), user);
                    chat_edit_massage.getEditText().setText("");
                    myRef.push().setValue(massage);
                } else
                    Toast.makeText(ChatActivity.this, "You cannot send empty massage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        park_pid = (String) getIntent().getStringExtra(PARK_PID); //get park pid
        database = FirebaseDatabase.getInstance();
        user = new User();
        messageList = new ArrayList<>();
        isDoublePressToClose = true;
        findViews();
    }

    private void findViews() {
        ImageView chat_background = findViewById(R.id.chat_background);
        Glide
                .with(this)
                .load("https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/kids-swinging-on-swing-set-royalty-free-image-1616099921.?crop=0.668xw:1.00xh;0.167xw,0&resize=640:*")
                .into(chat_background);

        //back button
        ImageButton chat_back_BTN = findViewById(R.id.chat_back_BTN);
        chat_back_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        messageList = new ArrayList<>();
    }

    private void getParkByPid(String pid) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Parks");
        myRef.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPark = dataSnapshot.getValue(Park.class);
                TextView chat_LBL_park_name = findViewById(R.id.chat_LBL_park_name);
                chat_LBL_park_name.setText(currentPark.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void displayMassages(List<Message> massageList) {
        RecyclerView chat_RV = findViewById(R.id.chat_RV);
        chat_RV.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        MassageAdapter massageAdapter = new MassageAdapter(ChatActivity.this, massageList, user.getUid());
        chat_RV.setAdapter(massageAdapter);
        //Recyclerview start from Bottom
        chat_RV.scrollToPosition(massageAdapter.getItemCount() - 1);
    }
}