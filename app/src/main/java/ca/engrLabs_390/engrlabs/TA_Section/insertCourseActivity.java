package ca.engrLabs_390.engrlabs.TA_Section;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;

public class insertCourseActivity extends AppCompatActivity {

    private boolean addCourseClick;
    private boolean saveCourseBool;

    private AutoCompleteTextView courseNameNcodeAutoCom;
    private AutoCompleteTextView sectionAutoCom;

    private Button addButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_course);

        addCourseClick = false;
        saveCourseBool = false;

        courseNameNcodeAutoCom = findViewById(R.id.courseNameNCodeAutoComplete);
        sectionAutoCom = findViewById(R.id.section);
        addButton = findViewById(R.id.addCourseButton);
        cancelButton = findViewById(R.id.cancelButtonAddCourse);

        ArrayAdapter<String> courseNameNCodeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, SIngleton2ShareData.getCoursesList());
        courseNameNcodeAutoCom.setAdapter(courseNameNCodeAdapter);


        addButton.setOnClickListener(addbuttonListener);


        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent insertCourseIntent = new Intent(getApplicationContext(), CourseSectionSelection.class);
                startActivity(insertCourseIntent);

            }
        });

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        addCourseClick = false;
        saveCourseBool = false;
    }

    Button.OnClickListener addbuttonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {


            if (saveCourseBool) {
                Intent insertCourseIntent = new Intent(getApplicationContext(), CourseSectionSelection.class);
                startActivity(insertCourseIntent);
            }

            if (addCourseClick == false) {
                sectionAutoCom.setVisibility(View.VISIBLE);
                courseNameNcodeAutoCom.setEnabled(false);

//                courseNameNcodeAutoCom.getText().toString();
                ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, SIngleton2ShareData.getSections(courseNameNcodeAutoCom.getText().toString()));
                sectionAutoCom.setAdapter(sectionAdapter);

                addCourseClick = true;
            }

            if (addCourseClick == true) {
                sectionAutoCom.setEnabled(true);



                // TODO::
                // read the 2 autocomplete using
                // autocompletextbox.getText().toString
                // save to the sharedPrefernce
                // Then populate the list in the courses added activity



                // Also the timer can be set from here timer --->
                //        List<String> sectionStartTime = new ArrayList<>(SIngleton2ShareData.getTimeOfSection("AERO-455", "TI-X"));

                // where the list sectionStartTime has
                //sectionStartTime[0] === hour as string
                //sectionStartTime[1] === min aas string
                //sectionStartTime[2] === sec as string


                saveCourseBool = true;

            }








            // this is the place to add it to shared preference
        }
    };


}

