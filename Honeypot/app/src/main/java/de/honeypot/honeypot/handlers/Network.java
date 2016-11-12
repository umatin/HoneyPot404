package de.honeypot.honeypot.handlers;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Tobias on 12.11.2016.
 */

public class Network {// bitte auf alle Methoden mit einem eigenen Task/Thread zugreifen

    public static String link = "http://honeypot4431.cloudapp.net";

    private static String token;
    private static String id;



    public static void init(String Token, String ID)
    {
        token=Token;
        id=ID;
    }

    static public boolean isInternt(){      //hat das handy internet

        try{
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link);
            HttpResponse response = client.execute(request);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            if (!responseString.equals("")){
                return true;
            }else{
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    static public void register(String name, String android_id){    //registriert das handy (wird eigentlich nicht gebraucht)
//
//        try{
//
//            HttpClient client = new DefaultHttpClient();
//
//
//            HttpGet request = new HttpGet(link + "/register?device=" + android_id + "&name=" + name);
//
//
//
//            HttpResponse response = client.execute(request);
//
//
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            response.getEntity().writeTo(out);
//            String responseString = out.toString();
//            out.close();
//
//            Log.i("Response of GET request", responseString);
//            JSONObject jObject = new JSONObject(responseString);
//
//            String token = jObject.getString("token");
//            String id = jObject.getString("id");
//
//            Log.i("token", token);
//            Log.i("id", id);
//
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    public static User profile(String id_else){        //Profile eines anderen aufrufen --> Rückgabewerte in den Attributen
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link+"/profile?id="+id+"&token="+token);
            HttpResponse response = client.execute(request);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();


            //DEBUG
            //String responseString = "{\"id\":4,\"name\":\"Miau\",\"points\":0,\"friends\":[2, 3, 4],\"picture\":\"\",\"color\":\"FF4081\"}";

            JSONObject jObject = new JSONObject(responseString);

            String name = jObject.getString("name");

            String ID = jObject.getString("id");
            int points = jObject.getInt("points");


            JSONArray jFriendsArray = jObject.getJSONArray("friends");//.getJSONObject(i);
            String picture = jObject.getString("picture");


            String[] friends = new String[jFriendsArray.length()];

            for(int i =0; i<jFriendsArray.length(); i++){
                friends[i]=jFriendsArray.getString(i);
                Log.i("test", friends[i]);
            }

            Log.i("user", ID + "," + name + ","  + picture+ ","+ points);
            return new User(name, ID, points, friends, picture);


        } catch (IOException e) { //falls kein
            e.printStackTrace();
            return new User();
        }catch (JSONException e) {
            e.printStackTrace();
            return new User();
        }
    }

    public static User ownProfile(){ //eigenes Profil zum sync --> Rückgabewerte in den Attributen
        return profile(id);
    }

    public static String[] nearby(double lat, double lon){     //gibt die geräte in der nähe aus

        try{
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link+"/nearby/"+lat+"/"+lon+"?token="+token);
            HttpResponse response = client.execute(request);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            JSONObject jObject = new JSONObject(responseString);


            JSONArray jsonArray = jObject.getJSONArray("nearby");//jObject.optJSONArray("nearby");




            String[] ids = new String[jsonArray.length()];
            for(int i =0; i<jsonArray.length(); i++){
                ids[i]=jsonArray.getString(i);
            }



            return ids;



        } catch (IOException e) { // falls kein Internet --> leeres array
            e.printStackTrace();
            return new String[0];
        }catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }

    }
    public static void capture(){
        //TODO
    }


}
