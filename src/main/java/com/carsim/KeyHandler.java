package com.carsim;

public interface KeyHandler<K> {

    void trigger(K event);

    void execute();
}
