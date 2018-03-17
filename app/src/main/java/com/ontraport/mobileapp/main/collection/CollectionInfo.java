package com.ontraport.mobileapp.main.collection;

import com.ontraport.mobileapp.AbstractInfo;
import com.ontraport.mobileapp.main.record.RecordInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CollectionInfo extends AbstractInfo {

    private int object_id;
    private List<RecordInfo> records = new ArrayList<>();
    private List<String> list_fields= new ArrayList<>();
    private int count = 0;
    private boolean forced = false;

    public CollectionInfo() {
    }

    public CollectionInfo(int object_id, Map<String, String>[] data, String[] list_fields, int count) {
        this.object_id = object_id;
        this.list_fields = sanitizeFields(list_fields);
        this.count = count;
        for (Map<String, String> record : data) {

            if (getListFields().contains("fn") && !record.containsKey("fn")) {
                record.put("fn", "");
            }
            records.add(new RecordInfo(object_id, record, getListFields()));
        }
    }

    public CollectionInfo force(boolean force) {
        this.forced = force;
        return this;
    }

    public boolean isForced() {
        return forced;
    }

    public RecordInfo get(int pos) {
        return records.get(pos);
    }

    public List<RecordInfo> getData() {
        return records;
    }

    public List<String> getListFields() {
        return list_fields;
    }

    public int getListFieldCount() {
        return list_fields.size();
    }

    public int getCount() {
        return count;
    }

    public int getObjectId() {
        return object_id;
    }

    public void add(RecordInfo new_record) {
        records.add(new_record);
    }

    public void addAll(List<RecordInfo> new_records) {
        records.addAll(new_records);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public int size() {
        return records.size();
    }

    private List<String> sanitizeFields(String[] list_fields) {
        ArrayList<String> fields = new ArrayList<>(Arrays.asList(list_fields));
        fields.remove("");
        return fields;
    }
}
