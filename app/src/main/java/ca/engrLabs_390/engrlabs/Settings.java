package ca.engrLabs_390.engrlabs;

import java.util.List;

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
    public List<LabFavourite> favouriteList;
}
