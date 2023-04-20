package ru.kpfu.itis.belskaya.models;

/**
 * @author Elizaveta Belskaya
 */
public abstract class User {

    public abstract String getEmail();

    public abstract String getPhone();

    public abstract void setAccount( Account account );

}
