package io.mirents.service;

import io.mirents.model.FaceUnit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleMenu {
    private DataBaseWorker dataBaseWorker;
    
    public void start() {
        dataBaseWorker = new DataBaseWorker();
        if(!dataBaseWorker.isDataEmpty())
            mainMenu(this);
    }

    // Главное меню
    private ConsoleMenu mainMenu(ConsoleMenu console) {
        int selection = 0;
        
        do {
            System.out.println("Меню");
            System.out.println("[1] - Вывести список всех компании в формате"
                    + " «Краткое название» – «Дата основания»");
            System.out.println("[2] - Вывести просроченные ценные бумаги");
            System.out.println("[3] - Ввести дату и показать все организации,"
                    + " основанные после введенной даты");
            System.out.println("[4] - Ввести валюту и показать ценные бумаги,"
                    + " содержащие введенную валюту");
            System.out.println("[5] - Выход");
            System.out.println("Введите пункт меню:");
            
            Scanner in = new Scanner(System.in);
            try {
                selection = in.nextInt();
            } catch (InputMismatchException ex) {}

            switch (selection) {
                case 1: return console.submenu1(console);
                case 2: return console.submenu2(console);
                case 3: return console.submenu3(console);
                case 4: return console.submenu4(console);
                case 5: return console;
                default:
                    System.out.println("Повторите ввод");
            }
        } while (selection != 5);
        return console;
    }

    // Первый пункт меню
    private ConsoleMenu submenu1(ConsoleMenu consoleMenu) {
        System.out.println("Список всех компаний:");
        dataBaseWorker.showSrtNameAndFndDate();
        System.out.println("Нажмите 'Enter' для возврата в главное меню:");
        Scanner in = new Scanner(System.in);
        in.nextLine();

        // Возврат в главное меню
        return consoleMenu.mainMenu(consoleMenu);
    }

    // Второй пункт меню
    private ConsoleMenu submenu2(ConsoleMenu consoleMenu) {
        LocalDate today = LocalDate.now();
        System.out.println("Просроченные ценные бумаги на сегодня " + 
                today.toString());
        dataBaseWorker.showExpiredStockToday();
        System.out.println("Нажмите любую клавишу");
        Scanner in = new Scanner(System.in);
        in.nextLine();
        
        // Возврат в главное меню
        return consoleMenu.mainMenu(consoleMenu);
    }
        
    // Третий пункт меню
    private ConsoleMenu submenu3(ConsoleMenu consoleMenu) {
        String selection = "";
        
        do {
            System.out.println("Введите дату в формате «ДД.ММ.ГГГГ», "
                    + "«ДД.ММ.ГГ», «ДД/ММ/ГГГГ» и «ДД/ММ/ГГ»");
            System.out.println("Или нажмите 'Enter' для возврата в главное меню");
            
            Scanner in = new Scanner(System.in);
            try {
                selection = in.nextLine();
                
                if(!"".equals(selection)) {
                    System.out.println("Организации, основанные после "
                            + "введенной даты(включительно):");
                        
                    // Форматирование строки в дату
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern("[dd.MM.][dd/MM/]")
                        .optionalStart()
                        .appendPattern("uuuu")
                        .optionalEnd()
                        .optionalStart()
                        .appendValueReduced(ChronoField.YEAR, 2, 2, 1970)
                        .optionalEnd()
                        .toFormatter();
                    
                    dataBaseWorker.showOrgAfterDate(LocalDate.parse(selection, formatter));
                    return consoleMenu.submenu3(consoleMenu);
                }
            } catch (DateTimeParseException ex) {
                System.out.println("Повторите ввод:");
            }
        } while(!"".equals(selection));
            
        // Возврат в главное меню
        return consoleMenu.mainMenu(consoleMenu);
    }
        
    // Четвертый пункт меню
    private ConsoleMenu submenu4(ConsoleMenu consoleMenu) {
        String selection = "";
        
        do {
            System.out.println("Введите валюту - USD, EUR, GBP, JPY, CHF, CNY, RUB");
            System.out.println("Или нажмите 'Enter' для возврата в главное меню");
            
            Scanner in = new Scanner(System.in);
            try {
                selection = in.nextLine();
                
                if(!"".equals(selection)) {
                    System.out.println("Ценные бумаги в выбранной валюте:");
                    dataBaseWorker.showStockToUnit(FaceUnit.valueOf(selection));
                    return consoleMenu.submenu4(consoleMenu);
                }
            } catch (InputMismatchException | IllegalArgumentException ex) {
                System.out.println("Повторите ввод:");
            }
        
        } while(!"".equals(selection));
        // Возврат в главное меню
        return consoleMenu.mainMenu(consoleMenu);
    }
}
