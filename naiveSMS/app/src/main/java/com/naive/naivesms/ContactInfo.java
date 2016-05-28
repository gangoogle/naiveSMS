package com.naive.naivesms;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

public class ContactInfo implements Serializable, Comparable<ContactInfo> {
    private String name = "";
    private String phone = "";
    private String email = "";

    private static Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int compareTo(ContactInfo another) {
        return com.compare(this.name, another.getName());
    }


}
