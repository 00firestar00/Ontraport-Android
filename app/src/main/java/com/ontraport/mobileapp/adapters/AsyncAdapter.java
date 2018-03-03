package com.ontraport.mobileapp.adapters;

public interface AsyncAdapter<T extends AbstractInfo> {

    void handleNullResponse();

    void updateInfo(T info);
}
