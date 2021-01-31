/*
    Перечисление валют для ценных бумаг
*/
package io.mirents.Elements;

public enum FaceUnit {
        USD("Доллар США"),
        EUR("Евро"),
        GBP("Фунт стерлингов Велико­британии"),
        JPY("Японская йена"),
        CHF("Швейцарский франк"),
        CNY("Китайский юань женьминьби"),
        RUB("Российский рубль");
        
        private final String title;

        FaceUnit(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
        
        @Override
        public String toString() {
            return title;
        }
    };
