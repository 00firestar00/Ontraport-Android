package com.ontraport.mobileapp.main.record.views;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.ontraport.mobileapp.main.record.asynctasks.UpdateAsyncTask;
import com.ontraport.sdk.http.Meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryViewHolder extends DropDownViewHolder {

    public CountryViewHolder(View view) {
        super(view);
    }

    @Override
    public void setText(String key, String value, String alias) {
        super.setText(key, value, alias);
        label.setText(alias);
        drop_down.setTag(key);
        fetchData(key, value);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String new_value = drop_down.getItemAtPosition(position).toString();
        if (old_val.equals("0") && new_value.isEmpty()) {
            new_value = "0";
        }
        String field = (String) drop_down.getTag();
        if (!new_value.equals(old_val)) {
            doUpdate(field, new_value);
        }
    }

    @Override
    public void fetchData(String field, String field_value) {
        Meta.Field meta_field = getMetaForField(field);
        if (meta_field.hasOptions()) {
            // Drop or list
            if (meta_field.getType().equals("country")) {
                Map<String, String> options = getCountryMap();
                List<String> values = new ArrayList<>(options.values());
                populateDropdown(values);
                setDefaultValue(values.indexOf(options.get(field_value)) + 1);
            }
        }
    }

    @Override
    protected void doUpdate(String field, String new_val) {
        Meta.Field meta_field = getMetaForField(field);
        if (!meta_field.hasOptions()) {
            return;
        }
        Map<String, String> options = getCountryMap();
        new_val = options.get(new_val);

        if (!new_val.equals(old_val)) {
            super.doUpdate(field, new_val);
            old_val = new_val;
            params.put(field, new_val);
            new UpdateAsyncTask(view.getContext()).execute(params);
        }
    }

    protected Map<String, String> getCountryMap() {
        Map<String, String> map = new HashMap<>();
        map.put("United States", "US");
        map.put("Canada", "CA");
        map.put("United Kingdom", "GB");
        map.put("Afghanistan", "AF");
        map.put("Åland", "AX");
        map.put("Albania", "AL");
        map.put("Algeria", "DZ");
        map.put("American Samoa", "AS");
        map.put("Andorra", "AD");
        map.put("Angola", "AO");
        map.put("Anguilla", "AI");
        map.put("Antarctica", "AQ");
        map.put("Antigua and Barbuda", "AG");
        map.put("Argentina", "AR");
        map.put("Armenia", "AM");
        map.put("Aruba", "AW");
        map.put("Australia", "AU");
        map.put("Austria", "AT");
        map.put("Azerbaijan", "AZ");
        map.put("Bahamas", "BS");
        map.put("Bahrain", "BH");
        map.put("Bangladesh", "BD");
        map.put("Barbados", "BB");
        map.put("Belarus", "BY");
        map.put("Belgium", "BE");
        map.put("Belize", "BZ");
        map.put("Benin", "BJ");
        map.put("Bermuda", "BM");
        map.put("Bhutan", "BT");
        map.put("Bolivia", "BO");
        map.put("Bonaire", "BQ");
        map.put("Bosnia and Herzegovina", "BA");
        map.put("Botswana", "BW");
        map.put("Bouvet Island", "BV");
        map.put("Brazil", "BR");
        map.put("British Indian Ocean Territory", "IO");
        map.put("British Virgin Islands", "VG");
        map.put("Brunei", "BN");
        map.put("Bulgaria", "BG");
        map.put("Burkina Faso", "BF");
        map.put("Burundi", "BI");
        map.put("Cambodia", "KH");
        map.put("Cameroon", "CM");
        map.put("Cape Verde", "CV");
        map.put("Cayman Islands", "KY");
        map.put("Central African Republic", "CF");
        map.put("Chad", "TD");
        map.put("Chile", "CL");
        map.put("China", "CN");
        map.put("Christmas Island", "CX");
        map.put("Cocos (Keeling) Islands", "CC");
        map.put("Colombia", "CO");
        map.put("Comoros", "KM");
        map.put("Cook Islands", "CK");
        map.put("Costa Rica", "CR");
        map.put("Croatia (Local Name: Hrvatska)", "HR");
        map.put("Cuba", "CU");
        map.put("Curacao", "CW");
        map.put("Cyprus", "CY");
        map.put("Czech Republic", "CZ");
        map.put("Democratic Republic of the Congo", "CD");
        map.put("Denmark", "DK");
        map.put("Djibouti", "DJ");
        map.put("Dominica", "DM");
        map.put("Dominican Republic", "DO");
        map.put("East Timor", "TL");
        map.put("Ecuador", "EC");
        map.put("Egypt", "EG");
        map.put("El Salvador", "SV");
        map.put("Equatorial Guinea", "GQ");
        map.put("Eritrea", "ER");
        map.put("Estonia", "EE");
        map.put("Ethiopia", "ET");
        map.put("Falkland Islands (Malvinas)", "FK");
        map.put("Faroe Islands", "FO");
        map.put("Fiji", "FJ");
        map.put("Finland", "FI");
        map.put("France", "FR");
        map.put("French Guiana", "GF");
        map.put("French Polynesia", "PF");
        map.put("French Southern Territories", "TF");
        map.put("Gabon", "GA");
        map.put("Gambia", "GM");
        map.put("Georgia", "GE");
        map.put("Germany", "DE");
        map.put("Ghana", "GH");
        map.put("Gibraltar", "GI");
        map.put("Greece", "GR");
        map.put("Greenland", "GL");
        map.put("Grenada", "GD");
        map.put("Guadeloupe", "GP");
        map.put("Guam", "GU");
        map.put("Guatemala", "GT");
        map.put("Guernsey", "GG");
        map.put("Guinea", "GN");
        map.put("Guinea-Bissau", "GW");
        map.put("Guyana", "GY");
        map.put("Haiti", "HT");
        map.put("Heard Island and McDonald Islands", "HM");
        map.put("Honduras", "HN");
        map.put("Hong Kong", "HK");
        map.put("Hungary", "HU");
        map.put("Iceland", "IS");
        map.put("India", "IN");
        map.put("Indonesia", "ID");
        map.put("Iran", "IR");
        map.put("Iraq", "IQ");
        map.put("Ireland", "IE");
        map.put("Isle of Man", "IM");
        map.put("Israel", "IL");
        map.put("Italy", "IT");
        map.put("Ivory Coast", "CI");
        map.put("Jamaica", "JM");
        map.put("Japan", "JP");
        map.put("Jersey", "JE");
        map.put("Jordan", "JO");
        map.put("Kazakhstan", "KZ");
        map.put("Kenya", "KE");
        map.put("Kiribati", "KI");
        map.put("Kosovo", "XK");
        map.put("Kuwait", "KW");
        map.put("Kyrgyzstan", "KG");
        map.put("Laos", "LA");
        map.put("Latvia", "LV");
        map.put("Lebanon", "LB");
        map.put("Lesotho", "LS");
        map.put("Liberia", "LR");
        map.put("Libya", "LY");
        map.put("Liechtenstein", "LI");
        map.put("Lithuania", "LT");
        map.put("Luxembourg", "LU");
        map.put("Macao", "MO");
        map.put("Macedonia", "MK");
        map.put("Madagascar", "MG");
        map.put("Malawi", "MW");
        map.put("Malaysia", "MY");
        map.put("Maldives", "MV");
        map.put("Mali", "ML");
        map.put("Malta", "MT");
        map.put("Marshall Islands", "MH");
        map.put("Martinique", "MQ");
        map.put("Mauritania", "MR");
        map.put("Mauritius", "MU");
        map.put("Mayotte", "YT");
        map.put("Mexico", "MX");
        map.put("Micronesia", "FM");
        map.put("Moldova", "MD");
        map.put("Monaco", "MC");
        map.put("Mongolia", "MN");
        map.put("Montenegro", "ME");
        map.put("Montserrat", "MS");
        map.put("Morocco", "MA");
        map.put("Mozambique", "MZ");
        map.put("Myanmar (Burma)", "MM");
        map.put("Namibia", "NA");
        map.put("Nauru", "NR");
        map.put("Nepal", "NP");
        map.put("Netherlands", "NL");
        map.put("New Caledonia", "NC");
        map.put("New Zealand", "NZ");
        map.put("Nicaragua", "NI");
        map.put("Niger", "NE");
        map.put("Nigeria", "NG");
        map.put("Niue", "NU");
        map.put("Norfolk Island", "NF");
        map.put("North Korea", "KP");
        map.put("Northern Mariana Islands", "MP");
        map.put("Norway", "NO");
        map.put("Oman", "OM");
        map.put("Pakistan", "PK");
        map.put("Palau", "PW");
        map.put("Palestine", "PS");
        map.put("Panama", "PA");
        map.put("Papua New Guinea", "PG");
        map.put("Paraguay", "PY");
        map.put("Peru", "PE");
        map.put("Philippines", "PH");
        map.put("Pitcairn Islands", "PN");
        map.put("Poland", "PL");
        map.put("Portugal", "PT");
        map.put("Puerto Rico", "PR");
        map.put("Qatar", "QA");
        map.put("Republic of the Congo", "CG");
        map.put("Réunion", "RE");
        map.put("Romania", "RO");
        map.put("Russia", "RU");
        map.put("Rwanda", "RW");
        map.put("Saint Barthélemy", "BL");
        map.put("Saint Helena", "SH");
        map.put("Saint Kitts and Nevis", "KN");
        map.put("Saint Lucia", "LC");
        map.put("Saint Martin", "MF");
        map.put("Saint Pierre and Miquelon", "PM");
        map.put("Saint Vincent and the Grenadines", "VC");
        map.put("Samoa", "WS");
        map.put("San Marino", "SM");
        map.put("Saudi Arabia", "SA");
        map.put("Senegal", "SN");
        map.put("Serbia", "RS");
        map.put("Seychelles", "SC");
        map.put("Sierra Leone", "SL");
        map.put("Singapore", "SG");
        map.put("Sint Maarten", "SX");
        map.put("Slovakia (Slovak Republic)", "SK");
        map.put("Slovenia", "SI");
        map.put("Solomon Islands", "SB");
        map.put("Somalia", "SO");
        map.put("South Africa", "ZA");
        map.put("South Georgia and the South Sandwich Islands", "GS");
        map.put("South Korea", "KR");
        map.put("South Sudan", "SS");
        map.put("Spain", "ES");
        map.put("Sri Lanka", "LK");
        map.put("Sudan", "SD");
        map.put("Suriname", "SR");
        map.put("Svalbard and Jan Mayen", "SJ");
        map.put("Swaziland", "SZ");
        map.put("Sweden", "SE");
        map.put("Switzerland", "CH");
        map.put("Syria", "SY");
        map.put("São Tomé and Príncipe", "ST");
        map.put("Taiwan", "TW");
        map.put("Tajikistan", "TJ");
        map.put("Tanzania", "TZ");
        map.put("Thailand", "TH");
        map.put("Togo", "TG");
        map.put("Tokelau", "TK");
        map.put("Tonga", "TO");
        map.put("Trinidad and Tobago", "TT");
        map.put("Tunisia", "TN");
        map.put("Turkey", "TR");
        map.put("Turkmenistan", "TM");
        map.put("Turks and Caicos Islands", "TC");
        map.put("Tuvalu", "TV");
        map.put("U.S. Minor Outlying Islands", "UM");
        map.put("U.S. Virgin Islands", "VI");
        map.put("Uganda", "UG");
        map.put("Ukraine", "UA");
        map.put("United Arab Emirates", "AE");
        map.put("Uruguay", "UY");
        map.put("Uzbekistan", "UZ");
        map.put("Vanuatu", "VU");
        map.put("Vatican City", "VA");
        map.put("Venezuela", "VE");
        map.put("Vietnam", "VN");
        map.put("Wallis and Futuna", "WF");
        map.put("Western Sahara", "EH");
        map.put("Yemen", "YE");
        map.put("Zambia", "ZM");
        map.put("Zimbabwe", "ZW");
        return map;
    }


    void populateDropdown(List<String> values) {
        ArrayAdapter<String> adapter = getNewAdapter();
        adapter.add("");
        adapter.addAll(values);
        drop_down.setAdapter(adapter);
    }
}
