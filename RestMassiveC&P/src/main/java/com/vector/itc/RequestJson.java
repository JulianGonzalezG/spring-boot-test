package com.vector.itc;

/**
 * Created by jgonzalezg on 27/06/2016.
 */
public class RequestJson {
    private String originalRoute;
    private String newRoute;
    private String configTxt;

    public String getOriginalRoute() {
        return originalRoute;
    }

    public void setOriginalRoute(String originalRoute) {
        this.originalRoute = originalRoute;
    }

    public String getNewRoute() {
        return newRoute;
    }

    public void setNewRoute(String newRoute) {
        this.newRoute = newRoute;
    }

    public String getConfigTxt() {
        return configTxt;
    }

    public void setConfigTxt(String configTxt) {
        this.configTxt = configTxt;
    }
}
