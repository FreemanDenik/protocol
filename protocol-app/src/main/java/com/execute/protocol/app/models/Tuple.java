package com.execute.protocol.app.models;

import lombok.Data;


public class Tuple<F,S> {
    public F first;
    public S second;

    public Tuple(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
