package com.tohan.veriparkcasestudy.Api.Models;

public class HandshakeRequestModel {

    String deviceId;
    String systemVersion;
    String platformName;
    String deviceModel;
    String manifacturer;

    public HandshakeRequestModel() {

    }
    public HandshakeRequestModel(String deviceId, String systemVersion, String platformName, String deviceModel, String manifacturer) {
        this.deviceId = deviceId;
        this.systemVersion = systemVersion;
        this.platformName = platformName;
        this.deviceModel = deviceModel;
        this.manifacturer = manifacturer;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getPlatformName() {
        return this.platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSystemVersion() {
        return this.systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getManifacturer() {
        return this.manifacturer;
    }

    public void setManifacturer(String manifacturer) {
        this.manifacturer = manifacturer;
    }

}
