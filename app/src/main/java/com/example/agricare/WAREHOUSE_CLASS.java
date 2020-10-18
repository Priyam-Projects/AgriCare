package com.example.agricare;

public class WAREHOUSE_CLASS {

    private String location ;
    private int total ;
    private int avail ;
    private int distance ;

    public WAREHOUSE_CLASS(String l1,int t1,int a1,int d1){
        location=l1;
        total=t1;
        avail=a1 ;
        distance=d1;

    }

    public String getLocation(){
        return location;
    }
    public int getTotal(){
        return total ;
    }
    public int getAvail(){
        return avail ;
    }
    public int getDistance(){return distance;}


}
