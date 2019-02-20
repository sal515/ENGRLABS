// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call goto_expandableRecyclerWithBottomNav() to go to expandableRecycler with Bottom Nav
        goto_expandableRecyclerWithBottomNav();

    }


    // going from mainActivity to ExpandableRecylerView with Bottom Nav
    public void goto_expandableRecyclerWithBottomNav() {
        // this intent object allows to link activity1 to activity2
        // Intent intent = new Intent(MainActivityView.this, second_activity.class);
        Intent intent = new Intent(this, ExpandableRecyclerWithBottomNav.class);

        // put extra is used to primitive data from one activity to another
        //        intent.putExtra("courseID", courseID);

        startActivity(intent);
    }
}
