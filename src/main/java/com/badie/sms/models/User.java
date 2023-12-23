package com.badie.sms.models;
import com.badie.sms.utils.Gender;
public class User {
    public String firstName;
    public String lastName;
    public String pass;
    public String email;
    public int id;
    public Gender gender;
    public String mobileNumber;
    public User(){

    }
    public User(User user){
        this.gender = user.gender;
        this.firstName = user.firstName;
        this.lastName = user.lastName;;
        this.id = user.id;;
        this.pass = user.pass;;
        this.email = user.email;;
        this.mobileNumber= user.mobileNumber;
    }
    @Override
    public String toString() {
        if (this.firstName != null && this.lastName != null)
            return this.firstName + " " + this.lastName;
        else
            return null;
    }
}
