package ca.engrLabs_390.engrlabs.dataModels;

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
    private static List<String> keysList;
    private static List<LabDataModel> tempLabDynamicDataObjects;

    private static final String TAGg = "SingleTonAppWide";

    // references to the database
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRootRef;
    private static DatabaseReference dynamicDataRef;
    private static DatabaseReference softwaresRef;
    private static DatabaseReference currentSemesterCoursesRef;
    private static DatabaseReference currentSemesterLabsRef;
    // valueEvent listener
    private static ValueEventListener labDetailsListenerVar;

//    public static void

    public static void downloadLabSnapshotAtStartUp() {
        // databaseRootRef = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        dynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");


        LabDataMap = new HashMap();
        tempLabDynamicDataObjects = new ArrayList<LabDataModel>();
        keysList = new ArrayList<String>();

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
                            keysList = new ArrayList<String>(LabDataMap.keySet());

                        }

                        String databaseNumberofStudents;
                        for (int j = 0; j < keysList.size(); j++) {
                            LabDataModel tempLabObj = new LabDataModel();
                            tempLabObj.setRoomCode(keysList.get(j));
                            databaseNumberofStudents = (String) ((HashMap) LabDataMap.get("B204")).get("NumberOfStudentsPresent");
                            tempLabObj.setNumberOfStudentsPresent(databaseNumberofStudents);
                            tempLabDynamicDataObjects.add(tempLabObj);
                        }

                        if (!keysList.isEmpty()) {
                            keysList.clear();
                        }

                        Log.w(TAGg, "ThreadWorking");

                        String i = "Work>";
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAGg, "loadPost:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        // Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                dynamicDataRef.addValueEventListener(labDetailsListener);
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

    public static List<LabDataModel> getTempLabDynamicDataObjects() {
        return tempLabDynamicDataObjects;
    }

    public static void setTempLabDynamicDataObjects(List<LabDataModel> tempLabDynamicDataObjects) {
        SIngleton2ShareData.tempLabDynamicDataObjects = tempLabDynamicDataObjects;
    }
}
