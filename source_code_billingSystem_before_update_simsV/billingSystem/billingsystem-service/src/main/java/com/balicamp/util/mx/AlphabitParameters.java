package com.balicamp.util.mx;

public class AlphabitParameters {
    
    private String name;
    private String type;
    private int position;
    private int length;

    public AlphabitParameters(String name, String type, int position, int length) {
        this.name = name;
        this.type = type;
        this.position = position;
        this.length = length;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

}