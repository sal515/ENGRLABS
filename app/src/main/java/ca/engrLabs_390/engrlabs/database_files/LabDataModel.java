package ca.engrLabs_390.engrlabs.database_files;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

//public class LabDataModel implements Comparable<LabDataModel> {
public class LabDataModel  {
    private int floor;
    private int room;
    private String roomStr;
    private String temperature;
    private String numberOfStudents;
    private int roomCapacity;
    //    private int upcomingClass;
    private boolean favourite;
    private boolean clicked;
    private String timeStamp;
    private String availability;


    private HashMap<String, String> upcomingClass = new HashMap<String, String>();


//    // Comparator for the PriorityQueue
//    @Override
//    public int compareTo(LabDataModel lab) {
////        if (this.getFloor() > lab.getFloor() && this.getRoom() > lab.getRoom()) {
////            return 1;
////        } else if (this.getFloor() > lab.getFloor() && this.getRoom() < lab.getRoom()) {
////            return -1;
////        } else {
////            return 0;
////        }
//
//        if (this.getRoom() > lab.getRoom()) {
//            return 1;
//        } else if (this.getRoom() < lab.getRoom()) {
//            return -1;
//        } else {
//            return 0;
//        }
//    }

    // reference : https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    @Override
    public boolean equals(@Nullable Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

                /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof LabDataModel)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        LabDataModel labDataModel = (LabDataModel) o;

//        // Compare the data members and return accordingly
//        return Double.compare(re, c.re) == 0
//                && Double.compare(im, c.im) == 0;

        return this.getNumberOfStudents().equals(labDataModel.getNumberOfStudents()) &&
                this.getTemperature().equals(labDataModel.getTemperature()) &&
                this.getAvailability().equals(labDataModel.getAvailability()) &&
                Objects.equals(this.getUpcomingClass().get("Category"), labDataModel.getUpcomingClass().get("Category")) &&
                Objects.equals(this.getUpcomingClass().get("StartHour"), labDataModel.getUpcomingClass().get("StartHour")) &&
                Objects.equals(this.getUpcomingClass().get("StartMin"), labDataModel.getUpcomingClass().get("StartMin")) &&
                Objects.equals(this.getUpcomingClass().get("StartSec"), labDataModel.getUpcomingClass().get("StartSec")) &&
                Objects.equals(this.getUpcomingClass().get("Subject"), labDataModel.getUpcomingClass().get("Subject")) &&
                Objects.equals(this.getUpcomingClass().get("Title"), labDataModel.getUpcomingClass().get("Title"));
    }


//    public LabDataModel(int floor,
//                        int room,
//                        String temperature,
//                        String numberOfStudents,
//                        int roomCapacity,
//                        HashMap<String, String> upcomingClass) {
//        this.floor = floor;
//        this.room = room;
//        this.temperature = temperature;
//        this.numberOfStudents = numberOfStudents;
//        this.roomCapacity = roomCapacity;
//        this.upcomingClass = upcomingClass;
//        this.favourite = false;
//        this.clicked = false;
//    }

    public LabDataModel() {
        this.floor = -1;
        this.room = -1;
        this.temperature = "";
        this.numberOfStudents = "";
        this.roomCapacity = -1;
        this.favourite = false;
        this.clicked = false;
        this.timeStamp = "";
        this.availability = "";
//        this.upcomingClass = null;
        this.upcomingClass.put("Category", "");
        this.upcomingClass.put("StartHour", "");
        this.upcomingClass.put("StartMin", "");
        this.upcomingClass.put("StartSec", "");
        this.upcomingClass.put("Subject", "");
        this.upcomingClass.put("Title", "");
    }

    public List<LabDataModel> generateLabs( int size) {
        List<LabDataModel> labDataModelList = new ArrayList<>();
        LabDataModel tempObj = new LabDataModel();

        for (int i = 0; i < size; i++) {
            tempObj.setRoom(800+i);
            labDataModelList.add(tempObj);
        }

        return labDataModelList;
    }

    // getters and setters

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getRoomStr() {
        return roomStr;
    }

    public void setRoomStr(String roomStr) {
        this.roomStr = roomStr;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    public String getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(String numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public HashMap<String, String> getUpcomingClass() {
        return upcomingClass;
    }

    public void setUpcomingClass(HashMap<String, String> upcomingClass) {
        this.upcomingClass = upcomingClass;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
