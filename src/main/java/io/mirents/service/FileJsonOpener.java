package io.mirents.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import io.mirents.model.Organization;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileJsonOpener {
    
    public List<Organization> readJsonFile(String filename) {
        List<Organization> data = new ArrayList<>();
        
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
            JsonReader jsonReader = new JsonReader(new FileReader(filename));

            // Считывание данных из файла
            Type type = new TypeToken<List<Organization>>() {}.getType();
            data = gson.fromJson(jsonReader, type);
            if(data.size() > 0)
                return data;
            else
                return null;
        } catch (Exception ex) {
            System.out.println("Ошибка открытия файла");
            return null;
        }
    }
}
