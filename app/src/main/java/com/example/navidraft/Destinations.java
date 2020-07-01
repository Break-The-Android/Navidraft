package com.example.navidraft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.navidraft.Model.DataStructure;
import com.example.navidraft.Model.TravelHistoryModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Random;

public class Destinations extends AppCompatActivity {

    static String encodeUserEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    ImageButton Return;
    private RecyclerView recyclerView;
    CardView cardView;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        recyclerView = findViewById(R.id.recycler_Destinations);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();

        Return = findViewById(R.id.DestinationsPreviousInterfaceBTN);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, android.R.anim.fade_out);
            }
        });
        adapter.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout rootview;
        public TextView ID;
        public TextView ADDRESS;
        public TextView DATE;
        public TextView TIME;
        public TextView TRAVELDISTANCE;
        public TextView TRAVELMEDIUM;
        public TextView TRAVELTIME;
        public TextView STARTPOINT;
        public TextView PLACENAME;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootview = itemView.findViewById(R.id.rootview);
            cardView = itemView.findViewById(R.id.ViewCard);
            ID = itemView.findViewById(R.id.IDFIRE);
            ADDRESS = itemView.findViewById(R.id.ADDRESSFIRE);
            DATE = itemView.findViewById(R.id.DATEFIRE);
            TIME = itemView.findViewById(R.id.TIMEFIRE);
            TRAVELDISTANCE = itemView.findViewById(R.id.TRAVELDISTANCEFIRE);
            TRAVELMEDIUM = itemView.findViewById(R.id.TRAVELMEDIUMFIRE);
            TRAVELTIME = itemView.findViewById(R.id.TRAVELTIMEFIRE);
            STARTPOINT = itemView.findViewById(R.id.STARTPOINTFIRE);
            PLACENAME = itemView.findViewById(R.id.PLACENAMEFIRE);
        }

        public void setID(String string) {
            ID.setText(string);
        }

        public void setADDRESS(String string) {
            ADDRESS.setText(string);
        }

        public void setDATE(String string) {
            DATE.setText(string);
        }

        public void setTIME(String string) {
            TIME.setText(string);
        }

        public void setTRAVELDISTANCE(String string) {
            TRAVELDISTANCE.setText(string);
        }

        public void setTRAVELMEDIUM(String string) {
            TRAVELMEDIUM.setText(string);
        }

        public void setTRAVELTIME(String string) {
            TRAVELTIME.setText(string);
        }

        public void setSTARTPOINT(String string) {
            STARTPOINT.setText(string);
        }

        public void setPLACENAME(String string) {
            PLACENAME.setText(string);
        }
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Destinations").child(encodeUserEmail(HomeMap.FinalEmail.toLowerCase()));

        FirebaseRecyclerOptions<TravelHistoryModel> options = new FirebaseRecyclerOptions.Builder<TravelHistoryModel>()
                .setQuery(query, new SnapshotParser<TravelHistoryModel>() {
                    @NonNull
                    @Override
                    public TravelHistoryModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new TravelHistoryModel(snapshot.child("tripID").getValue().toString(),
                                snapshot.child("date").getValue().toString(),
                                snapshot.child("time").getValue().toString(),
                                snapshot.child("address").getValue().toString(),
                                snapshot.child("startPoint").getValue().toString(),
                                snapshot.child("placeName").getValue().toString(),
                                snapshot.child("travelTime").getValue().toString(),
                                snapshot.child("travelDistance").getValue().toString(),
                                snapshot.child("travelMedium").getValue().toString());
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<TravelHistoryModel, Destinations.ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull TravelHistoryModel travelHistoryModel) {
                viewHolder.setID(travelHistoryModel.getTripID());
                viewHolder.setADDRESS(travelHistoryModel.getAddress());
                viewHolder.setDATE(travelHistoryModel.getDate());
                viewHolder.setTIME(travelHistoryModel.getTime());
                viewHolder.setTRAVELDISTANCE(travelHistoryModel.getTravelDistance());
                viewHolder.setTRAVELMEDIUM(travelHistoryModel.getTravelMedium().toUpperCase());
                viewHolder.setTRAVELTIME(travelHistoryModel.getTravelTime());
                viewHolder.setSTARTPOINT(travelHistoryModel.getStartPoint());
                viewHolder.setPLACENAME(travelHistoryModel.getPlaceName());
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.destination_view, parent, false);
                return new ViewHolder(view);
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, android.R.anim.fade_out);
    }
}