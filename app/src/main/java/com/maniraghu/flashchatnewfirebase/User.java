package com.maniraghu.flashchatnewfirebase;

public class User {

    public String gender,companyname,companymail,region,dob;

    public User()
    {}


    public User(String gender, String companyname, String companymail, String region,String dob) {
        this.gender = gender;
        this.companyname = companyname;
        this.companymail = companymail;
        this.region = region;
        this.dob=dob;
    }
}
