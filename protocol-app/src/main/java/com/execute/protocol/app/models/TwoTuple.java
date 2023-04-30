package com.execute.protocol.app.models;


public class TwoTuple<F,S> {
    public F first;
    public S second;

    public TwoTuple(F first, S second) {
        this.first = first;
        this.second = second;
    }
    public static <F, S> TwoTuple<F, S> of(F first, S second){
        return new TwoTuple<>(first, second);
    }
}
