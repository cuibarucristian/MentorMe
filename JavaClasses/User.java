package com.example.cristian.mentorme.JavaClasses;

public class User
{

    String biography;
    String firstname;
    String image;
    String secondname;
    int semester;
    String specialization;
    String status; //Role




    public User(String biography, String first_name, String image, String second_Name, int semester, String specialization, String status)
    {
        this.biography = biography;
        this.firstname = first_name;
        this.image = image;
        this.secondname = second_Name;
        this.semester = semester;
        this.specialization = specialization;
        this.status = status;
    }

    public String getBiography()
    {
        return biography;
    }

    public void setBiography(String biography)
    {
        this.biography = biography;
    }

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getSecondname()
    {
        return secondname;
    }

    public void setSecondname(String secondname)
    {
        this.secondname = secondname;
    }

    public int getSemester()
    {
        return semester;
    }

    public void setSemester(int semester)
    {
        this.semester = semester;
    }

    public String getSpecialization()
    {
        return specialization;
    }

    public void setSpecialization(String specialization)
    {
        this.specialization = specialization;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }



    public User()
    {

    }






}
