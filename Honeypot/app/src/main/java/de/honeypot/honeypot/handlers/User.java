package de.honeypot.honeypot.handlers;

import org.json.JSONArray;

/**
 * Created by Tobias on 12.11.2016.
 */

public class User {

    public String name="";
    public String id;
    public int points;
    public String friends[];
    public String picture;

    public User(String Name, String ID, int Points, String Friends[], String Picture){
        name=Name;
        id = ID;
        points = Points;
        picture = Picture;

        friends = Friends;


    }

    public User(){

    }


}
