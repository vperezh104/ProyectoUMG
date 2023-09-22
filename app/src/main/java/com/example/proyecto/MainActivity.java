package com.example.proyecto;

import static com.example.proyecto.R.id.btnMaps;
import static com.example.proyecto.R.id.map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button  mBtnMaps;

    private TextView mTextViewData,mTextViewData2;
    private String origen2;
    private String origen1;

    private int  MY_PERMISSIONS_REQUEST_READ_CONTACTS ;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference mDatabase;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time time=new time();
        time.execute();


        time2 time2=new time2();
        time2.execute();





        mTextViewData=(TextView) findViewById(R.id.textViewData);
        mTextViewData2=(TextView) findViewById(R.id.textViewData2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mBtnMaps= findViewById(R.id.btnMaps);
        // subirLatLongFirebase();
        //subirLatLongFirebase2();




        //recuperar datos
mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        for(   DataSnapshot snapshot : dataSnapshot.getChildren()){



            mDatabase.child("usuarios").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    origen1 = dataSnapshot.getValue().toString();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


//recuoerar datos 2

        mDatabase.child("usuarios2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(   DataSnapshot snapshot : dataSnapshot.getChildren()){



                    mDatabase.child("usuarios2").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            origen2 = dataSnapshot.getValue().toString();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });












        //llmar a los mapas
       mBtnMaps.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

Intent i = new Intent ( MainActivity.this, MapsActivity.class);
startActivity(i);

    }
});

    }






// Ingreso a Firebase

    private void subirLatLongFirebase() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {


                            public void onSuccess(Location location) {

                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    //Log.e ("Latitude: ", + location.getLatitude()+"Longitude: "+location.getLongitude());

                                    Map<String,Object> latlang = new HashMap<>();
                                    latlang.put("latitude",location.getLatitude());
                                    latlang.put("longitude",location.getLongitude());
                                    mDatabase.child("usuarios").push().setValue(latlang);




                                }
                            }
                        });
    }



    //Segundo ingreso
    private void subirLatLongFirebase2() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this,
                        new OnSuccessListener<Location>() {


                            public void onSuccess(Location location) {

                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    //Log.e ("Latitude: ", + location.getLatitude()+"Longitude: "+location.getLongitude());

                                    Map<String,Object> latlang = new HashMap<>();
                                    latlang.put("latitude",location.getLatitude());
                                    latlang.put("longitude",location.getLongitude());
                                    mDatabase.child("usuarios2").push().setValue(latlang);




                                }
                            }
                        });
    }












    // Timpo para que se reinicie el Ciclo
    public void hilo()  {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }


    // Reset del Tiempo
    public void ejecutar(){
        time time=new time();
        time.execute();
    }


    //ciclo para el Tiempo
    public class time extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i=1; i<=4; i++){
                hilo();
            }

            return true;
        }


        //al finalizar el Tiempo Realice
        @Override
        protected void onPostExecute(Boolean aBoolean) {

            ejecutar();
            subirLatLongFirebase();


if (origen1 == origen2 ){
 Toast.makeText(MainActivity.this,"es igual", Toast.LENGTH_LONG).show();
       }


           mTextViewData.setText("Latitud es:  " + origen1);



        //  Toast.makeText(MainActivity.this,"instruccion 1", Toast.LENGTH_SHORT).show();

        }
    }





    // Timpo para que se reinicie el Ciclo2
    public void hilo2()  {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }




    // Reset del Tiempo
    public void ejecutar2(){
        time2 time2=new time2();
        time2.execute();
    }



    //ciclo para el Tiempo
    public class time2 extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i=1; i<=4; i++){
                hilo2();
            }

            return true;
        }


        //al finalizar el Tiempo Realice
        @Override
        protected void onPostExecute(Boolean aBoolean) {

            ejecutar2();
            subirLatLongFirebase2();




            mTextViewData2.setText("Latitud es:  " + origen2);


            Toast.makeText(MainActivity.this,"esta es la instruccion 2", Toast.LENGTH_SHORT).show();
        }
    }







}