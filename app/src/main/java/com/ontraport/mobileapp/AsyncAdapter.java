package com.ontraport.mobileapp;

public interface AsyncAdapter<I extends Info> {

    void handleNullResponse();

    void updateInfo(I info);
}
