package com.example.cristian.mentorme.JavaClasses;

public class Message
{

    public String getMessage()
    {
        return message;
    }

    public Message()
    {

    }


    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    private String message;
    private String from;

    public Message(String message, String from)
    {
        this.message = message;
        this.from = from;
    }




}
