package com.dawnsoft.dawn.audiotask.model;

public class ContactBook {
    private String name;
    private String phoneNumber;
    private boolean isSelected;

    public ContactBook(String name, String phoneNumber, boolean isSelected) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean getIsSelected(){
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
