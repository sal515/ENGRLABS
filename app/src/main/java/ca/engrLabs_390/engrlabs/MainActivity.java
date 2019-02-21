// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Declare reference variables
    Button loginBtn;
    Button homepageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeReferences();
        initializeListeners();

    }

    void initializeReferences() {
        loginBtn = findViewById(R.id.loginBtn);
        homepageBtn = findViewById(R.id.homepageBtn);
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
