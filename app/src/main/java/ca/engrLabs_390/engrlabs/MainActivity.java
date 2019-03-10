// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import ca.engrLabs_390.engrlabs.TA_Section.LoginActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // Declare reference variables
    Button loginBtn;
    Button homepageBtn;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initializeReferences();
        initializeListeners();

    }

    void initializeReferences() {
        loginBtn = findViewById(R.id.loginBtn);
        homepageBtn = findViewById(R.id.homepageBtn);
        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.ic_menu_animated);
    }

    void initializeListeners() {
        loginBtn.setOnClickListener(loginBtnOnclickListener);
        homepageBtn.setOnClickListener(homepageBtnOnclickListener);
    }


    Button.OnClickListener loginBtnOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_loginActivity();
        }
    };

    Button.OnClickListener homepageBtnOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_homepageActivity();
        }
    };


    // going from mainActivity to ExpandableRecylerView with Bottom Nav
    public void goto_homepageActivity() {
        Intent intent = new Intent(this, ExpandableRecyclerWithBottomNav.class);

        // Example to pass info from one activity to another
        // put extra is used to primitive data from one activity to another
        //        intent.putExtra("courseID", courseID);

        startActivity(intent);
    }

    // going from mainActivity to login activity for testing
    public void goto_loginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
