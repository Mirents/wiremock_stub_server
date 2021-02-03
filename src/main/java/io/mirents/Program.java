/*
    Основная программа
*/
package io.mirents;

import io.mirents.service.ConsoleMenu;

public class Program {
    static ConsoleMenu consoleMenu;
    
    public static void main(String[] args) {
        ConsoleMenu consoleMenu = new ConsoleMenu();
        consoleMenu.start();
    }
}
