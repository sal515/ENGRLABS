package ca.engrLabs_390.engrlabs;

public class Settings {

    public enum SortTypes {
        NONE,
        TEMP_UP,
        TEMP_DOWN,
        PEOPLE_UP,
        PEOPLE_DOWN,
    }

    public SortTypes sortType;
    public int filterType;
    public boolean favouriteFilter;
}
