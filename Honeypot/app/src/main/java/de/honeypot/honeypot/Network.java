package de.honeypot.honeypot;

import android.content.Context;
import android.content.SharedPreferences;
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

    private String token;
    private String id;


    public Network(String Token, String ID){
        token=Token;
        id=ID;
    }


    static public boolean isInternt(){

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

    static public void register(String name, String android_id){

        try{

            HttpClient client = new DefaultHttpClient();


            HttpGet request = new HttpGet(link + "/register?device=" + android_id + "&name=" + name);



            HttpResponse response = client.execute(request);



            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            Log.i("Response of GET request", responseString);
            JSONObject jObject = new JSONObject(responseString);

            String token = jObject.getString("token");
            String id = jObject.getString("id");

            Log.i("token", token);
            Log.i("id", id);



        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public User profile(String id_else){        //Profile eines anderen aufrufen --> Rückgabewerte in den Attributen
        try{
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link+"/profile?id="+id+"&token="+token);
            HttpResponse response = client.execute(request);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            JSONObject jObject = new JSONObject(responseString);

            String name=jObject.getString("name");
            String ID = jObject.getString("id");
            int points = jObject.getInt("points");
            JSONArray jFriendsArray = jObject.getJSONArray("friends");//.getJSONObject(i);
            String picture = jObject.getString("picture");


            String[] friends = new String[jFriendsArray.length()];
            for(int i =0; i<jFriendsArray.length(); i++){
                friends[i]=jFriendsArray.getJSONObject(i).getString("id");
            }



            return new User(name, ID, points, friends, picture);



        } catch (IOException e) {
            e.printStackTrace();
            return new User();
        }catch (JSONException e) {
            e.printStackTrace();
            return new User();
        }
    }

    public User ownProfile(){ //eigenes Profil zum sync --> Rückgabewerte in den Attributen
        return profile(id);
    }

    public String[] nearby(double lat, double lon){

        try{
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(link+"/nearby?lat="+lat+"&lon="+lon+"&token="+token);
            HttpResponse response = client.execute(request);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();

            JSONObject jObject = new JSONObject(responseString);
//
//            String name=jObject.getString("name");
//            String ID = jObject.getString("id");
//            int points = jObject.getInt("points");
//            JSONArray jFriendsArray = jObject.getJSONArray();//.getJSONObject(i);
//            String picture = jObject.getString("picture");

            JSONArray jsonArray = jObject.optJSONArray("nearby");

            //JSONArray jsonArray = new JSONArray(responseString);


            String[] ids = new String[jsonArray.length()];
            for(int i =0; i<jsonArray.length(); i++){
                ids[i]=jsonArray.getString(i);
            }



            return ids;



        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }catch (JSONException e) {
            e.printStackTrace();
            return new String[0];
        }

    }
    public void capture(){
        //TODO
    }


}