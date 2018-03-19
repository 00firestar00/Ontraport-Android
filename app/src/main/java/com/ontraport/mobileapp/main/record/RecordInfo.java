package com.ontraport.mobileapp.main.record;

import android.text.TextUtils;
import com.ontraport.mobileapp.Info;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.utils.FieldType;
import com.ontraport.sdk.exceptions.InvalidValueException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.objects.fields.BulkEmailStatus;
import com.ontraport.sdk.objects.fields.BulkSMSStatus;
import com.ontraport.sdk.objects.fields.CreditCardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordInfo implements Info {

    private int id;
    private int object_id;
    private List<String> keys = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<Integer> types = new ArrayList<>();

    public RecordInfo(int object_id, Map<String, String> data) {
        this(object_id, data, new ArrayList<>(data.keySet()));
    }

    public RecordInfo(int object_id, Map<String, String> data, List<String> order) {
        this.object_id = object_id;

        Meta.Data meta = OntraportApplication.getInstance().getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();

        id = Integer.parseInt(data.get("id"));
        for (String key : order) {
            String alias;
            String type;
            int field_type;
            try {
                alias = fields.get(key).getAlias();
                type = fields.get(key).getType();
            }
            catch (NullPointerException e) {
                System.out.println("Missing: " + key);
                data.remove(key);
                continue;
            }
            if (alias == null || alias.isEmpty()) {
                data.remove(key);
                continue;
            }

            switch (key) {
                case "id":
                case "dla":
                case "date":
                case "spent":
                case "dlm":
                case "ip_addy":
                    field_type = FieldType.DISABLED;
                    break;
                default:
                    // if not disabled, check actual type
                    switch (type) {
                        case "phone":
                            field_type = FieldType.PHONE;
                            break;
                        case "timestamp":
                            field_type = FieldType.TIMESTAMP;
                            break;
                        case "url":
                            field_type = FieldType.URL;
                            break;
                        case "numeric":
                        case "price":
                            field_type = FieldType.NUMERIC;
                            break;
                        case "email":
                            field_type = FieldType.EMAIL;
                            break;
                        case "drop":
                            field_type = FieldType.DROP;
                            break;
                        case "list":
                            field_type = FieldType.LIST;
                            break;
                        case "parent":
                            field_type = FieldType.PARENT;
                            break;
                        case "text":
                        default:
                            field_type = FieldType.TEXT;
                    }
            }

            if (key.equals("fn")) {
                String first = data.get("firstname") == null ? "" : data.get("firstname");
                String last = data.get("lastname") == null ? "" : data.get("lastname");
                data.put(key, first + " " + last);
            }

            try {
                if (key.equals("bulk_mail")) {
                    data.put(key, BulkEmailStatus.getNameFromValue(Integer.parseInt(data.get(key))));
                }

                if (key.equals("bulk_sms")) {
                    data.put(key, BulkSMSStatus.getNameFromValue(Integer.parseInt(data.get(key))));
                }

                if (key.equals("cc_type")) {
                    data.put(key, CreditCardType.getNameFromValue(Integer.parseInt(data.get(key))));
                }
            }
            catch (InvalidValueException e) {
                System.out.println("Received incorrect value from API: " + data.get(key) + " for " + key + ". You should report this");
                continue;
            }

            keys.add(key);
            aliases.add(alias);
            values.add(data.get(key) == null ? "" : data.get(key));
            types.add(field_type);
        }
    }

    public int getId() {
        return id;
    }

    public int getObjectId() {
        return object_id;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getValues() {
        return values;
    }

    public List<Integer> getTypes() {
        return types;
    }

    public String getAlias(int pos) {
        return aliases.get(pos);
    }

    public String getKey(int pos) {
        return keys.get(pos);
    }

    public String getValue(int pos) {
        return values.get(pos);
    }

    public @FieldType
    int getType(int pos) {
        return types.get(pos);
    }

    public String get(String key) {
        int index = getKeys().indexOf(key);
        return getValues().get(index);
    }

    public int size() {
        return getKeys() == null ? 0 : getKeys().size();
    }

    public String toString() {
        return TextUtils.join(" ", getValues());
    }
}
