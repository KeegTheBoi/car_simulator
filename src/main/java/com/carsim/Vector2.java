package com.carsim;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Vector2 {
    
    private DoubleProperty y;
    private DoubleProperty x;

    public Vector2(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }

    public Vector2 add(Vector2 vec) {
        return new Vector2(x.get() + vec.x.get(), y.get() + vec.y.get());
    }

    public Vector2 scalar(double scale) {
        return new Vector2(x.get() * scale, x.get() * scale);
    }

    public void setX(double x) {
        this.x.set(x);
    }

    public DoubleProperty getYProprety() {
        return y;
    }

    public void setY(double y) {
        this.y.set(y);
    }

    public DoubleProperty getXProprety() {
        return x;
    }
}
