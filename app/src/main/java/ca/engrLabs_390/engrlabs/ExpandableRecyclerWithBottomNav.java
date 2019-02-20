package ca.engrLabs_390.engrlabs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.TextView;

public class ExpandableRecyclerWithBottomNav extends AppCompatActivity {

    // Testing TextBox for Bottom Navigation Bar
    private TextView mTextSeletectionTextBox;

    // Bottom Navigation Bar variable
    BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler_with_bottom_nav);

        // Calling the initial setup functions -> "ORDER of CALL MATTERS"
        initializeAllReferences();
        setListeneres();
    }

    private void initializeAllReferences() {
        mTextSeletectionTextBox = (TextView) findViewById(R.id.message);
        // Initializing the bottom nav bar reference
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }
    private void setListeneres(){
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
                    return true;
                case R.id.navigation_dashboard:
                    mTextSeletectionTextBox.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextSeletectionTextBox.setText(R.string.title_notifications);
                    return true;
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


}
