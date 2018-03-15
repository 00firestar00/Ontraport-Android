package com.ontraport.mobileapp;

public interface AsyncAdapter<T extends AbstractInfo> {

    void handleNullResponse();

    void updateInfo(T info);
}
