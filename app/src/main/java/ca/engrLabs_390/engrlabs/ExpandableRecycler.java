package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.TA_Section.LoginActivity;
import ca.engrLabs_390.engrlabs.dataModels.LabDataModel;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;
import ca.engrLabs_390.engrlabs.recyclerView.recyclerView_lastChangesAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

import static com.mancj.materialsearchbar.MaterialSearchBar.BUTTON_BACK;

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
    static String searchFilterSelection = "";

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
    Switch favoritesSwitch;
    public SharedPreferenceHelper sharedPreferenceHelper;
    /*
    enum SortTypes {
        NONE,
        TEMP_UP,
        TEMP_DOWN,
        PEOPLE_UP,
        PEOPLE_DOWN,
    }
    */
    boolean favouriteFilter = false;
    int floorFilter = 0;
    Settings.SortTypes sortType = Settings.SortTypes.NONE;
    List<LabFavourite> favouriteList = new ArrayList<>();
    public Settings profile;

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

    //Tooltips
    //Handles Tutorial Mode
    public static int tooltipState = 0; //local state machine to control active tooltip
    private Tooltip tool;   //local tooltip

    public static int listToolTipState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler);
//        setupWindowAnimations();

        ProgressBar loading = findViewById(R.id.progressBar);

        // Calling the initial setup functions -> "ORDER of CALL MATTERS"
        initializeAllReferences();

        // FIXME: where should the search bar logic be called ??
        searchBarLogic();

        setListeneres();

