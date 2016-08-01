package com.vector.itc;

/**
 * Created by jgonzalezg on 27/06/2016.
 */
public class RequestMergeJson {
    private String originalRoute;
    private String searchRoute;
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

    public String getSearchRoute() {return searchRoute;}

    public void setSearchRoute(String searchRoute) {this.searchRoute = searchRoute;}
}
