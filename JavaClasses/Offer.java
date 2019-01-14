package com.example.cristian.mentorme.JavaClasses;

public class Offer
{
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    public String getImageURI()
    {
        return imageURI;
    }

    public void setImageURI(String imageURI)
    {
        this.imageURI = imageURI;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public myDate getDeadline()
    {
        return deadline;
    }

    public void setDeadline(myDate deadline)
    {
        this.deadline = deadline;
    }

    public Offer(String title, String description, int price, String firstName, String secondName, String imageURI, String role, myDate deadline, String uid)
    {
        this.title = title;
        this.description = description;
        this.price = price;
        this.firstName = firstName;
        this.secondName = secondName;
        this.imageURI = imageURI;
        this.role = role;
        this.deadline = deadline;
        this.uid = uid;
    }

    public Offer()
    {

    }

    private String title;
    private String description;
    private int price;
    private String firstName;
    private String secondName;
    private String imageURI;
    private String role;
    private myDate deadline;
    private String uid;


    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }
}
