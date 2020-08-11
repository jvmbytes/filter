package com.jbytes.spy.service;

public class TuserService {

    public Tuser getUser(String name){
        return new Tuser(name);
    }
}
