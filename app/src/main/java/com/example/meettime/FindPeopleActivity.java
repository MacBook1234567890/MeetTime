package com.example.meettime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class FindPeopleActivity extends AppCompatActivity {

    private RecyclerView findFriendList;
    private EditText searchET;
    private String str="";
    private DatabaseReference  usersRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");

        searchET=findViewById(R.id.search_user_text);
        findFriendList=findViewById(R.id.find_friends_list);
        findFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSeq, int start, int before, int count) {


                if (searchET.getText().toString().equals(""))
                {
                    Toast.makeText(FindPeopleActivity.this, "Please write name to search", Toast.LENGTH_SHORT).show();

                }
                else
                {
                        str=charSeq.toString();
                        onStart();
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

        protected void onStart()
        {
            super.onStart();

            FirebaseRecyclerOptions<Contacts> options=null;
            if (str.equals(""))
            {
                options=
                        new FirebaseRecyclerOptions.Builder<Contacts>()
                                .setQuery(usersRef,Contacts.class)
                                .build();
            }
            else {
                options=
                        new FirebaseRecyclerOptions.Builder<Contacts>()
                                .setQuery(usersRef.
                                        orderByChild("name")
                                                .startAt(str)
                                                .endAt(str+"\uf8ff")
                                        ,Contacts.class)
                                .build();
            }




            FirebaseRecyclerAdapter<Contacts,FindFriendsViewHolder> firebaseRecyclerAdapter
                    =new FirebaseRecyclerAdapter<Contacts,FindFriendsViewHolder>(options){
                @Override
                protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Contacts model) {
                    holder.userNameTxt.setText(model.getName());
                    Picasso.get().load(model.getImage()).into(holder.profileImageView);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int adapterPosition = holder.getAdapterPosition();//this all new
                            if (adapterPosition != RecyclerView.NO_POSITION) {  //this all new

                                String visit_user_id = getRef(adapterPosition).getKey();//change position to adapterPosition
                                Intent intent = new Intent(FindPeopleActivity.this, ProfileActivity.class);
                                intent.putExtra("visit_user_id", visit_user_id);
                                intent.putExtra("profile_image", model.getImage());
                                intent.putExtra("profile_name", model.getName());
                                startActivity(intent);
                            }//this all new

                        }
                    });
                }

                @NonNull
                @Override
                public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup p, int viewType){
                    View view= LayoutInflater.from(p.getContext()).inflate(R.layout.contact_design,p,false);
                    FindFriendsViewHolder viewHolder=new FindFriendsViewHolder(view);
                    return viewHolder;
                }

            };

            findFriendList.setAdapter(firebaseRecyclerAdapter);
            firebaseRecyclerAdapter.startListening();


        }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {

        TextView userNameTxt;
        Button videoCallBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);


            userNameTxt=itemView.findViewById(R.id.name_contact);
            videoCallBtn=itemView.findViewById(R.id.call_btn);
            profileImageView=itemView.findViewById(R.id.image_contact);
            cardView=itemView.findViewById(R.id.card_view1);


            videoCallBtn.setVisibility(View.GONE);
        }
    }
}