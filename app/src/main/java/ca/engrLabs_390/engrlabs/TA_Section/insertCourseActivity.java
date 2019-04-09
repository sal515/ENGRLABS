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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_course);


        AutoCompleteTextView courseNameNcodeAutoCom = findViewById(R.id.courseNameNCodeAutoComplete);
        AutoCompleteTextView sectionAutoCom = findViewById(R.id.section);
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

                Intent insertCourseIntent = new Intent(getApplicationContext(), CourseSectionSelection.class);
                startActivity(insertCourseIntent);

                // this is the place to add it to shared preference

            }
        });


    }
}