//        Creating reference of databaseRootRef - to write to db
//        FirebaseDatabase databaseRootRef = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = databaseRootRef.getReference("/");

        sharedPreferenceHelper = new SharedPreferenceHelper(this);
        profile = sharedPreferenceHelper.getSettings();
        if(profile == null){
            //Toast.makeText(getApplicationContext(), "created" /*String.valueOf(profile.favouriteFilter)*/, Toast.LENGTH_SHORT).show();
            profile = new Settings();
            profile.favouriteFilter = false;
            profile.filterType = 0;
            profile.sortType = Settings.SortTypes.NONE;
            profile.favouriteList = new ArrayList<>();
            //profile.favouriteList.add(new LabFavourite("H841",true));
            //profile.favouriteList.add(new LabFavourite("H821",true));
            sharedPreferenceHelper.saveSettings(profile);
        }
        //goToProfileActivity();
        else{
            Toast.makeText(getApplicationContext(), "Settings Loaded" /*String.valueOf(profile.favouriteFilter)*/, Toast.LENGTH_SHORT).show();
            sortType = profile.sortType;
            floorFilter = profile.filterType;
            favouriteFilter = profile.favouriteFilter;
            favouriteList = profile.favouriteList;

            if (favouriteFilter == true){
                favoritesSwitch.setChecked(true);
            }
            if (sortType != Settings.SortTypes.NONE){
                if (sortType == Settings.SortTypes.PEOPLE_DOWN){
                    if (!peopleDown.isChecked()){
                        peopleDown.toggle();
                    }
                }
                else if (sortType == Settings.SortTypes.PEOPLE_UP){
                    if (!peopleUp.isChecked()) {
                        peopleUp.toggle();
                    }
                }
                else if (sortType == Settings.SortTypes.TEMP_DOWN){
                    if (!tempDown.isChecked()) {
                        tempDown.toggle();
                    }
                }
                else if (sortType == Settings.SortTypes.TEMP_UP){
                    if (!peopleUp.isChecked()) {
                        tempUp.toggle();
                    }
                }
            }
            if (floorFilter != 0){
                if (floorFilter == 8){
                    eigthFloor.setChecked(true);
                }
                else if (floorFilter == 9){
                    ninthFloor.setChecked(true);
                }
            }
        }

        databaseRootRef = FirebaseDatabase.getInstance().getReference();

        //loading.setVisibility(View.INVISIBLE);
        // calling the recycler binding function -- !!Should be called only once!!
        bindingAdapterToRecycleViewer();

        processTooltips();

        //List<String> labList = new ArrayList<>(SIngleton2ShareData.getLabList("AGI32_18_3_PTBPE_193"));

        int test = 0;

    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.lablist).setChecked(true);
        navigationView.getMenu().findItem(R.id.homepage).setChecked(false);
        navigationView.getMenu().findItem(R.id.taSection).setChecked(false);
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
                        navigationView.getMenu().findItem(R.id.lablist).setChecked(false);
                        break;
                    case R.id.lablist:
                        //startActivity(new Intent(getApplicationContext(), ExpandableRecycler.class));
                        break;
                    case R.id.taSection:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        navigationView.getMenu().findItem(R.id.lablist).setChecked(false);
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
        favoritesSwitch = navigationView.getMenu().findItem(R.id.favourites).getActionView().findViewById(R.id.switcher);
        favouriteSwitches.add(favoritesSwitch);
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
                if (MainActivity.getTutorialMode() == true) {
                    if (tool!=null){
                        tool.dismiss();
                    }
                    if (recyclerView_lastChangesAdapter.tool != null){
                        if (recyclerView_lastChangesAdapter.tool.isShowing()){
                            recyclerView_lastChangesAdapter.tool.dismiss();
                        }
                    }
                    /*
                    if (tooltipState == 2){
                        nextToolTip();
                    }
                    else if ((tooltipState == 0 )||(tooltipState == 1 )){
                        tool.dismiss();
                    }
                    */
                }

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if ((tooltipState == 0 )||(tooltipState == 1 )){
                    processTooltips();
                }
                if (listToolTipState == 0 && MainActivity.getTutorialMode() == true){
                    if (recyclerView_lastChangesAdapter.tool!= null){
                        recyclerView_lastChangesAdapter.tool.show();
                    }
                }

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
            sortType = Settings.SortTypes.NONE;
            if (switchOn == true) {
                sortType = Settings.SortTypes.TEMP_UP;
                //Toast.makeText(getApplicationContext(), "TempUp", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == tempDown){
            sortType = Settings.SortTypes.NONE;
            if (switchOn == true) {
                sortType = Settings.SortTypes.TEMP_DOWN;
                //Toast.makeText(getApplicationContext(), "TempDown", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == peopleUp){
            sortType = Settings.SortTypes.NONE;
            if (switchOn == true) {
                sortType = Settings.SortTypes.PEOPLE_UP;
                //Toast.makeText(getApplicationContext(), "PeopleUp", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == peopleDown){
            sortType = Settings.SortTypes.NONE;
            if (switchOn == true) {
                sortType = Settings.SortTypes.PEOPLE_DOWN;
                //Toast.makeText(getApplicationContext(), "PeopleDown", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == eigthFloor){
            floorFilter = 0;
            if (switchOn == true) {
                floorFilter = 8;
                //Toast.makeText(getApplicationContext(), "EigthFloor", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == ninthFloor){
            floorFilter = 0;
            if (switchOn == true) {
                floorFilter = 9;
                //Toast.makeText(getApplicationContext(), "NinthFloor", Toast.LENGTH_SHORT).show();
            }
        }
        else if (pressedSwitch == favoritesSwitch){
            favouriteFilter = false;
            if (switchOn == true) {
                favouriteFilter = true;
                //Toast.makeText(getApplicationContext(), "Favourites", Toast.LENGTH_SHORT).show();
            }
        }
        profile.sortType = sortType;
        profile.filterType = floorFilter;
        profile.favouriteFilter = favouriteFilter;
        sharedPreferenceHelper.saveSettings(profile);
        syncFavouriteList();
        updateData();
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
//        List<LabDataModel> tempTest = new ArrayList(SIngleton2ShareData.getLabDynamicDataObjects());
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
                long upcomingclassTime;



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
                    upcomingclassTime =  (long)((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("UpcomingClassTime");
//                    Log.w(TAG,  (((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("UpcomingClassTime")).getClass().getCanonicalName());
//                    Log.w(TAG,  (((HashMap) labsDynamicDataMap.get(labKeysList.get(j))).get("UpcomingClassTime")).toString());


                    tempDynamicDataObj.setAvailableSpots(AvailableSpots);
                    tempDynamicDataObj.setBuildingCode(BuildingCode);
                    tempDynamicDataObj.setLabAvailability(LabAvailable);
                    tempDynamicDataObj.setLocationCode(LocationCode);
                    tempDynamicDataObj.setRoom(Room);
                    tempDynamicDataObj.setRoomCode(RoomCode);
                    tempDynamicDataObj.setTemperature(Temperature);
                    tempDynamicDataObj.setTotalCapacity(TotalCapacity);
                    tempDynamicDataObj.setUpcomingclassTime(upcomingclassTime);
//                    tempDynamicDataObj.setUpcomingclassTime((long) -1);

                    // setting the Room number
//                    tempDynamicDataObj.setRoomStr(labKeysList.get(j));
                    // NumberOfStudentsPresent = (String) ((HashMap) labsDynamicDataMap.get("B204")).get("NumberOfStudentsPresent");
                    tempDynamicDataObj.setNumberOfStudentsPresent(NumberOfStudentsPresent);


                    // Sort the array here: According to user selection
                    // if temperature sort ascending === true
                        // sortAscendingTemp()


                    // Adding the tempDynamicData object created to the List
                    tempDynamicDataList.add(tempDynamicDataObj);
                }


                    ////****************************YABZ CODE*****************************//
                for(int i = 0;i<tempDynamicDataList.size();i++){
                    tempDynamicDataList.get(i).setFloor((tempDynamicDataList.get(i).getRoomCode()).charAt(1)-48);
                    System.out.println(tempDynamicDataList.get(i).getFloor());
                }

                syncFavouriteList();
                /*
                //Sync with Favourite List
                for(int i = 0;i<tempDynamicDataList.size();i++){
                    for (int j = 0; j < favouriteList.size();j++){
                        if (tempDynamicDataList.get(i).getRoomCode().equals(favouriteList.get(j).labCode)){
                            tempDynamicDataList.get(i).setFavourite(favouriteList.get(j).favourite);
                            break;
                        }
                    }
                }
                */

                    ////****************************YABZ CODE*****************************//w

                updateData();

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
        updateData();
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
        suggestList.addAll(SIngleton2ShareData.getSoftwareList());    //all should print out everything as a suggestion
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
                if (MainActivity.getTutorialMode() == true){
                    if((tooltipState == 2)&&(tool!=null)){
                        tool.show();
                    }
                }
                text = materialSearchBar.getText();
                searchFilterSelection = "";//materialSearchBar.getText();
                updateData();
                //materialSearchBar.setText("");
                //filterSelection = "";
                //FIXME: Avoid called this whole recycler initializer function
                //bindingAdapterToRecycleViewer();
            } else {
                sortButton.setVisibility(View.GONE);
                materialSearchBar.setText(text);
                if (MainActivity.getTutorialMode() == true){
                    if((tooltipState == 1)&&(tool!=null)){
                        nextToolTip();
                    }
                }

                //materialSearchBar.setText("");
            }
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
            searchFilterSelection = materialSearchBar.getText();
            updateData();
            // This is where you set the state for the recyclerview

        }

        @Override
        public void onButtonClicked(int buttonCode) {
            //***************doesn't work, don't know why

            /*
                if (buttonCode == BUTTON_BACK){
                    searchFilterSelection = materialSearchBar.getText();
                    updateData();
                }
            */
        }
    };

    private ImageView.OnClickListener sortButtonListener
            = new View.OnClickListener() {
        public void onClick(View v) {
            //SoftwareListFragment dialog = new SoftwareListFragment();
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
//            SoftwareListFragment dialog = new SoftwareListFragment();
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

             tempDynamicDataList = new ArrayList<>(SIngleton2ShareData.getLabDynamicDataObjects());
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

    private void updateData(){

        List<LabDataModel> sortedList = new ArrayList<>(tempDynamicDataList);

        if (sortedList.size() == 0){
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            return;
        }
        else{
            findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            findViewById(R.id.loading).setVisibility(View.INVISIBLE);
        }

        //**********************************DEMO-MODE**************************************//
        if (MainActivity.demoMode == true){
            sortedList = demoModeFilter(sortedList);
        }
        //*********************************DEMO-MODE-END***********************************//

        //****************************************FILTERS********************************//

        //Floor Filter
        if (floorFilter != 0){
            sortedList = filterByFloor(sortedList);
        }

        //Favourite Filter
        if (favouriteFilter == true){
            sortedList = filterByFavourite(sortedList);
        }

        //Search Filter
        if (searchFilterSelection != null){
            if (!searchFilterSelection.equals("")){
                sortedList = searchFilter(sortedList);
            }
        }
        //************************************FILTERS-END********************************//

        //**********************************SORTING**************************************//
        sortedList = sortLabList(sortedList);
        //*********************************SORTING-END***********************************//

        //Update Sync List
        findViewById(R.id.noLabsMessage).setVisibility(View.GONE);
        findViewById(R.id.disableFavourites).setVisibility(View.GONE);
        if (sortedList.size() == 0){
            findViewById(R.id.noLabsMessage).setVisibility(View.VISIBLE);
            if(favouriteFilter == true){
                findViewById(R.id.disableFavourites).setVisibility(View.VISIBLE);
            }
        }

        //Update data
        recyclerViewAdapter.updateLabData(sortedList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private List<LabDataModel> demoModeFilter(List<LabDataModel> input){
        for (int i = 0; i < input.size(); i++) {
            int fakeTemperature;
            int fakeOccupancy;

            Random rand = new Random();
            fakeTemperature = rand.nextInt(12) + 18;
            fakeOccupancy = rand.nextInt(30);
            if (fakeOccupancy > Integer.parseInt(input.get(i).getTotalCapacity())){
                fakeOccupancy = Integer.parseInt(input.get(i).getTotalCapacity());
            }

            input.get(i).setNumberOfStudentsPresent(Integer.toString(fakeOccupancy));
            input.get(i).setTemperature(Integer.toString(fakeTemperature));
        }
        return input;
    }

    private List<LabDataModel> searchFilter(List<LabDataModel> input){
        List<LabDataModel> output = new ArrayList<>();
        List<String> labList = new ArrayList<>(SIngleton2ShareData.getLabList(searchFilterSelection));
        for(int i = 0;i<input.size();i++){
            for(int j = 0;j<labList.size();j++){
                if (labList.get(j).equals(input.get(i).getRoomCode())){
                    output.add(input.get(i));
                    break;
                }
            }
        }
        return output;
    }

    private List<LabDataModel> filterByFloor(List<LabDataModel> input){
        List<LabDataModel> output = new ArrayList<>();
        for(int i = 0;i<input.size();i++){
            //System.out.println(input.get(i).getFloor());
            input.get(i).getRoomCode();
            if ((input.get(i).getFloor() == floorFilter)|| (floorFilter == 0)){
                output.add(input.get(i));
            }
        }
        return output;
    }

    private List<LabDataModel> filterByFavourite(List<LabDataModel> input){
        List<LabDataModel> output = new ArrayList<>();
        for(int i = 0;i<input.size();i++){
            //System.out.println(input.get(i).isFavourite());
            if (input.get(i).isFavourite()){
                output.add(input.get(i));
            }
        }
        return output;
    }

    private List<LabDataModel> sortLabList(List<LabDataModel> input) {
        List<LabDataModel> output = new ArrayList<>();
        while (input.size() > 0) {   //basically a selection sort
            int index = 0;
            float referenceParameter = 0;
            boolean ascending = false; //if true then the sort method is ascending,  The sorting always works in ascending but if this flag is set the "next value" will be placed at the beginning of the list instead of the end, essentially making the output in ascending order
            for (int i = 0; i < input.size(); i++) {
                float compareValue = 0;
                switch (sortType) {  //depending on the sort type requested, the key value being compared will change.  By default it sorts in descending order  (biggest first)
                    case NONE:
                        ascending = true;
                        compareValue = Integer.parseInt(input.get(i).getRoomCode().substring(1));
                        break;
                    case TEMP_UP:
                        ascending = true;
                    case TEMP_DOWN:
                        String stringTemp = input.get(i).getTemperature();
                        if (!stringTemp.contains("?")){
                            compareValue = Float.parseFloat(stringTemp);
                        }
                        else{
                            compareValue = 999;
                        }
                        break;
                    case PEOPLE_DOWN:
                        ascending = true;  //logic needs to be flipped (Down sets the ascending flag) since this sorting is based on a subtraction
                    case PEOPLE_UP:
                        String stringPeople = input.get(i).getNumberOfStudentsPresent();
                        String stringTotalPeople = input.get(i).getTotalCapacity();
                        if (!stringPeople.contains("?")){
                            compareValue = Integer.parseInt(stringTotalPeople) - Integer.parseInt(stringPeople);
                        }
                        else{
                            compareValue = -1;
                        }
                        break;
                }
                if (compareValue > referenceParameter) {
                    referenceParameter = compareValue;
                    index = i;
                }
            }
            if (ascending == true) {
                output.add(0, input.get(index));
            } else {
                output.add(input.get(index));
            }
            input.remove(index);
        }
        return output;
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

    public static void initTooltips(){
        tooltipState = 0;
        listToolTipState = 0;
    }
    private void processTooltips(){
        if (MainActivity.getTutorialMode() == true) {
            switch (tooltipState) {
                case 0:
                    buildToolTip("Press a Lab to Expand for More Info", Gravity.TOP, recyclerViewVar);
                    break;
                case 1:
                    buildToolTip("You can Search for a Specific Software", Gravity.BOTTOM, searchCard);
                    break;
                case 2:
                    buildToolTip("Press Here or Pull From Edge to Open Menu", Gravity.RIGHT, sortButton);
                    break;

                default:
                    break;

            }
        }
    }
    private void buildToolTip(String text, int gravity, View v){
        tool = new Tooltip.Builder(v, R.style.Tooltip)
                .setCancelable(false)
                .setDismissOnClick(false)
                .setCornerRadius(20f)
                .setGravity(gravity)
                .setText(text)
                .setTextSize(R.dimen.toolTipSize)
                .show();
        tool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull Tooltip tooltip) {
                nextToolTip();
            }
        });
    }

    public void nextToolTip(){
        tool.dismiss();
        tooltipState++;
        processTooltips();
    }

    public void addFavouriteToSharedPreference(String room){
        profile.favouriteList.add(new LabFavourite(room,true));
        sharedPreferenceHelper.saveSettings(profile);
    }
    public void deleteFavouriteFromSharedPreference(String room){
        for (int i=0;i<profile.favouriteList.size();i++){
            if (profile.favouriteList.get(i).labCode.equals(room)){
                profile.favouriteList.remove(i);
                break;
            }
        }
        sharedPreferenceHelper.saveSettings(profile);
    }

    private void syncFavouriteList(){
        for(int i = 0;i<tempDynamicDataList.size();i++){
            for (int j = 0; j < favouriteList.size();j++){
                if (tempDynamicDataList.get(i).getRoomCode().equals(favouriteList.get(j).labCode)){
                    tempDynamicDataList.get(i).setFavourite(favouriteList.get(j).favourite);
                    break;
                }
            }
        }
    }

}
