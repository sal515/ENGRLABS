package ca.engrLabs_390.engrlabs.TA_Section;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ca.engrLabs_390.engrlabs.R;

public class CourseSectionSelection extends AppCompatActivity {

    private FirebaseAuth mAuth;


    Button signOutButton;
    ListView classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_section_selection);

        mAuth = FirebaseAuth.getInstance();


        //List
        classList = findViewById(R.id.selectedList);

        //format ActionBar and Content
        ActionBar ab = getSupportActionBar();   //get the Action Bar object
        ab.setTitle("TA Course Selection");   //set the title
        ab.setDisplayHomeAsUpEnabled(true); //enable UP button, parent is declared in the manifest

        //Sign out Button
        signOutButton = findViewById(R.id.signOutButtonInstructorPage);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();

                signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //Creates ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_selection,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handles action button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_course:
                ChooseCourseDialogFragment dialog = new ChooseCourseDialogFragment();
                dialog.show(getSupportFragmentManager(), "Insert Course");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mAuth.signOut();
//        updateUI(null);
    }

}
