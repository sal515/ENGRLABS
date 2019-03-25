package ca.engrLabs_390.engrlabs.database_files;

import java.util.HashMap;

public class LabDataModel implements Comparable<LabDataModel> {
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


    // Comparator for the PriorityQueue
    @Override
    public int compareTo(LabDataModel lab) {
//        if (this.getFloor() > lab.getFloor() && this.getRoom() > lab.getRoom()) {
//            return 1;
//        } else if (this.getFloor() > lab.getFloor() && this.getRoom() < lab.getRoom()) {
//            return -1;
//        } else {
//            return 0;
//        }

        if (this.getRoom() > lab.getRoom()) {
            return 1;
        } else if (this.getRoom() < lab.getRoom()) {
            return -1;
        } else {
            return 0;
        }
    }

    public LabDataModel(int floor,
                        int room,
                        String temperature,
                        String numberOfStudents,
                        int roomCapacity,
                        HashMap<String, String> upcomingClass) {
        this.floor = floor;
        this.room = room;
        this.temperature = temperature;
        this.numberOfStudents = numberOfStudents;
        this.roomCapacity = roomCapacity;
        this.upcomingClass = upcomingClass;
        this.favourite = false;
        this.clicked = false;
    }

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
        this.upcomingClass = null;
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
