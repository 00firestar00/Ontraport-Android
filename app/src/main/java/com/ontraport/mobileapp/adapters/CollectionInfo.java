package com.ontraport.mobileapp.adapters;

import java.util.Map;

public class CollectionInfo extends AbstractInfo {

    private Map<String, String>[] data;
    private String[] list_fields;
    private int count;
    private boolean forced;

    public CollectionInfo(Map<String, String>[] data, String[] list_fields, int count) {
        this.data = data;
        this.list_fields = list_fields;
        this.count = count;
    }

    public CollectionInfo force(boolean force) {
        this.forced = force;
        return this;
    }

    public boolean isForced() {
        return forced;
    }

    public Map<String, String>[] getData() {
        return data;
    }

    public String[] getListFields() {
        return list_fields;
    }

    public int getCount() {
        return count;
    }
}
