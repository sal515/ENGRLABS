package ca.engrLabs_390.engrlabs.Parser;

public class Software {

    //public cuz i dont feel like making get and sets
    public Classroom classrooms;
    public String softwareName;

    public Software(String name,Classroom classes){
        this.classrooms = classes;
        this.softwareName = name;
    }
}
