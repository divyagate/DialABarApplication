package com.example.dialabarapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialabarapplication.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class HomeActivity extends AppCompatActivity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};
    private FirebaseFirestore ProductsRef;
    TextView result ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        result =findViewById(R.id.result);
        ProductsRef = FirebaseFirestore.getInstance();
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        getDate();
    }
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void getDate(){

        ProductsRef.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            String resultstr="";
                            for(DocumentSnapshot document:task.getResult()){

                                Products product =document.toObject(Products.class);
                                resultstr +="Product Name: "+ product.getPname()+"\nProduct Description: " +product.getDescription()+
                                        "\nProduct Category: " +product.getCategory()+"\n\n";
                            }
                            result.setText(resultstr);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"loading fail",Toast.LENGTH_SHORT).show();
            }
        });

    }
}