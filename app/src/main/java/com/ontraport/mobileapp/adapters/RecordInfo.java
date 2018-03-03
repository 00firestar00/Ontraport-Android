package com.ontraport.mobileapp.adapters;

import java.util.Map;

public class RecordInfo extends AbstractInfo {

    private Map<String, String> data;

    public RecordInfo(Map<String, String> data) {
        this.data = data;
    }

    public Map<String, String> getData() {
        return data;
    }

}
