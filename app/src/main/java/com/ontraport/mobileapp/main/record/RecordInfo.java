package com.ontraport.mobileapp.main.record;

import com.ontraport.mobileapp.AbstractInfo;

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
