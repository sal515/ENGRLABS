package ca.engrLabs_390.engrlabs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

        // Initialize RecyclerView variable
        recyclerViewVar = findViewById(R.id.expandingRecyclerView);

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
                    return true;
                case R.id.navigation_dashboard:
                    mTextSeletectionTextBox.setText(R.string.title_dashboard);
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
                    return true;
                case R.id.navigation_floor_9:
                    mTextSeletectionTextBox.setText(R.string.Floor_9);
                    return true;
            }
            return false;
        }
    };


    private void bindingAdapterToRecycleViewer(){
        List<recyclerViewData> data;

        Queue<Integer> openedQueue = new LinkedList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


        // get the data into an arrayList
        data = recyclerViewData.createContactsList(40);

        // pass the arrayList to the recyclerViewVar adapter
        recyclerViewAdapter = new dataAdapter_recyclerView();

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
        Vector<Software> soft;  //make list to store software
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




}
