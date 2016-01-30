package com.dc.esb.servicegov.util;

/**
 * Created by Administrator on 2015/7/30.
 */
public class Counter {
    private int countValue;

    public Counter(){countValue = 0;}
    public Counter(int count){countValue = count;}

    public void increment(){
        ++countValue;
    }

    public boolean decrement(){
        if(countValue == 0) return false;
        else --countValue;
        return true;
    }

    public void reset(){
        countValue = 0;
    }

    public void set(int count){
        countValue = count;
    }

    public int getCount(){return countValue;}
}
