package com.example.agricare;

public class SCHEME_CLASS {

    private String DES ;
    private String URL ;

    public SCHEME_CLASS(String des1,String url1){
        DES=des1;
        URL=url1;
    }

    public String get_Des(){
        return DES ;
    }

    public String get_Url(){
        return URL ;
    }


}
