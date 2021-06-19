package com.example;

public class Rectangle {
    private String id;
    private String name;
    private Integer width;
    private Integer length;
    private String colour;
    private Integer area;

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getLength() {
        return this.length;
    }

    public String getColour() {
        return this.colour;
    }

    public Integer getArea() {
        return this.area;
    }

    public void setID(String f) {
        this.id = f;
    }

    public void setName(String f) {
        this.name = f;
    }

    public void setWidth(Integer f) {
        this.width = f;
    }

    public void setLength(Integer f) {
        this.length = f;
    }

    public void setColour(String l) {
        this.colour = l;
    }

    public void setArea(Integer f) {
        this.area = f;
    }

}
