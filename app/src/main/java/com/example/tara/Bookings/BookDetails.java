package com.example.tara.Bookings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.tara.Explore.CarDetails;
import com.example.tara.Main.MessageDialog;
import com.example.tara.Main.PaymentActivity;
import com.example.tara.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookDetails extends AppCompatActivity {
    String carId, carHostId,price, carHostName,passBmy,passLocation,passImageUrl, userId;
    DatabaseReference vehicleRef, userHostRef,userRef;
    ImageSlider imageSlider;
    TextView tvBmy, tvLocation, tvPriceRate, tvTransmission, tvDrivetrain, tvSeats,
            tvType, tvFuelType, tvMileage, tvDescription, hostName,tvPriceRate2;
    ImageView hostPic;
    DataSnapshot dataSnapshot;
    FirebaseAuth mAuth;
    Boolean isVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        getSupportActionBar().hide();

        String databaseLocation = getString(R.string.databasePath);
        Toolbar toolbar = findViewById(R.id.appBar);
        ArrayList<SlideModel> slideModels = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        imageSlider = findViewById(R.id.sliderBook);

        carId = getIntent().getStringExtra("carId");
        carHostId = getIntent().getStringExtra("carHostId");

        tvBmy = findViewById(R.id.tvcdBmyBook);
        tvLocation = findViewById(R.id.tvcdLocationBook);
        tvPriceRate = findViewById(R.id.tvPriceBook);
        tvTransmission = findViewById(R.id.tvTransmissionBook);
        tvDrivetrain = findViewById(R.id.tvDrivetrainBook);
        tvSeats = findViewById(R.id.tvSeatsBook);
        tvType = findViewById(R.id.tvTypeBook);
        tvFuelType = findViewById(R.id.tvFuelTypeBook);
        tvMileage = findViewById(R.id.tvcdMileageBook);
        tvDescription = findViewById(R.id.tvDescriptionBook);
        hostName = findViewById(R.id.tvHostBook);
        hostPic = findViewById(R.id.ivHostBook);
        tvPriceRate2 = findViewById(R.id.tvcdPricingBook);

        vehicleRef = FirebaseDatabase.getInstance(databaseLocation).getReference("vehicle").child(carId).child(carHostId);
        userHostRef = FirebaseDatabase.getInstance(databaseLocation).getReference("users").child(carHostId);
        userRef = FirebaseDatabase.getInstance(databaseLocation).getReference("users").child(userId);


        vehicleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSnapshot = snapshot;
                price = snapshot.child("priceRate").getValue().toString();
                passBmy = snapshot.child("bmy").getValue().toString();
                passLocation = snapshot.child("location").getValue().toString();
                passImageUrl = snapshot.child("exterior1Url").getValue().toString();
                tvBmy.setText(snapshot.child("bmy").getValue().toString());
                tvLocation.setText(snapshot.child("location").getValue().toString());
                tvPriceRate.setText(snapshot.child("priceRate").getValue().toString());
                tvTransmission.setText(snapshot.child("transmission").getValue().toString());
                tvDrivetrain.setText(snapshot.child("drivetrain").getValue().toString());
                tvSeats.setText(snapshot.child("seats").getValue().toString());
                tvType.setText(snapshot.child("type").getValue().toString());
                tvFuelType.setText(snapshot.child("fuelType").getValue().toString());
                tvMileage.setText(snapshot.child("mileage").getValue().toString());
                tvDescription.setText(snapshot.child("description").getValue().toString());
                tvPriceRate2.setText(snapshot.child("priceRate").getValue().toString());

                String exterior1Url = snapshot.child("exterior1Url").getValue().toString();
                slideModels.add(new SlideModel(exterior1Url,null));

                if(snapshot.child("exterior2Url").exists()){
                    String exterior2Url = snapshot.child("exterior2Url").getValue().toString();
                    slideModels.add(new SlideModel(exterior2Url,null));
                }
                if(snapshot.child("exterior3Url").exists()){
                    String exterior3Url = snapshot.child("exterior3Url").getValue().toString();
                    slideModels.add(new SlideModel(exterior3Url,null));
                }
                if(snapshot.child("exterior4Url").exists()){
                    String exterior4Url = snapshot.child("exterior4Url").getValue().toString();
                    slideModels.add(new SlideModel(exterior4Url,null));
                }

                String interior1Url = snapshot.child("interior1Url").getValue().toString();
                slideModels.add(new SlideModel(interior1Url,null));

                if(snapshot.child("interior2Url").exists()){
                    String interior2Url = snapshot.child("interior2Url").getValue().toString();
                    slideModels.add(new SlideModel(interior2Url,null));
                }
                if(snapshot.child("interior3Url").exists()){
                    String interior3Url = snapshot.child("interior3Url").getValue().toString();
                    slideModels.add(new SlideModel(interior3Url,null));
                }
                if(snapshot.child("interior4Url").exists()){
                    String interior4Url = snapshot.child("interior4Url").getValue().toString();
                    slideModels.add(new SlideModel(interior4Url,null));
                }

                imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userHostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("imageUrl").getValue().toString().isEmpty())){
                    String imageUrl = snapshot.child("imageUrl").getValue().toString();
                    Glide.with(getApplicationContext()).load(imageUrl).into(hostPic);
                }
                carHostName = snapshot.child("name").getValue().toString();
                hostName.setText(capitalizeWord(carHostName));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public String capitalizeWord(String str){
        String[] words =str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
}