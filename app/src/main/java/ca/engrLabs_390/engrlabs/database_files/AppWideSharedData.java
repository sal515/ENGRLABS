package ca.engrLabs_390.engrlabs.database_files;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AppWideSharedData extends Application {
    private HashMap LabDataMap;
    private static final String TAG = "SingleTonAppWide";

    private FirebaseDatabase database;
    private DatabaseReference databaseRootRef;
    private DatabaseReference databaseDynamicDataRef;
    private ValueEventListener labDetailsListenerVar;


    @Override
    public void onCreate() {
        super.onCreate();
        downloadLabSnapshotAtStartUp();
    }

    void downloadLabSnapshotAtStartUp() {
        databaseRootRef = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        databaseDynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");
        LabDataMap = new HashMap();


        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                ValueEventListener labDetailsListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI

                        // extract the snapshot as an object
                        Object labObj = dataSnapshot.getValue();


                        // Check if the object is of type HashMap, if it is cast it to HashMap
                        if (labObj instanceof HashMap) {
                            LabDataMap = new HashMap((HashMap) labObj);
                        }

                        Log.w(TAG, "ThreadWorking");

                        String i = "Work>";
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
//                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                databaseDynamicDataRef.addValueEventListener(labDetailsListener);
                labDetailsListenerVar = labDetailsListener;


//                final DiffUtil.DiffResult diffResult =
//                        DiffUtil.calculateDiff(new LabDataModelDiffCallback(oldLabModelList, newLabModelList));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        applyDiffResult(newLabModelList, diffResult);
                    }
                });
            }
        }).start();


        if (labDetailsListenerVar != null) {
            databaseRootRef.removeEventListener(labDetailsListenerVar);
        }

    }


    public HashMap getLabDataMap() {
        return LabDataMap;
    }

    public void setLabDataMap(HashMap labDataMap) {
        LabDataMap = labDataMap;
    }
}
