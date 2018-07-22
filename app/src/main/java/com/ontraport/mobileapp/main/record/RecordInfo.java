package com.ontraport.mobileapp.main.record;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.ontraport.mobileapp.Info;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.utils.FieldType;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.InvalidValueException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.models.fieldeditor.ObjectSection;
import com.ontraport.sdk.objects.fields.BulkEmailStatus;
import com.ontraport.sdk.objects.fields.BulkSMSStatus;
import com.ontraport.sdk.objects.fields.CreditCardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordInfo implements Info, Parcelable {

    private int id;
    private int object_id;
    private List<String> keys = new ArrayList<>();
    private List<String> aliases = new ArrayList<>();
    private List<String> values = new ArrayList<>();
    private List<Integer> types = new ArrayList<>();
    private Map<String, String> parent_object_ids = new HashMap<>();

    public RecordInfo(int object_id, Map<String, String> data) {
        this(object_id, data, new ArrayList<>(data.keySet()));
    }

    public RecordInfo(int object_id, Map<String, String> data, List<String> order) {
        this.object_id = object_id;

        List<ObjectSection> sections = OntraportApplication.getInstance().getFieldSections(object_id);
        Meta.Data meta = OntraportApplication.getInstance().getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();

        id = Integer.parseInt(data.get(FieldUtils.findIdField(data)));
        for (String key : order) {
            String alias = null;
            String type = "";
            int field_type;
            try {
                alias = fields.get(key).getAlias();
                type = fields.get(key).getType();
                if (fields.get(key).hasParent()) {
                    parent_object_ids.put(key, fields.get(key).getParent());
                }
                if (alias == null || alias.isEmpty()) {
                    data.remove(key);
                    continue;
                }
            }
            catch (NullPointerException e) {
                if (!FieldUtils.isSpecialField(key)) {
                    System.out.println("Missing: " + key);
                    data.remove(key);
                    continue;
                }
            }

            switch (key) {
                case "dla":
                case "date":
                case "dlm":
                    data.put(key, FieldUtils.convertDate(data.get(key)));
                case "id":
                case "spent":
                case "ip_addy":
                case "bulk_mail":
                case "bulk_sms":
                    field_type = FieldType.DISABLED;
                    break;
                default:
                    // if not disabled, check actual type
                    switch (type) {
                        case "phone":
                            field_type = FieldType.PHONE;
                            break;
                        case "timestamp":
                        case "fulldate":
                            data.put(key, FieldUtils.convertDate(data.get(key)));
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
                        case "subscription":
                        case "list":
                            field_type = FieldType.LIST;
                            break;
                        case "parent":
                            field_type = FieldType.PARENT;
                            break;
                        case "mergefield":
                            field_type = FieldType.TEXT;
                            if (key.equals("fn")) {
                                String first = data.get("firstname") == null ? "" : data.get("firstname");
                                String last = data.get("lastname") == null ? "" : data.get("lastname");
                                data.put(key, first + " " + last);
                            }
                            break;
                        case "text":
                        default:
                            field_type = FieldType.TEXT;
                    }
            }

            try {
                data.put(key, convertIdToName(key, data.get(key)));
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

    public Map<String, String> getParentIds() {
        return parent_object_ids;
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

    public void setValue(int pos, String value) {
        values.set(pos, value);
    }

    public void setValue(String key, String value) {
        values.set(indexOf(key), value);
    }

    public @FieldType
    int getType(int pos) {
        return types.get(pos);
    }

    public String getParentId(String key) {
        return parent_object_ids.get(key);
    }

    public String get(String key) {
        int index = indexOf(key);
        return getValues().get(index);
    }

    public int indexOf(String key) {
        return getKeys().indexOf(key);
    }

    public int size() {
        return getKeys() == null ? 0 : getKeys().size();
    }

    public String toString() {
        OntraportApplication app = OntraportApplication.getInstance();
        String object_label = app.getObjectLabel(getObjectId());
        if (object_label == null) {
            return TextUtils.join(" ", getValues());
        }
        return FieldUtils.replaceMergeFields(object_label, getAliases(), getValues());
    }

    private String convertIdToName(String key, String val) throws InvalidValueException {
        if (key.equals("bulk_mail")) {
            return BulkEmailStatus.getNameFromValue(Integer.parseInt(val));
        }

        if (key.equals("bulk_sms")) {
            return BulkSMSStatus.getNameFromValue(Integer.parseInt(val));
        }

        if (key.equals("cc_type")) {
            return CreditCardType.getNameFromValue(Integer.parseInt(val));
        }
        return val;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(object_id);
        dest.writeList(keys);
        dest.writeList(aliases);
        dest.writeList(values);
        dest.writeList(types);
        dest.writeMap(parent_object_ids);
    }

    public static final Parcelable.Creator<RecordInfo> CREATOR
            = new Parcelable.Creator<RecordInfo>() {
        public RecordInfo createFromParcel(Parcel in) {
            return new RecordInfo(in);
        }

        public RecordInfo[] newArray(int size) {
            return new RecordInfo[size];
        }
    };

    private RecordInfo(Parcel in) {
        id = in.readInt();
        object_id = in.readInt();
        in.readList(keys, String.class.getClassLoader());
        in.readList(aliases, String.class.getClassLoader());
        in.readList(values, String.class.getClassLoader());
        in.readList(types, Integer.class.getClassLoader());
        in.readMap(parent_object_ids, String.class.getClassLoader());
    }

    class RecordSection {

    }

}
