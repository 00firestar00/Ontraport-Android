package com.ontraport.mobileapp.adapters;

import java.util.Map;

public class CollectionInfo extends AbstractInfo {

    private Map<String, String>[] data;
    private String[] list_fields;

    public CollectionInfo(Map<String, String>[] data, String[] list_fields) {
        this.data = data;
        this.list_fields = list_fields;
    }

    public Map<String, String>[] getData() {
        return data;
    }

    public String[] getListFields() {
        return list_fields;
    }
}
