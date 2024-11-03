package com.example.petstore.utils;

import com.example.petstore.models.Pet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class JsonUtils {
    public static Pet getPetFromJson(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        Pet pet = null;
        try {
            pet = mapper.readValue(Paths.get("src/test/resources/testData/" + fileName).toFile(), Pet.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pet;
    }
}
