// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.tooltip.OnClickListener;
import com.tooltip.Tooltip;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ca.engrLabs_390.engrlabs.TA_Section.LoginActivity;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;
import ca.engrLabs_390.engrlabs.notifications.AlarmReceiver;

public class MainActivity extends AppCompatActivity {

    // Declare reference variables
    Button loginBtn;
    Button homepageBtn;
    ImageView logo;
    private NotificationManagerCompat notificationManager;

    //Handles Tutorial Mode
    private Switch tutorialModeSwitch;  //the switch
    private static boolean tutorialMode = false; //global variable which stores the status of tutorial mode, modified by tutorialModeSwitch
    private static int tooltipState = 0; //local state machine to control active tooltip
    private Tooltip tool;   //local tooltip

    //Handles demo mode
    private Switch demoModeSwitch;
    public static boolean demoMode;

    // =========  Nav Drawer Stuff   ==========
    DrawerLayout drawer;
    NavigationView navigationView;
    MenuItem homePageNavButton;
    MenuItem labListNavButton;
    MenuItem taLoginNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
//        setupWindowAnimations();

        SIngleton2ShareData.downloadDynamicDataForRecyclerStartUp();
        SIngleton2ShareData.extractParsedSoftwareData();

        initializeReferences();
        initializeListeners();

        notificationManager = NotificationManagerCompat.from(this);

        //setScheduledNotification(Calendar.TUESDAY,23,42,0);  //Monday at 10:45pm
    }

    @Override
    protected void onStart(){
        super.onStart();
        if ((tutorialMode == true)&&(!tutorialModeSwitch.isChecked())){
            tutorialModeSwitch.setChecked(true);
            if (tool != null){
                tool.dismiss();
            }
            processTooltips();
        }
        if ((demoMode == true)&&(!demoModeSwitch.isChecked())){
            demoModeSwitch.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.lablist).setChecked(false);
        navigationView.getMenu().findItem(R.id.homepage).setChecked(true);
        navigationView.getMenu().findItem(R.id.taSection).setChecked(false);
    }

    // Slide animation between activity
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    void initializeReferences() {
        loginBtn = findViewById(R.id.loginBtn);
        homepageBtn = findViewById(R.id.homepageBtn);
        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.ic_logo_better);
        tutorialModeSwitch = findViewById(R.id.tutorialMode);
        demoModeSwitch = findViewById(R.id.demoMode);
        drawer = findViewById(R.id.drawerContainer);
        navigationView = findViewById(R.id.nav_view);

        homePageNavButton = findViewById(R.id.homepage);
        labListNavButton = findViewById(R.id.lablist);
        taLoginNavButton = findViewById(R.id.taSection);
        navigationView.getMenu().getItem(1).setVisible(false);
        navigationView.getMenu().getItem(2).setVisible(false);
        navigationView.getMenu().getItem(3).setVisible(false);
    }

    void initializeListeners() {
        loginBtn.setOnClickListener(loginBtnOnclickListener);
        homepageBtn.setOnClickListener(homepageBtnOnclickListener);
        tutorialModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutorialMode = tutorialModeSwitch.isChecked();

                if (tutorialMode == true){
                    setTooltips();
                }
                else{
                    if (tool != null){
                        tool.dismiss();
                    }
                }
            }
        });
        demoModeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoMode = demoModeSwitch.isChecked();
                setTimedNotification(10);
                if (demoModeSwitch.isChecked()){
                    Toast.makeText(getApplicationContext(), "Demo Mode Enabled", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Demo Mode Disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homepage:
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.lablist:
                        startActivity(new Intent(getApplicationContext(), ExpandableRecycler.class));
                        navigationView.getMenu().findItem(R.id.homepage).setChecked(false);
                        break;
                    case R.id.taSection:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        navigationView.getMenu().findItem(R.id.homepage).setChecked(false);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
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
        Intent intent = new Intent(this, ExpandableRecycler.class);

        startActivity(intent);
    }

    // going from mainActivity to login activity for testing
    public void goto_loginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void setScheduledNotification(int dayOfWeek, int hourOfDay, int minute, int second){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,second);

        //cal.add(Calendar.MINUTE, 1);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }

    public void setTimedNotification(int seconds){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.SECOND, seconds);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
    public static boolean getTutorialMode(){
        return tutorialMode;
    }

    private void setTooltips(){
        if (tutorialMode = true){
            if (tool != null){
                tool.dismiss();
            }
            tooltipState = 0;
            ExpandableRecycler.initTooltips();
            processTooltips();
        }
    }
    private void processTooltips(){
        switch (tooltipState){
            case 0:
                buildToolTip("Click On a Tip to Dismiss it", Gravity.BOTTOM,tutorialModeSwitch);
                break;
            case 1:
                buildToolTip("Login as a TA",Gravity.TOP,loginBtn);
                break;
            case 2:
                buildToolTip("View the List of Labs",Gravity.BOTTOM,homepageBtn);
                break;

            default:
                break;

        }
    }
    private void buildToolTip(String text, int gravity, View v){
        tool = new Tooltip.Builder(v, R.style.Tooltip)
                .setCancelable(false)
                .setDismissOnClick(false)
                .setCornerRadius(20f)
                .setGravity(gravity)
                .setText(text)
                .setTextSize(R.dimen.toolTipSize)
                .show();
        tool.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull Tooltip tooltip) {
                tool.dismiss();
                tooltipState++;
                processTooltips();
            }
        });
    }
}
