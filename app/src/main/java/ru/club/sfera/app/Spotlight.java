package ru.club.sfera.app;

import android.app.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ru.club.sfera.constants.Constants;
import ru.club.sfera.model.Profile;

public class Spotlight extends Application implements Constants {

    private ArrayList<Profile> itemsList;

	@Override
	public void onCreate() {

		super.onCreate();

        itemsList = new ArrayList<>();
	}

    public void read_from_json(JSONObject jsonData) {

        if (this.itemsList == null) {

            itemsList = new ArrayList<>();
        }

        if (this.itemsList.size() > 0) {

            this.itemsList.clear();
        }

        try {

            if (jsonData.has("items")) {

                JSONArray usersArray = jsonData.getJSONArray("items");

                int arrayLength = usersArray.length();

                if (arrayLength > 0) {

                    for (int i = 0; i < usersArray.length(); i++) {

                        JSONObject userObj = (JSONObject) usersArray.get(i);

                        Profile item = new Profile(userObj);

                        itemsList.add(item);
                    }
                }
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    public ArrayList<Profile> getItemsList() {

        if (this.itemsList == null) {

            itemsList = new ArrayList<>();
        }

        return this.itemsList;
    }

    public void setItemsList(ArrayList<Profile> itemsList) {

        this.itemsList = itemsList;
    }
}