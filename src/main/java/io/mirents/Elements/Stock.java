/*
    Класс акции
*/
package io.mirents.Elements;

import java.time.LocalDate;

public class Stock {
    private String shortname;
    private String name;
    private String boardid;
    private String isin;
    private FaceUnit faceunit;
    private LocalDate issuedate;
    private LocalDate selldate;
    private int count;
    private double price;
    
    public Stock(String shortname, String name, String boardid, String isin,
            FaceUnit faceunit, LocalDate issuedate, LocalDate selldate,
            int count, double price) {
        this.shortname = shortname;
        this.name = name;
        this.boardid = boardid;
        this.isin = isin;
        this.faceunit = faceunit;
        this.issuedate = issuedate;
        this.selldate = selldate;
        this.count = count;
        this.price = price;
    }
    
    @Override
    public String toString() {
        return shortname + " - " + isin + " - " + selldate;
    }
    
    public FaceUnit getFaceUnit() {
        return faceunit;
    }
    
    public String getSrtNameAndIsin() {
        return String.format("%-12s | %s", shortname, isin);
    }
    
    public LocalDate getSellDate() {
        return selldate;
    }
    
    public String getExpiredStock() {
        return String.format("%-14s | %-14s | %-14s", shortname, isin, selldate);
    }
}
