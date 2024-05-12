package com.example.hp.smart_city_traveller;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hp on 8/4/2016.
 */
public class JSONParse {
    public String mainparse(JSONObject json) {
        String p="parse";
        try {
            p=json.getString("Value");
        } catch (JSONException e) {
            p=e.getMessage();
        }
        return p;
    }

    public String placeparse(String s) {
        String a="parse";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            a=jsonObject.getString("response");
        } catch (JSONException e) {
            a=e.getMessage();
        }

        return a;
    }

    public String parsing(JSONObject json) {
        String p="parse";
        try {
            p=json.getString("Value");
        } catch (JSONException e) {
            p=e.getMessage();
        }
        return p;
    }
}
