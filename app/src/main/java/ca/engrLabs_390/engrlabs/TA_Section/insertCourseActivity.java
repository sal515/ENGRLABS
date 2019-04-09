package ca.engrLabs_390.engrlabs.TA_Section;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.Settings;
import ca.engrLabs_390.engrlabs.SharedPreferenceHelper;
import ca.engrLabs_390.engrlabs.courseObject;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;

public class insertCourseActivity extends AppCompatActivity {

    int state;
    public SharedPreferenceHelper sharedPreferenceHelper;
    public Settings profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_course);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.navBar)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status)); //status bar or the time bar at the top
        }
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        final AutoCompleteTextView courseNameNcodeAutoCom = findViewById(R.id.courseNameNCodeAutoComplete);
        final AutoCompleteTextView sectionAutoCom = findViewById(R.id.section);
        Button addButton = findViewById(R.id.addCourseButton);
        Button cancelButton = findViewById(R.id.cancelButtonAddCourse);

        ArrayAdapter<String> courseNameNCodeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, SIngleton2ShareData.getCoursesList());
        courseNameNcodeAutoCom.setAdapter(courseNameNCodeAdapter);


        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, SIngleton2ShareData.getSections("AERO-455"));
        sectionAutoCom.setAdapter(sectionAdapter);


        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent insertCourseIntent = new Intent(getApplicationContext(), CourseSectionSelection.class);
                startActivity(insertCourseIntent);

            }
        });


        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textBoxCourseName = courseNameNcodeAutoCom.getText().toString();
                String textBoxSection = sectionAutoCom.getText().toString();
                if (state == 0){
                    if (!textBoxCourseName.equals("")){
                        state++;
                        findViewById(R.id.section).setVisibility(View.VISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Course Name Cannot be Empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(state == 1){
                    if ((!textBoxCourseName.equals(""))&&(!textBoxSection.equals(""))){
                        boolean valid = false;
                        /*
                        for(int i=0;i<SIngleton2ShareData.getCoursesList().size();i++){
                            if (textBoxSection.equals(SIngleton2ShareData.getCoursesList().get(i))){
                                valid = true;
                            }
                        }
                        if (valid == false){
                            Toast.makeText(getApplicationContext(), "Invalid Section", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        */
                        profile = sharedPreferenceHelper.getSettings();
                        if(profile == null){
                            profile = new Settings();
                            profile.favouriteFilter = false;
                            profile.filterType = 0;
                            profile.sortType = Settings.SortTypes.NONE;
                            profile.favouriteList = new ArrayList<>();
                            profile.courseList = new ArrayList<>();
                            sharedPreferenceHelper.saveSettings(profile);
                        }
                        profile.courseList.add(new courseObject(textBoxCourseName,textBoxSection));
                        //sharedPreferenceHelper.saveSettings(profile);
                        sharedPreferenceHelper.saveSettings(profile);
                        Intent insertCourseIntent = new Intent(getApplicationContext(), CourseSectionSelection.class);
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        startActivity(insertCourseIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Fields Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    }

                }

                // this is the place to add it to shared preference

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        state = 0;
        findViewById(R.id.section).setVisibility(View.INVISIBLE);
    }
}

