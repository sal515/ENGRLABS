package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.Parser.Classroom;
import ca.engrLabs_390.engrlabs.Parser.Parser;
import ca.engrLabs_390.engrlabs.Parser.Software;
import ca.engrLabs_390.engrlabs.database_files.recyclerViewData;
import ca.engrLabs_390.engrlabs.recyclerView.dataAdapter_recyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

import static com.mancj.materialsearchbar.MaterialSearchBar.BUTTON_BACK;

public class ExpandableRecyclerWithBottomNav extends AppCompatActivity {

    // Testing TextBox for Bottom Navigation Bar
    private TextView mTextSeletectionTextBox;

    // Bottom Navigation Bar variable
    BottomNavigationView navigation;

    // Recycler Adapter variable declaration
    dataAdapter_recyclerView recyclerViewAdapter;

    //Suggestion list for search bar
    List<String> suggestList = new ArrayList<>();
    List<String> fullSuggestList = new ArrayList<>();   //will contain the entire list of softwares

    //Search Bar
    MaterialSearchBar materialSearchBar;

    // DummyClassData
    List<LabInfo> dummyClassList = new ArrayList<>();
    int floorMode;
    List<LabInfo> filteredDummyList = new ArrayList<>();
    String filterSelection;
    Vector<Software> soft;  //make list to store software

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

        // Calling the initial setup functions -> "ORDER of CALL MATTERS"
        initializeAllReferences();
        setListeneres();

        // calling the recycler binding function
        bindingAdapterToRecycleViewer();

    }

    private void initializeAllReferences() {
        mTextSeletectionTextBox = (TextView) findViewById(R.id.message);

        // Initializing the bottom nav bar reference
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);

        // Initialize RecyclerView variable
        recyclerViewVar = findViewById(R.id.expandingRecyclerView);

        //Dummy CLass List
        floorMode = 0;
        filterSelection = "";
        dummyClassList.add(new LabInfo(8,21,25,10,30,40));
        dummyClassList.add(new LabInfo(8,23,22,12,30,60));
        dummyClassList.add(new LabInfo(8,47,24,9,30,20));
        dummyClassList.add(new LabInfo(9,13,21,5,30,10));
        dummyClassList.add(new LabInfo(9,17,28,16,30,30));
        dummyClassList.add(new LabInfo(9,21,24,24,30,35));
        dummyClassList.add(new LabInfo(10,52,22,18,30,25));
        dummyClassList.add(new LabInfo(10,16,26,14,30,45));

        //Initialize Search Bar and SUggestion List
        populateSuggestionList();
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your software or 'all'");
        materialSearchBar.setVisibility(View.GONE);
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

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                //When search bar is closed
                //Restore original adapter
                if (!enabled){
                    materialSearchBar.setText("");
                    filterSelection = "";
                    bindingAdapterToRecycleViewer();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filterSelection = materialSearchBar.getText();
                bindingAdapterToRecycleViewer();
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                //***************doesn't work, don't know why
                /*
                if (buttonCode == BUTTON_BACK){
                    materialSearchBar.setText(null);
                    filterSelection = null;
                    bindingAdapterToRecycleViewer();
                }
                */
            }
        });
    }

    private void updateSuggestList(String inputString){
        suggestList.clear();
        if (inputString.length()<3) {    //if no text was entered, no substring searching needed
            materialSearchBar.clearSuggestions();
            return;
        }
        suggestList.addAll(fullSuggestList);    //all should print out everything as a suggestion
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

    private void setListeneres() {
        // Setting the bottom nav bar onItemSelection Listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    // Bottom Navigation bar OnItemSelectionListener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextSeletectionTextBox.setText(R.string.title_home);
                    recyclerViewAdapter.notifyDataSetChanged();
                    floorMode = 0;
                    bindingAdapterToRecycleViewer();
                    return true;
                case R.id.navigation_profile:
                    mTextSeletectionTextBox.setText(R.string.title_profile);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    if(materialSearchBar.getVisibility() == View.VISIBLE)
                    {
                        materialSearchBar.setVisibility(View.GONE);
                    }
                    else{
                        materialSearchBar.setVisibility(View.VISIBLE);
                    }
                    return false;
                case R.id.navigation_floor_8:
                    mTextSeletectionTextBox.setText(R.string.Floor_8);
                    floorMode = 8;
                    bindingAdapterToRecycleViewer();
                    return true;
                case R.id.navigation_floor_9:
                    mTextSeletectionTextBox.setText(R.string.Floor_9);
                    floorMode = 9;
                    bindingAdapterToRecycleViewer();
                    return true;
            }
            return false;
        }
    };


    private void bindingAdapterToRecycleViewer(){
        List<recyclerViewData> data;

        Queue<Integer> openedQueue = new LinkedList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        filteredDummyList.clear();
        for(int i = 0;i<dummyClassList.size();i++){
            if ((dummyClassList.get(i).floor == floorMode) || floorMode == 0)
            {
                filteredDummyList.add(dummyClassList.get(i));
            }
        }

        List<LabInfo> searchFilteredList;
        searchFilteredList = filterClasses(filteredDummyList);

        // get the data into an arrayList
        data = recyclerViewData.createContactsList(searchFilteredList.size());

        // pass the arrayList to the recyclerViewVar adapter
        recyclerViewAdapter = new dataAdapter_recyclerView(searchFilteredList);

        // set the custom adapter to the recycler view with the data model passed in
        recyclerViewVar.setAdapter(recyclerViewAdapter);


        // set the layout manager position the data according to the xml
        recyclerViewVar.setLayoutManager(new LinearLayoutManager(this));

        // setting the data for the adapter to the array created
        recyclerViewAdapter.submitList(data);




        // Need to work on the animators -- not working currently
//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        recyclerViewVar.setItemAnimator(itemAnimator);

    }

    private void populateSuggestionList(){
        Parser parser = new Parser();
        soft = parser.parse(this);  //parse the file in Assets folder
        fullSuggestList.clear();    //erase current suggest list
        for(int i =0;i<soft.size();i++){    //iterate through software list
            String temp = soft.get(i).softwareName;
            boolean duplicate = false;
            for(int j =0;j<fullSuggestList.size();j++){ //checks for duplicates
                if (fullSuggestList.get(j).equals(temp)){
                    duplicate = true;
                }
            }
            if (duplicate == false){    //if software wasnt already added, add it
                fullSuggestList.add(temp);
            }
        }
    }

    private List<LabInfo> filterClasses(List<LabInfo> input){

        if (filterSelection.equals("") || filterSelection == null){
            return input;
        }

        List<LabInfo> returnClassList = new ArrayList<>();

        List<Classroom> classes = new ArrayList<>();
        for(int i=0;i<soft.size();i++){
            if (soft.get(i).softwareName.equals(filterSelection)){
                classes.add(soft.get(i).classrooms);
            }
        }

        for(int i=0;i<input.size();i++){
            boolean valid = false;

            for(int j=0;j<classes.size();j++){
                if ((input.get(i).floor == classes.get(j).floor) && (input.get(i).room == classes.get(j).room)){
                    valid = true;
                }
            }
            if (valid == true){
                returnClassList.add(input.get(i));
            }
        }
        return returnClassList;
    }


}
