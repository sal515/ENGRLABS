package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.TA_Section.LoginActivity;
import ca.engrLabs_390.engrlabs.dataModels.LabDataModel;
import ca.engrLabs_390.engrlabs.dataModels.Singleton2ShareData;
import ca.engrLabs_390.engrlabs.recyclerView.recyclerView_lastChangesAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class ExpandableRecycler extends AppCompatActivity {

    private static final String TAG = "RecyclerViewActivity";


    // FIXME: Related to favourite buttom commenting out for now; until I go through the logic
    // favourite checking variable
    //    boolean favouritesOnly = false;

    // Recycler Adapter variable declaration
    recyclerView_lastChangesAdapter recyclerViewAdapter;

    // =========  Search bar stuff   ==========
    //Suggestion list for search bar
    //List<String> suggestList = new ArrayList<>();

    //Search Card - Search Bar for the softwares
    MaterialSearchBar materialSearchBar;
    static String text = "";
    CardView searchCard;
    ImageView sortButton;
    List<String> suggestList = new ArrayList<>();

    // =========  Search bar stuff   ==========

    // =========  Nav Drawer Stuff   ==========
    DrawerLayout drawer;
    NavigationView navigationView;
    MenuItem menu;
    MenuItem homePageNavButton;
    MenuItem labListNavButton;
    MenuItem taLoginNavButton;
    Switch tempUp;
    Switch tempDown;
    Switch peopleUp;
    Switch peopleDown;
    Switch eigthFloor;
    Switch ninthFloor;
    Switch favorites;
    // =========  Nav Drawer Stuff   ==========

    // ========= Firebase variables =================

    FirebaseFirestore db;
    // variables
    HashMap<String, Object> document;
    HashMap labsDynamicDataMap;
    List<String> labKeysList;
    // FIXME: Temp listArray to add IEEE to the recyclerView
    List<LabDataModel> tempDynamicDataList;
    List<LabDataModel> labObjList;
    HashMap<String, Object> labsMap;


    // database references
    private FirebaseDatabase database;
    private DatabaseReference databaseRootRef;
    private DatabaseReference databaseDynamicDataRef;
    private ValueEventListener labDetailsListenerVar;

    // ========= Firebase variables =================


    // DummyClassData
//    List<LabDataModel> dummyClassList = new ArrayList<>();
//    int floorMode;
//    List<LabDataModel> filteredDummyList = new ArrayList<>();
//    String filterSelection;

    // Why and How recyclerView: https://guides.codepath.com/android/using-the-recyclerview
    // Using a RecyclerView has the following key steps:
    // 1. Add RecyclerView support library to the gradle build file
    // 2. Define a model class to use as the data source
    // 3. Add a RecyclerView to your activity to display the items
    // 4. Create a custom row layout XML file to visualize the item
    // 5. Create a RecyclerView.Adapter and ViewHolder to render the item
    // 6. Bind the adapter to the data source to populate the RecyclerView

    // RecyclerView Reference variable
    RecyclerView recyclerViewVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler_with_bottom_nav);
//        setupWindowAnimations();

        // Calling the initial setup functions -> "ORDER of CALL MATTERS"
        initializeAllReferences();

        // FIXME: where should the search bar logic be called ??
        searchBarLogic();

        setListeneres();

