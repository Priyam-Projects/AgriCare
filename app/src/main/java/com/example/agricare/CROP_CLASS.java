package com.example.agricare;

public class CROP_CLASS {

    private String name ;
    private double live_price ;
    private double expected_price ;
    private double percentage_of_farmer ;


    public CROP_CLASS(String name1,double live_price1,double expected_price1,double percentage_of_farmer1){
        name=name1;
        live_price=live_price1;
        expected_price=expected_price1;
        percentage_of_farmer=percentage_of_farmer1;
    }

    public String getName(){
        return name ;
    }
    public double getLive_price(){
        return live_price;
    }

    public double getExpected_price() {
        return expected_price;
    }

    public double getPercentage_of_farmer() {
        return percentage_of_farmer;
    }

    public void setName(String name1){
        name=name1;

    }
    public void setLive_price(double live_price1){
        live_price=live_price1;
    }
    public void setExpected_price(double expected_price1){
        expected_price=expected_price1;
    }
    public void setPercentage_of_farmer(double percentage_of_farmer1){
        percentage_of_farmer=percentage_of_farmer1;
    }
}
