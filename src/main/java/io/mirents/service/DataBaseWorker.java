/*
    Класс для работой с базой данных - открытие, выборка элементов, а так же 
    создание меню.
*/
package io.mirents.service;

import io.mirents.model.Organization;
import io.mirents.model.FaceUnit;
import io.mirents.model.Stock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataBaseWorker {
    // Лист с данными организаций
    private List<Organization> dataBase = new ArrayList<>();
    private final String FILENAMEJSON = "data.json";
    
    // Конструктор базы данных организаций
    public DataBaseWorker() {
        FileJsonOpener fileJsonOpener = new FileJsonOpener();
        dataBase = fileJsonOpener.readJsonFile(FILENAMEJSON);
    }

    // Проверка наличия элементов в списке с данными
    public boolean isDataEmpty() {
        if(dataBase == null)
            return true;
        else
            return false;
    }
            
    // Метод показа всех организаций в виде: Короткое название и дата основания
    public void showSrtNameAndFndDate() {
        System.out.printf("%-17s | %s\n", "Краткое название", "Дата основания");
        System.out.println("----------------------------------");
        
        dataBase.stream().forEach(System.out::println);
    }

    // Метод вывода данных о просроченных акциях
    public void showExpiredStockToday() {
        System.out.printf("%-14s | %-14s | %-14s\n", "Название", "Код ISIN",
                "Дата истечения");
        System.out.println("----------------------------------------------------"
                + "--------------------");
        /// Текущая дата
        LocalDate today = LocalDate.now();

        // Выборка списков акций из каждой организации с последующим фильтром
        // по дате в сравнении с сегодняшней датой
        List<Stock> stock = dataBase.stream()
            .flatMap(e -> e.getStock().stream())
            .filter(s -> s.getSellDate().isBefore(today))
            .collect(Collectors.toList());
        
        // Этот вывод можно было поместить в верхний стим, но в т.к.
        // count и forEach терминальные операции - пришлось выбрать одну
        // из них, а вторую реализовать отдельно
        stock.forEach(System.out::println);
        System.out.println("Общее число просроченных ценных бумаг: " + stock.size());
    }
    
    // Метод, показывающий все организации, основанные после указанной даты
    public void showOrgAfterDate(LocalDate localDate) {
        System.out.printf("%-17s | %s\n", "Краткое название", "Дата основания");
        System.out.println("----------------------------------");
        
        dataBase.stream()
            .filter(s -> s.getLocalDate().isAfter(localDate))
            .forEach(System.out::println);
    }
    
    // Метод вывода акций по заданной валюте
    public void showStockToUnit(FaceUnit faceunit) {
        System.out.printf("%-12s | %s\n", "Название", "Номер ISIN");
        System.out.println("----------------------------");

        dataBase.stream()
            .flatMap(e -> e.getStock().stream())
            .filter(s -> s.getFaceUnit() == faceunit)
            .forEach(l -> System.out.println(l.getSrtNameAndIsin()));
    }
}