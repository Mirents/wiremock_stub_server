/*
    Класс для работой с базой данных - открытие, выборка элементов, а так же 
    создание меню.
*/
package io.mirents.OperatorDB;

import io.mirents.Elements.Organization;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.mirents.Elements.FaceUnit;
import io.mirents.Elements.Stock;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DataBaseJSON {
    // Лист с данными организаций
    private List<Organization> data;
    // Название файла с БД для открытия
    private final String FILENAMEJSON = "data.json";
    
    public DataBaseJSON() {
        try {
            GsonBuilder builder = new GsonBuilder();

            // Создание десериализатора для даты, т.к. все элементы файла
            // считавались верно, кроме даты
            builder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type type,
                        JsonDeserializationContext context) throws JsonParseException {
                    LocalDate date = LocalDate.parse(json.getAsString());
                    return date;
                }
            });
            
            // Открытие файла
            Gson gson = builder.create();
            JsonReader jsonReader = new JsonReader(new FileReader(FILENAMEJSON));

            // Считывание данных из файла
            Type type = new TypeToken<List<Organization>>() {}.getType();
            data = gson.fromJson(jsonReader, type);

            // Создание меню и переход к навигации
            ConsoleMenu consoleMenu = new ConsoleMenu();
            consoleMenu.mainMenu(consoleMenu);
            
        } catch (Exception ex) {
            System.out.println("Ошибка открытия файла");
        }
    }
    
    // Метод показа всех организаций в виде: Короткое название и дата основания
    public void showSrtNameAndFndDate() {
        System.out.printf("%-17s | %s\n", "Краткое название", "Дата основания");
        System.out.println("----------------------------------");
        
        // Сортировка списка организаций и их вывод на экран
        data.stream()
            // Сортировка по датам создания организаций
            .sorted((o1, o2) -> o1.getLocalDate().compareTo(o2.getLocalDate()))
            // Выборка коллекции организаций
            .collect(Collectors.toList())
            // Вывод коллекции организаций
            .stream().forEach(
                (o) -> {
                    System.out.println(o.getSrtNameAndFndDate());
                }
            );
    }

    // Метод вывода данных о просроченных акциях
    private void showExpiredStockToday() {
        System.out.printf("%-14s | %-14s | %-14s | %s\n", "Название", "Код ISIN",
                "Дата истечения", "Организация-владелец");
        System.out.println("----------------------------------------------------"
                + "--------------------");
        
        /// Текущая дата
        LocalDate today = LocalDate.now();
        // Двойной цикл для перебора элементов акций и проверки того, что
        // дата продажи меньше текущей даты
        data.stream().forEach(
          (o) -> {
              o.getStock().stream().forEach(
                (s) -> {
                    if(s.getSellDate().compareTo(today) <= 0) {
                        String str = String.format("%s | %-12s", s.getExpiredStock(),
                                o.getName());
                        System.out.println(str);
                    }
                }
              );
          }
        );
        
        // В этом месте я потратил много времени на поиск решения, которое с
        // помощью steam() могло бы посчитать количество элементов, но выделить
        // из списка организаций второй список с акциями я не смог. Поэтому
        // пришлось вынести подсчет отдельно.
        // К тому же в верхний блок нельзя добавлять локальную переменную,
        // а вернуть значение из терминальной операции тоже нельзя.
        // Если есть решение, я был бы рад узнать о нем.
        int expired = 0;
        for(Organization o : data) {
            for(Stock s : o.getStock()) {
                if(s.getSellDate().compareTo(today) <= 0)
                    expired++;
            }
        }
        System.out.println("Общее число просроченных ценных бумаг: " + expired);
    }
    
    // Метод, показывающий все организации, основанные после указанной даты
    public void showOrgAfterDate(LocalDate localDate) {
        System.out.printf("%-17s | %s\n", "Краткое название", "Дата основания");
        System.out.println("----------------------------------");
        
        // Перебор всех организаций и вывод информации о них
        data.stream().forEach(
          (o) -> {
              if(o.getLocalDate().compareTo(localDate) >= 0)
                System.out.println(o.getSrtNameAndFndDate());
          }
        );
    }
    
    // Метод вывода акций по заданной валюте
    public void showStockToUnit(FaceUnit faceunit) {
        System.out.printf("%-12s | %s\n", "Название", "Номер ISIN");
        System.out.println("----------------------------");
        
        // Двойной цикл для прохода по элементам акций и проверки условия
        // соответствия заданной валюте
        data.stream().forEach(
          (o) -> {
              o.getStock().stream().forEach(
                (s) -> {
                    if(s.getFaceUnit() == faceunit)
                        System.out.println(s.getSrtNameAndIsin());
                }
              );
          }
        );
    }
    
    // Внутренний класс для реализации меню возможно не лучшее решение, но
    // для вложенного двухуровнего меню это решение лучше нескольких switch
    private class ConsoleMenu {
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
            showSrtNameAndFndDate();
            System.out.println("Нажмите 'Enter' для возврата в главное меню:");
            Scanner in = new Scanner(System.in);
            String selection = in.nextLine();
            
            // Возврат в главное меню
            return consoleMenu.mainMenu(consoleMenu);
        }
        
        // Второй пункт меню
        private ConsoleMenu submenu2(ConsoleMenu consoleMenu) {
            LocalDate today = LocalDate.now();
            System.out.println("Просроченные ценные бумаги на сегодня " + 
                    today.toString());
            showExpiredStockToday();
            System.out.println("Нажмите любую клавишу");
            Scanner in = new Scanner(System.in);
            String selection = in.nextLine();
            
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
                        
                        showOrgAfterDate(LocalDate.parse(selection, formatter));
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
                        showStockToUnit(FaceUnit.valueOf(selection));
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
}