package com.wjg53.accountingapp;

public class BillsItem {
    public int[] images = {R.mipmap.sort_call_pay, R.mipmap.sort_shopping ,R.mipmap.sort_creaditcard,R.mipmap.sort_e_product,
            R.mipmap.sort_food,R.mipmap.sort_housing,R.mipmap.sort_jiaotong,R.mipmap.sort_kid,
            R.mipmap.sort_learning,R.mipmap.sort_medical,R.mipmap.sort_other,R.mipmap.sort_play,
            R.mipmap.sort_shouxufei,R.mipmap.sort_sport,R.mipmap.sort_travel};
    public String[] items = {"Telephone fee", "Shopping", "Card Bill", "E-Product", "Food", "House", "Traffic",
            "Children", "Learning", "Medical" , "Other" ,"Play", "Charge", "Sport", "Travel"};
    public int[] inComeimages = {R.mipmap.sort_add, R.mipmap.sort_deposit, R.mipmap.sort_salary, R.mipmap.sort_other};
    public String[] inComeitems = {"Receive", "Deposit", "Salary", "Other"};


    public int[] getImage(){
        return images;
    }
    public String[] getItems(){
        return items;
    }
    public int setImage(int id){
        return images[id];
    }
    public String setItem(int id){
        return items[id];
    }
    public int[] inComeimages(){
        return inComeimages;
    }
    public String[] getinComeitems(){
        return inComeitems;
    }
    public int setinComeimages(int id){
        return inComeimages[id];
    }
    public String setinComeitems(int id){
        return inComeitems[id];
    }

}
