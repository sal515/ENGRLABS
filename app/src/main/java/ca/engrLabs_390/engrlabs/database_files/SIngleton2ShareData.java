package ca.engrLabs_390.engrlabs.database_files;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SIngleton2ShareData extends Application {
    private static HashMap LabDataMap;
    private static List<String> keys;
    private static List<LabDataModel> tempLabObjects;

    private static final String TAGg = "SingleTonAppWide";

    private static DatabaseReference databaseRootRef;
    private static FirebaseDatabase database;
    private static DatabaseReference databaseDynamicDataRef;
    private static ValueEventListener labDetailsListenerVar;

    public static void downloadLabSnapshotAtStartUp() {
        databaseRootRef = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        databaseDynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");
        LabDataMap = new HashMap();
        tempLabObjects = new ArrayList<LabDataModel>();
        keys = new ArrayList<String>();

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                ValueEventListener labDetailsListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // extract the snapshot as an object
                        Object labObj = dataSnapshot.getValue();

                        // Check if the object is of type HashMap, if it is cast it to HashMap
                        if (labObj instanceof HashMap) {
                            LabDataMap = new HashMap((HashMap) labObj);
                        }

                        String databaseNumberofStudents;
                        for (int j = 0; j < keys.size(); j++) {
                            LabDataModel tempLabObj = new LabDataModel();
                            tempLabObj.setRoomStr(keys.get(j));
                            databaseNumberofStudents = (String) ((HashMap) LabDataMap.get("B204")).get("NumberOfStudentsPresent");
                            tempLabObj.setNumberOfStudentsPresent(databaseNumberofStudents);
                            tempLabObjects.add(tempLabObj);
                        }



                        Log.w(TAGg, "ThreadWorking");

                        String i = "Work>";
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAGg, "loadPost:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
//                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                databaseDynamicDataRef.addValueEventListener(labDetailsListener);
                labDetailsListenerVar = labDetailsListener;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI stuff can be done at the end of the actual computation in the thread
                    }
                });
            }
        }).start();


        if (labDetailsListenerVar != null) {
            databaseRootRef.removeEventListener(labDetailsListenerVar);
        }

    }


    public static HashMap getLabDataMap() {
        return LabDataMap;
    }

    public static void setLabDataMap(HashMap labDataMap) {
        LabDataMap = labDataMap;
    }


    public static List<LabDataModel> getTempLabObjects() {
        return tempLabObjects;
    }

    public static void setTempLabObjects(List<LabDataModel> tempLabObjects) {
        SIngleton2ShareData.tempLabObjects = tempLabObjects;
    }
}
