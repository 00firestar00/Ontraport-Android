package com.ontraport.mobileapp.main.record.views;

import android.view.View;
import com.ontraport.mobileapp.main.record.asynctasks.UpdateAsyncTask;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateViewHolder extends DropDownViewHolder {

    public StateViewHolder(View view) {
        super(view);
    }


    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.getType().equals("state")) {
            Map<String, String> options = getStateMap();
            List<String> values = new ArrayList<>(options.values());
            populateDropdown(values);
            setDefaultValue(values.indexOf(options.get(field_value)) + 1);
        }
    }

    @Override
    protected void doUpdate(String field, String new_val) {
        Map<String, String> options = getStateMap();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if (entry.getValue().equals(new_val)) {
                new_val = entry.getKey();
                break;
            }
        }

        if (!new_val.equals(old_val)) {
            super.doUpdate(field, new_val);
            old_val = new_val;
            params.put(field, new_val);
            new UpdateAsyncTask(view.getContext()).execute(params);
        }
    }

    private Map<String, String> getStateMap() {
        Map<String, String> map = new HashMap<>();
        map.put("US", "United States");
        map.put("AL", "Alabama");
        map.put("AK", "Alaska");
        map.put("AZ", "Arizona");
        map.put("AR", "Arkansas");
        map.put("CA", "California");
        map.put("CO", "Colorado");
        map.put("CT", "Connecticut");
        map.put("DE", "Delaware");
        map.put("DC", "D.C.");
        map.put("FL", "Florida");
        map.put("GA", "Georgia");
        map.put("HI", "Hawaii");
        map.put("ID", "Idaho");
        map.put("IL", "Illinois");
        map.put("IN", "Indiana");
        map.put("IA", "Iowa");
        map.put("KS", "Kansas");
        map.put("KY", "Kentucky");
        map.put("LA", "Louisiana");
        map.put("ME", "Maine");
        map.put("MD", "Maryland");
        map.put("MA", "Massachusetts");
        map.put("MI", "Michigan");
        map.put("MN", "Minnesota");
        map.put("MS", "Mississippi");
        map.put("MO", "Missouri");
        map.put("MT", "Montana");
        map.put("NE", "Nebraska");
        map.put("NV", "Nevada");
        map.put("NH", "New Hampshire");
        map.put("NM", "New Mexico");
        map.put("NJ", "New Jersey");
        map.put("NY", "New York");
        map.put("NC", "North Carolina");
        map.put("ND", "North Dakota");
        map.put("OH", "Ohio");
        map.put("OK", "Oklahoma");
        map.put("OR", "Oregon");
        map.put("PA", "Pennsylvania");
        map.put("PR", "Puerto Rico");
        map.put("RI", "Rhode Island");
        map.put("SC", "South Carolina");
        map.put("SD", "South Dakota");
        map.put("TN", "Tennessee");
        map.put("TX", "Texas");
        map.put("UT", "Utah");
        map.put("VT", "Vermont");
        map.put("VA", "Virginia");
        map.put("WA", "Washington");
        map.put("WV", "West Virginia");
        map.put("WI", "Wisconsin");
        map.put("WY", "Wyoming");
        map.put("AB", "Alberta");
        map.put("BC", "British Columbia");
        map.put("MB", "Manitoba");
        map.put("NB", "New Brunswick");
        map.put("NL", "Newfoundland and Labrador");
        map.put("NS", "Nova Scotia");
        map.put("NT", "Northwest Territories");
        map.put("NU", "Nunavut");
        map.put("ON", "Ontario");
        map.put("PE", "Prince Edward Island");
        map.put("QC", "Quebec");
        map.put("SK", "Saskatchewan");
        map.put("YT", "Yukon");
        map.put("ACT", "(AU) Australian Capital Territory");
        map.put("NSW", "(AU) New South Wales");
        map.put("VIC", "(AU) Victoria");
        map.put("QLD", "(AU) Queensland");
        map.put("AU_NT", "(AU) Northern Territory");
        map.put("AU_WA", "(AU) Western Australia");
        map.put("SA", "(AU) South Australia");
        map.put("TAS", "(AU) Tasmania");
        map.put("GP", "(ZA) Gauteng");
        map.put("WP", "(ZA) Western Cape");
        map.put("EC", "(ZA) Eastern Cape");
        map.put("KZN", "(ZA) KwaZulu Natal");
        map.put("NW", "(ZA) North West");
        map.put("AF_NC", "(ZA) Northern Cape");
        map.put("MP", "(ZA) Mpumalanga");
        map.put("FS", "(ZA) Free State");
        map.put("_NOTLISTED_", "My State is not listed");
        return map;
    }
}