//        Creating reference of databaseRootRef - to write to db
//        FirebaseDatabase databaseRootRef = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = databaseRootRef.getReference("/");

        databaseRootRef = FirebaseDatabase.getInstance().getReference();

        // calling the recycler binding function -- !!Should be called only once!!
        bindingAdapterToRecycleViewer();


        List<String> labList = new ArrayList<>(Singleton2ShareData.getLabList("AGI32_18_3_PTBPE_193"));

        int test = 0;
    }

    // window animation between activities
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    private void initializeAllReferences() {
        // initiazing variables for the database
        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        databaseDynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");


        // initializing the arrayList of LabDataModel objects extracted from the databaseRootRef
        labObjList = new ArrayList<LabDataModel>();
        tempDynamicDataList = new ArrayList<LabDataModel>();
        labsDynamicDataMap = new HashMap();


        // Initialize RecyclerView variable
        recyclerViewVar = findViewById(R.id.expandingRecyclerView);

        // Init Nav Drawer
        initNavBar();
        //Dummy Class List
//        floorMode = 0;
//        filterSelection = "";
//        dummyClassList.add(new LabDataModel(8,21,25,10,30,40));
//        dummyClassList.add(new LabDataModel(8,23,22,12,30,60));
//        dummyClassList.add(new LabDataModel(8,47,24,9,30,20));
//        dummyClassList.add(new LabDataModel(9,13,21,5,30,10));
//        dummyClassList.add(new LabDataModel(9,17,28,16,30,30));
//        dummyClassList.add(new LabDataModel(9,21,24,24,30,35));
//        dummyClassList.add(new LabDataModel(10,52,22,18,30,25));
//        dummyClassList.add(new LabDataModel(10,16,26,14,30,45));

        //Initialize Search Card and Suggestion List

        sortButton = findViewById(R.id.sortImage);
        searchCard = findViewById(R.id.searchCard);
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);

    }

    private void initNavBar(){
        drawer = findViewById(R.id.drawerContainer);
        navigationView = findViewById(R.id.nav_view);

        homePageNavButton = findViewById(R.id.homepage);
        labListNavButton = findViewById(R.id.lablist);
        taLoginNavButton = findViewById(R.id.taSection);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.lablist:
                        startActivity(new Intent(getApplicationContext(), ExpandableRecycler.class));
                        break;
                    case R.id.taSection:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        List<Switch> floorSwitches = new ArrayList<>();;
        eigthFloor = navigationView.getMenu().findItem(R.id.eighthFloor).getActionView().findViewById(R.id.switcher);
        ninthFloor = navigationView.getMenu().findItem(R.id.ninthFloor).getActionView().findViewById(R.id.switcher);
        floorSwitches.add(eigthFloor);
        floorSwitches.add(ninthFloor);
        for(int i = 0;i<floorSwitches.size();i++){
            sortInitForSwitchesInAGroup(floorSwitches,i);
        }

        List<Switch> favouriteSwitches = new ArrayList<>();
        favorites = navigationView.getMenu().findItem(R.id.favourites).getActionView().findViewById(R.id.switcher);
        favouriteSwitches.add(favorites);
        for(int i = 0;i<favouriteSwitches.size();i++){
            sortInitForSwitchesInAGroup(favouriteSwitches,i);
        }

        List<Switch> sortSwitches = new ArrayList<>();
        tempUp = navigationView.getMenu().findItem(R.id.tempUp).getActionView().findViewById(R.id.switcher);
        tempDown = navigationView.getMenu().findItem(R.id.tempDown).getActionView().findViewById(R.id.switcher);
        peopleUp = navigationView.getMenu().findItem(R.id.freeUp).getActionView().findViewById(R.id.switcher);
        peopleDown = navigationView.getMenu().findItem(R.id.freeDown).getActionView().findViewById(R.id.switcher);
        sortSwitches.add(tempUp);
        sortSwitches.add(tempDown);
        sortSwitches.add(peopleUp);
        sortSwitches.add(peopleDown);
        for(int i = 0;i<sortSwitches.size();i++){
            sortInitForSwitchesInAGroup(sortSwitches,i);
        }

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                /*
                if (MainActivity.getTutorialMode() == true) {
                    if (tooltipState == 2){
                        nextToolTip();
                    }
                    else if ((tooltipState == 0 )||(tooltipState == 1 )){
                        tool.dismiss();
                    }
                }
                */
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                /*
                if ((tooltipState == 0 )||(tooltipState == 1 )){
                    processTooltips();
                }
                */
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void sortInitForSwitchesInAGroup(final List<Switch> switchList, int switchIndex){
        switchList.get(switchIndex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean turnOnFlag = false;
                if (((Switch)v).isChecked()){
                    turnOnFlag = true;
                }
                for(int i = 0;i<switchList.size();i++){
                    if (switchList.get(i).isChecked()){
                        switchList.get(i).toggle();
                    }
                }
                if (turnOnFlag == true){
                    ((Switch)v).toggle();
                }
                navSwitchPressed((Switch) v,turnOnFlag);
            }
        });
    }

    private void navSwitchPressed(Switch pressedSwitch, boolean switchOn){
        if (pressedSwitch == tempUp){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "TempUp", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == tempDown){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "TempDown", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == peopleUp){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "PeopleUp", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == peopleDown){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "PeopleDown", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == eigthFloor){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "EigthFloor", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == ninthFloor){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "NinthFloor", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == favorites){
            if (switchOn == true) {
                Toast.makeText(getApplicationContext(), "Favourites", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setListeneres() {

        // Setting the bottom nav bar onItemSelection Listener
        sortButton.setOnClickListener(sortButtonListener);
        materialSearchBar.setOnSearchActionListener(materialOnSearchListener);

    }


    @Override
    protected void onStart() {
        super.onStart();
//        floorMode = 0;
//        filterSelection = "";


//        int asdaf = 0;
//        List<LabDataModel> tempTest = new ArrayList(Singleton2ShareData.getLabDynamicDataObjects());
//
//        for (int i = 0; i < tempTest.size(); i++) {
//            Log.w(TAG, String.valueOf(tempTest.get(i)));
//
//        }
//
//        int sdfa = 0;


        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener labDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                Log.w(TAG, "RecyclerViewDBListenerWorking");

                // extract the snapshot as an object
                Object labObj = dataSnapshot.getValue();

                // Check if the object is of type HashMap, if it is cast it to HashMap
                if (labObj instanceof HashMap) {
                    labsDynamicDataMap = new HashMap((HashMap) labObj);
                    labKeysList = new ArrayList<String>(labsDynamicDataMap.keySet());
                }

                // !!Important!! Clearing the temporary list to pass it to the comparision function
                // If the list is not clearing the comparison doesn't work properly!!!
                tempDynamicDataList.clear();

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



                for (int j = 0; j < labKeysList.size(); j++) {

                    // tempDynamicDataList = new ArrayList<LabDataModel>();
                    LabDataModel tempDynamicDataObj = new LabDataModel();

                    // AvailableSpots
                    AvailableSpots =  ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("AvailableSpots").toString();
                    BuildingCode = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("BuildingCode");
                    LabAvailable = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("LabAvailable");
                    LocationCode = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("LocationCode");
                    try {
                        Room = Integer.parseInt((String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("Room"));
                    } catch (Exception e) {
                         e.printStackTrace();
                        Log.w(TAG, "Error Loading Room from Database");

                        Room = 000;

                    }

                    try {
                        RoomCode = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("RoomCode");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.w(TAG, "Error Loading Room Code from Database");
                        RoomCode = "000";
                    }
                    // setting the temperature
                    Temperature = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("Temperature");
                    TotalCapacity = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("TotalCapacity");
                    // setting the number of students
                    NumberOfStudentsPresent = (String) ((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("NumberOfStudentsPresent");


                    tempDynamicDataObj.setAvailableSpots(AvailableSpots);
                    tempDynamicDataObj.setBuildingCode(BuildingCode);
                    tempDynamicDataObj.setLabAvailability(LabAvailable);
                    tempDynamicDataObj.setLocationCode(LocationCode);
                    tempDynamicDataObj.setRoom(Room);
                    tempDynamicDataObj.setRoomCode(RoomCode);
                    tempDynamicDataObj.setTemperature(Temperature);
                    tempDynamicDataObj.setTotalCapacity(TotalCapacity);

                    // setting the Room number
//                    tempDynamicDataObj.setRoomStr(labKeysList.get(j));
                    // NumberOfStudentsPresent = (String) ((HashMap) labsDynamicDataMap.get("B204")).get("NumberOfStudentsPresent");
                    tempDynamicDataObj.setNumberOfStudentsPresent(NumberOfStudentsPresent);

                    // if

                    // Sort the array here: According to user selection
                    // if temperature sort ascending === true
                        // sortAscendingTemp()





                    // Adding the tempDynamicData object created to the List
                    tempDynamicDataList.add(tempDynamicDataObj);
                }

                recyclerViewAdapter.updateLabData(tempDynamicDataList);

                if (!labKeysList.isEmpty()) {
                    labKeysList.clear();
                }

                // [START_EXCLUDE]
                String i = "Work>";
                // [END_EXCLUDE]
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
        // set the listener for the db
        databaseDynamicDataRef.addValueEventListener(labDetailsListener);
        labDetailsListenerVar = labDetailsListener;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (labDetailsListenerVar != null) {
            databaseRootRef.removeEventListener(labDetailsListenerVar);
        }
    }


    private void searchBarLogic() {
        materialSearchBar.setHint("Enter your software or 'all'");
        //searchCard.setVisibility(View.GONE);
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //When user type their text, the suggestion list will update
                updateSuggestList(materialSearchBar.getText());
                materialSearchBar.updateLastSuggestions(suggestList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void updateSuggestList(String inputString){
        suggestList.clear();
        if (inputString.length()<3) {    //if no text was entered, no substring searching needed
            materialSearchBar.clearSuggestions();
            return;
        }
        suggestList.addAll(Singleton2ShareData.getSoftwareList());    //all should print out everything as a suggestion
        if (inputString.toLowerCase().equals("all")){
            return;
        }
        for(int i =0;i<suggestList.size();i++){ //iterate through suggestList
            String temp = suggestList.get(i).toLowerCase();
            if (!temp.contains(inputString.toLowerCase())){
                suggestList.remove(i);   //remove anything that isn't a match
                i--;
            }
        }
        if (suggestList.size() == 0){   //if nothings left that means there were no matches, just output No Matches
            suggestList.add("No Results");
        }
    }

    private MaterialSearchBar.OnSearchActionListener materialOnSearchListener
            = new MaterialSearchBar.OnSearchActionListener() {
        @Override
        public void onSearchStateChanged(boolean enabled) {

            //When search bar is closed
            //Restore original adapter
            if (!enabled) {
                //materialSearchBar.setText("");
                sortButton.setVisibility(View.VISIBLE);
                text = materialSearchBar.getText();
                //materialSearchBar.setText("");
                //filterSelection = "";
                //FIXME: Avoid called this whole recycler initializer function
                //bindingAdapterToRecycleViewer();
            } else {
                sortButton.setVisibility(View.GONE);
                materialSearchBar.setText(text);
                //materialSearchBar.setText("");
            }
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
//            filterSelection = materialSearchBar.getText();

            // This is where you set the state for the recyclerview


            //FIXME: Avoid called this whole recycler initializer function
//                bindingAdapterToRecycleViewer();

        }

        @Override
        public void onButtonClicked(int buttonCode) {
            //***************doesn't work, don't know why
                /*
                if (buttonCode == BUTTON_BACK){
                    materialSearchBar.setText(null);
                    filterSelection = null;
                    //FIXME: Avoid called this whole recycler initializer function
                    bindingAdapterToRecycleViewer();
                }
                */
        }
    };

    private ImageView.OnClickListener sortButtonListener
            = new View.OnClickListener() {
        public void onClick(View v) {
            //LabSortFragment dialog = new LabSortFragment();
            drawer.openDrawer(GravityCompat.START);
            //MenuItem checkable = navigationView.getMenu().findItem(R.id.tempDown);
            //checkable.setChecked(true);
            //dialog.show(getSupportFragmentManager(), "Insert Course");
            // your code here
        }
    };

//    private ImageView.OnClickListener sortButtonListener
//            = new View.OnClickListener() {
//        public void onClick(View v) {
//            LabSortFragment dialog = new LabSortFragment();
//            dialog.show(getSupportFragmentManager(), "Insert Course");
//            // your code here
//        }
//    };

    private void bindingAdapterToRecycleViewer() {

//        filteredDummyList.clear();
//        for(int i = 0;i<dummyClassList.size();i++){
//            if ((dummyClassList.get(i).floor == floorMode) || floorMode == 0)
//            {
//                filteredDummyList.add(dummyClassList.get(i));
//            }
//        }

//        List<LabDataModel> searchFilteredList;
//        searchFilteredList = filterClasses(filteredDummyList);


        // This absolutely works!!
        // Passing the data to the Adapter
//        recyclerViewAdapter = new recyclerView_lastChangesAdapter(tempDynamicDataList);

        try {
//            tempDynamicDataList = new ArrayList<>();

             tempDynamicDataList = new ArrayList<>(Singleton2ShareData.getLabDynamicDataObjects());
            Log.w(TAG, "Data Filled From SingleTon Class");
            int i = 0;
        } catch (Exception e) {
            tempDynamicDataList = new ArrayList<>();
            Log.w(TAG, "Data failed to fill from the SingleTon Class");
            // e.printStackTrace();
        }

        recyclerViewAdapter = new recyclerView_lastChangesAdapter(tempDynamicDataList);

        // set the custom adapter to the recycler view with the data model passed in
        recyclerViewVar.setAdapter(recyclerViewAdapter);

        // set the layout manager position the data according to the xml
        recyclerViewVar.setLayoutManager(new LinearLayoutManager(this));

//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(300);
//        itemAnimator.setRemoveDuration(300);
//        itemAnimator.setAddDuration(300);
//        itemAnimator.setChangeDuration(300);
//        itemAnimator.setMoveDuration(300);
//        recyclerViewVar.setItemAnimator(itemAnimator);

//        RecyclerView.ItemAnimator itemAnimator = new SlideInUpAnimator(new OvershootInterpolator(1f));
//        RecyclerView.ItemAnimator itemAnimator = new FadeInAnimator(new OvershootInterpolator(4f));
//      The animator fixes the program of Flickering screen
        RecyclerView.ItemAnimator itemAnimator = new LandingAnimator(new OvershootInterpolator(1f));
        recyclerViewVar.setItemAnimator(itemAnimator);

    }

    // FIXME: Not sure if we will need it
//    private List<LabDataModel> filterClasses(List<LabDataModel> input){
//
//        List<LabDataModel> returnClassList = new ArrayList<>();
//
//        for(int i=0;i<input.size();i++){
//            if (favouritesOnly == true){
//                if (input.get(i).favourite == true){
//                        returnClassList.add(input.get(i));
//                }
//            }
//            else{
//                returnClassList.add(input.get(i));
//            }
//        }
//        return returnClassList;
//    }

}
