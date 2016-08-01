package com.vector.itc;

import java.util.List;

/**
 * Created by jgonzalezg on 27/06/2016.
 */
public class ResponseJson {
    private String originalRoute;
    private String newRoute;
    private String configTxt;
    private List<String> noCopiados;
    private int totalAcopiar;
    private int totalCopiados;

    public int getTotalAcopiar() {
        return totalAcopiar;
    }

    public void setTotalAcopiar(int totalAcopiar) {
        this.totalAcopiar = totalAcopiar;
    }

    public int getTotalCopiados() {
        return totalCopiados;
    }

    public void setTotalCopiados(int totalCopiados) {
        this.totalCopiados = totalCopiados;
    }

    public List<String> getNoCopiados() {
        return noCopiados;
    }

    public void setNoCopiados(List<String> noCopiados) {
        this.noCopiados = noCopiados;
    }

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
