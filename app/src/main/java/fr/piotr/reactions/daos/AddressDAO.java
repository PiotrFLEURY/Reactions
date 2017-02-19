package fr.piotr.reactions.daos;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import fr.piotr.reactions.Rule;
import fr.piotr.reactions.persistence.AddressReference;

/**
 * Created by piotr_000 on 21/01/2017.
 *
 */

public class AddressDAO extends AbstractJsonDAO {

    public static final String FILENAME = "ReactionsAddresses.json";

    public static final String ID = "ID";
    public static final String LINE_0 = "LINE_0";
    public static final String LOCALITY = "LOCALITY";
    public static final String POSTAL_CODE = "POSTAL_CODE";
    public static final String COUNTRY_NAME = "COUNTRY_NAME";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String LATITUDE = "LATITUDE";

    public void save(Context context, List<AddressReference> addresses) {
        saveFile(context, toJson(addresses), FILENAME);
    }

    public List<AddressReference> loadAddresses(Context context) {
        String json = loadJsonFromFile(context, FILENAME);
        return fromJson(json);
    }

    private List<AddressReference> fromJson(String json) {
        List<AddressReference> addressReferences = new ArrayList<>();

        try {
            JSONArray jsonRules = new JSONArray(json);
            for(int i=0;i<jsonRules.length();i++){
                JSONObject jsonObject = jsonRules.getJSONObject(i);

                UUID id = UUID.fromString(jsonObject.getString(ID));

                Address address = new Address(Locale.getDefault());

                address.setAddressLine(0, jsonObject.getString(LINE_0));
                address.setLocality(jsonObject.getString(LOCALITY));
                address.setPostalCode(jsonObject.getString(POSTAL_CODE));
                address.setCountryName(jsonObject.getString(COUNTRY_NAME));

                address.setLongitude(jsonObject.getDouble(LONGITUDE));
                address.setLatitude(jsonObject.getDouble(LATITUDE));

                addressReferences.add(new AddressReference(id, address));
            }
        } catch(JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

        return addressReferences;
    }

    private String toJson(List<AddressReference> addresses) {
        JSONArray jsonRules = new JSONArray();
        try {
            for (AddressReference addressReference : addresses) {

                UUID id = addressReference.getId();
                Address address = addressReference.getAddress();

                JSONObject jsonRule = new JSONObject();

                jsonRule.put(ID, id.toString());

                jsonRule.put(LINE_0, address.getAddressLine(0));
                jsonRule.put(LOCALITY, address.getLocality());
                jsonRule.put(POSTAL_CODE, address.getPostalCode());
                jsonRule.put(COUNTRY_NAME, address.getCountryName());

                jsonRule.put(LONGITUDE, ""+address.getLongitude());
                jsonRule.put(LATITUDE, ""+address.getLatitude());

                jsonRules.put(jsonRule);
            }
        } catch(JSONException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        String json = jsonRules.toString();
        Log.d(getClass().getSimpleName(), json);
        return json;
    }

}
