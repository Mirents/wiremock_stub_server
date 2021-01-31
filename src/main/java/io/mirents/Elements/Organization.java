/*
    Класс организации со списком акций, принадлежащих компании
*/
package io.mirents.Elements;

import java.time.LocalDate;
import java.util.ArrayList;

public class Organization {
    
    private String shortname;
    private String name;
    private String adress;
    private String phonenumber;
    private String inn;
    private String ogrn;
    private LocalDate founddate;
    private ArrayList<Stock> stock;
    
    public Organization(String shortname, String name, String adress,
            String phonenumber, String inn, String ogrn, LocalDate founddate,
            ArrayList<Stock> stock) {
        this.shortname = shortname;
        this.name = name;
        this.adress = adress;
        this.phonenumber = phonenumber;
        this.inn = inn;
        this.ogrn = ogrn;
        this.founddate = founddate;
        this.stock = stock;
    }
    
    @Override
    public String toString() {
        return shortname + ", " + stock.toString();
    }
    
    public ArrayList<Stock> getStock() {
        return stock;
    }
    
    public LocalDate getLocalDate() {
        return founddate;
    }
    
    public String getSrtNameAndFndDate() {
        return String.format("%-17s | %s", shortname, founddate);
    }
    
    public String getName() {
        return name;
    }
}
