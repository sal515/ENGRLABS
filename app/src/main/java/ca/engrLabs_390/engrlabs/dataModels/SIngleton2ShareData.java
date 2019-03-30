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

    private static final String TAG = "SingleTon";


    private static HashMap LabDataMap;
    private static List<String> keysList;
    private static List<LabDataModel> tempLabDynamicDataObjects;


    // references to the database
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRootRef;
    private static DatabaseReference dynamicDataRef;
    private static DatabaseReference softwaresRef;
    private static DatabaseReference currentSemesterCoursesRef;
    private static DatabaseReference currentSemesterLabsRef;
    // valueEvent listener
    private static ValueEventListener labDetailsListenerVar;



    public static void downloadDynamicDataForRecyclerStartUp() {
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

                        // Variable declarations for the RecyclerVeiw Rows
                        int floor;
                        int Room;
                        String RoomCode;
                        String Temperature;
                        String NumberOfStudentsPresent;
                        String TotalCapacity;
                        String AvailableSpots;
                        String LabAvailable;
                        String BuildingCode;
                        String LocationCode;
                        HashMap<String, String> upcomingClass = new HashMap<String, String>();

                        for (int j = 0; j < keysList.size(); j++) {
                            // tempDynamicDataList = new ArrayList<LabDataModel>();
                            LabDataModel tempDynamicDataObj = new LabDataModel();

                            // AvailableSpots
                            AvailableSpots =  ((HashMap) LabDataMap.get(keysList.get(j))).get("AvailableSpots").toString();
                            BuildingCode = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("BuildingCode");
                            LabAvailable = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("LabAvailable");
                            LocationCode = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("LocationCode");
                            try {
                                Room = Integer.parseInt((String) ((HashMap) LabDataMap.get(keysList.get(j))).get("Room"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.w(TAG, "Error Loading Room from Database");

                                Room = 000;

                            }

                            try {
                                RoomCode = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("RoomCode");
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                Log.w(TAG, "Error Loading Room Code from Database");
                                RoomCode = "000";
                            }
                            // setting the temperature
                            Temperature = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("Temperature");
                            TotalCapacity = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("TotalCapacity");
                            // setting the number of students
                            NumberOfStudentsPresent = (String) ((HashMap) LabDataMap.get(keysList.get(j))).get("NumberOfStudentsPresent");


                            tempDynamicDataObj.setAvailableSpots(AvailableSpots);
                            tempDynamicDataObj.setBuildingCode(BuildingCode);
                            tempDynamicDataObj.setLabAvailability(LabAvailable);
                            tempDynamicDataObj.setLocationCode(LocationCode);
                            tempDynamicDataObj.setRoom(Room);
                            tempDynamicDataObj.setRoomCode(RoomCode);
                            tempDynamicDataObj.setTemperature(Temperature);
                            tempDynamicDataObj.setTotalCapacity(TotalCapacity);

                            // setting the Room number
//                    tempDynamicDataObj.setRoomStr(keysList.get(j));
                            // NumberOfStudentsPresent = (String) ((HashMap) LabDataMap.get("B204")).get("NumberOfStudentsPresent");
                            tempDynamicDataObj.setNumberOfStudentsPresent(NumberOfStudentsPresent);

                            // Adding the tempDynamicData object created to the List
                            tempLabDynamicDataObjects.add(tempDynamicDataObj);
                        }

                        if (!keysList.isEmpty()) {
                            keysList.clear();
                        }

                        Log.w(TAG, "ThreadWorking");

                        String i = "Work>";
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        // Toast.LENGTH_SHORT).show();
                        // [END_EXCLUDE]
                    }
                };
                dynamicDataRef.addListenerForSingleValueEvent(labDetailsListener);
//                labDetailsListenerVar = labDetailsListener;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI stuff can be done during the computation thread is running

                    }
                });
            }
        }).start();

//        if (labDetailsListenerVar != null) {
//            databaseRootRef.removeEventListener(labDetailsListenerVar);
//        }
    }


    public static void extractParsedSoftwareData() {

        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {



                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // UI stuff can be done at the end of the actual computation in the thread
                    }
                });
            }
        }).start();



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
