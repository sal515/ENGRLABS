package ca.engrLabs_390.engrlabs;

public class LabInfo {
    public int floor;
    public int room;
    public int temperature;
    public int numberOfStudents;
    public int roomCapacity;
    public int upcomingClass;
    public boolean favourite;

    public LabInfo(int floor, int room, int temperature, int numberOfStudents, int roomCapacity, int upcomingClass) {
        this.floor = floor;
        this.room = room;
        this.temperature = temperature;
        this.numberOfStudents = numberOfStudents;
        this.roomCapacity = roomCapacity;
        this.upcomingClass = upcomingClass;
        this.favourite = false;
    }

    public LabInfo() {
    }
}
