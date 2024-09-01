package com.example.myapplication;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

// For Fit API
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import androidx.annotation.Nullable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.tasks.Task;

import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.ZoneId;

import android.util.Log;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.sql.DataSource;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Created App");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // request permission for google fit
        // requestFitnessPermissions();
        signInToGoogleFit();

        // Configure sign-in to request the user's email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();





    }

    // deprecated, using signInToGoogleFit instead
    private void requestFitnessPermissions() {
        Log.d(TAG, "Requested Fitness Permissions");
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_LOCATION_SAMPLE, FitnessOptions.ACCESS_READ)
                .build();
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
        }
    }

    private void signInToGoogleFit() {
        Log.d(TAG, "Requested Fitness Permissions and Signin requested");
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                // .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (account == null || !GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            Log.d(TAG, "Need Sign in, ask user to sign in");
            GoogleSignIn.requestPermissions(
                    this,
                    REQUEST_OAUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    fitnessOptions);
                    subscribeToFitnessData(account);
                    readFitnessData(account);
        } else {
            Log.d(TAG, "Already signed in, read fitness data");
            // Proceed to read data if already signed in

            subscribeToFitnessData(account);
            readFitnessData(account);
        }
    }

    private void readFitnessData(GoogleSignInAccount account) {
        Log.d(TAG, "Started reading fitness data");
        ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());
        ZonedDateTime startTime = endTime.minusWeeks(1);
        Log.i(TAG, String.format("Range Start: %s", startTime));
        Log.i(TAG, String.format("Range End: %s", endTime));

//        DataSource dataSource = new DataSource.Builder()
//                .setAppPackageName("com.google.android.gms")
//                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                .setType(DataSource.TYPE_DERIVED)
//                .setStreamName("estimated_steps")
//                .build();


        // Currently don't have any data, so it won't read anything.
        // TODO: either record health data before read, or uses google account that actually has data in google fit
        DataReadRequest readRequest = new DataReadRequest.Builder()
                // speed and location is not getting read anymore as we are switching to step trackers
                // .read(DataType.TYPE_LOCATION_SAMPLE) // Location
                // .read(DataType.TYPE_SPEED) // Speed
                // .read(DataType.TYPE_DISTANCE_DELTA) // distance travelled  for the last week
                .read(DataType.TYPE_STEP_COUNT_DELTA) // Num of steps taken for the last week
//                .aggregate(dataSource)
//                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build();

//        Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
//                .readData(request)
//                .addOnSuccessListener(response -> {
//                    int totalSteps = response.getBuckets().stream()
//                            .flatMap(bucket -> bucket.getDataSets().stream())
//                            .flatMap(dataSet -> dataSet.getDataPoints().stream())
//                            .mapToInt(dataPoint -> dataPoint.getValue(Field.FIELD_STEPS).asInt())
//                            .sum();
//                    Log.i(TAG, String.format("Total steps: %d", totalSteps));
//                });



        Task<DataReadResponse> response = Fitness.getHistoryClient(this, account)
                .readData(readRequest);

        response.addOnSuccessListener(dataReadResponse -> {
            for (DataSet dataSet : dataReadResponse.getDataSets()) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                    for (Field field : dp.getDataType().getFields()) {
                        Log.d("GoogleFit", "Field: " + field.getName() + " Value: " + dp.getValue(field));
                    }
                }
            }
        });
    }
    private void subscribeToFitnessData(GoogleSignInAccount account) {
       // GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);
        Log.d(TAG, "Subscribe started");

        Fitness.getRecordingClient(this, account)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(unused ->
                        Log.i(TAG, "Successfully subscribed!"))
                .addOnFailureListener(e ->
                        Log.w(TAG, "There was a problem subscribing.", e));
    }




    }



