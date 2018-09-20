package com.ontraport.mobileapp.main.record;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import com.ontraport.mobileapp.Info;
import com.ontraport.mobileapp.OntraportApplication;
import com.ontraport.mobileapp.utils.FieldType;
import com.ontraport.mobileapp.utils.FieldUtils;
import com.ontraport.sdk.exceptions.InvalidValueException;
import com.ontraport.sdk.http.Meta;
import com.ontraport.sdk.models.fieldeditor.ObjectField;
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
    private SparseArray<RecordSection> sections = new SparseArray<>();
    private Map<String, String> key_to_value = new HashMap<>();
    private Map<String, String> parent_object_ids = new HashMap<>();

    public RecordInfo(int object_id, Map<String, String> data) {
        this(object_id, data, new ArrayList<>(data.keySet()));
    }

    public RecordInfo(int object_id, Map<String, String> data, List<String> order) {
        this.object_id = object_id;

        Meta.Data meta = OntraportApplication.getInstance().getMetaData(object_id);
        Map<String, Meta.Field> fields = meta.getFields();
        id = Integer.parseInt(data.get(FieldUtils.findIdField(data)));
        for (String key : order) {
            String alias = null;
            try {
                alias = fields.get(key).getAlias();
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

            try {
                if (key.equals("fn")) {
                    String first = data.get("firstname") == null ? "" : data.get("firstname");
                    String last = data.get("lastname") == null ? "" : data.get("lastname");
                    data.put(key, first + " " + last);
                }
                else {
                    data.put(key, convertIdToName(key, data.get(key)));
                }
            }
            catch (InvalidValueException e) {
                System.out.println("Received incorrect value from API: " + data.get(key) + " for " + key + ". You should report this");
                continue;
            }
            key_to_value.put(key, data.get(key) == null ? "" : data.get(key));
            keys.add(key);
            aliases.add(alias);
            values.add(data.get(key) == null ? "" : data.get(key));
        }
    }

    public int getId() {
        return id;
    }

    public int getObjectId() {
        return object_id;
    }

    public List<String> getValues() {
        return values;
    }

    public Map<String, String> getParentIds() {
        return parent_object_ids;
    }

    public String getAlias(int pos) {
        return aliases.get(pos);
    }

    public String getValue(int pos) {
        return values.get(pos);
    }

    public void setValue(String key, String value) {
        values.set(keys.indexOf(key), value);
    }

    public String get(String key) {
        int index = keys.indexOf(key);
        return getValues().get(index);
    }

    public RecordSection getSection(int id) {
        if (sections.size() == 0) {
            List<ObjectSection> object_sections = OntraportApplication.getInstance().getFieldSections(object_id);
            for (ObjectSection section : object_sections) {
                sections.append(section.getId(), new RecordSection(section, key_to_value));
            }
        }
        return sections.get(id);
    }

    public int size() {
        return keys == null ? 0 : keys.size();
    }

    public String toString() {
        OntraportApplication app = OntraportApplication.getInstance();
        String object_label = app.getObjectLabel(getObjectId());
        if (object_label == null) {
            return TextUtils.join(" ", getValues());
        }
        return FieldUtils.replaceMergeFields(object_label, aliases, getValues());
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
        in.readMap(parent_object_ids, String.class.getClassLoader());
    }

    class RecordSection {
        ObjectSection section;
        List<RecordField> fields = new ArrayList<>();

        RecordSection(ObjectSection section, Map<String, String> key_value_pairs) {
            this.section = section;
            for (Map.Entry<String, String> entry : key_value_pairs.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                ObjectField field = section.getField(key);
                if (field == null) {
                    continue;
                }
                fields.add(new RecordField(field, value));
            }
        }

        public RecordField getField(int pos) {
            return fields.get(pos);
        }

        public int size() {
            return fields.size();
        }
    }

    class RecordField {
        ObjectField field;
        String value;

        RecordField(ObjectField field, String value) {
            this.field = field;
            this.value = value;
        }

        public @FieldType
        int getFieldType() {
            if (!field.isEditable()) {
                return FieldType.DISABLED;
            }

            com.ontraport.sdk.objects.fields.FieldType ft = field.getType();
            if (ft != null) {
                return ft.ordinal();
            }

            System.out.println("unknown field type for " + field);
            return FieldType.DISABLED;
        }

        public String getAlias() {
            return field.getAlias();
        }

        public String getValue() {
            return value;
        }

        public String getField() {
            return field.getField();
        }
    }
}
