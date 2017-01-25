package com.example.fuad.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BonusProgram extends AppCompatActivity implements View.OnClickListener  {
    private Button buttoncode;
    private EditText edittextcode;
    RecyclerView recyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabaseReference;

    Context context;

    RecyclerView.Adapter recyclerView_Adapter;

    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private List<String> codes = new ArrayList<>();
    private FirebaseUser user;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_program);
        firebaseAuth=FirebaseAuth.getInstance();

        buttoncode=(Button) findViewById(R.id.buttonCodeEngine);
        edittextcode=(EditText) findViewById(R.id.edittextCode);
        buttoncode.setOnClickListener(this);

        mdatabaseReference= FirebaseDatabase.getInstance().getReference();

        context = getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerViewLayoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        code();


    }
    private void code(){

        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    user=firebaseAuth.getCurrentUser();
                    count = Integer.valueOf(dataSnapshot.child(user.getUid()).getValue().toString());
                    recyclerView_Adapter = new RecyclerViewAdapter(context,count);
                    recyclerView.setAdapter(recyclerView_Adapter);

                    for (int i=0; i<dataSnapshot.child("code").getChildrenCount();i++) {
                        codes.add(String.valueOf(dataSnapshot.child("code").child(String.valueOf(i)).getValue()));
                    }

                }
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void codeengine() {
        String code = edittextcode.getText().toString().trim();
if(Connection()==true){
        for (int i = 0; i < codes.size(); i++) {


            if (code.equals(codes.get(i))) {
                switch (count) {

                    case 9:
                        Toast.makeText(getApplicationContext(), "You get one coffee for free", Toast.LENGTH_LONG).show();
                        break;
                    case 10:

                        count = 0;
                        break;
                }
                count++;
                mdatabaseReference.child(user.getUid()).setValue(count);
                recyclerView.getAdapter().notifyDataSetChanged();
                edittextcode.setText("");
                return;
            }

        else
        Toast.makeText(getApplicationContext(), "You enter wrong code", Toast.LENGTH_LONG).show();
      return;
    }
    }
        else
    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
        return;


    }
    public boolean Connection() {
        ConnectivityManager connect = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connect.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public void onClick(View view) {
        if(view==buttoncode){
            codeengine();
        }


    }

}