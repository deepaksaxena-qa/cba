package com.example.petstore.utils;

import com.example.petstore.models.Pet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import io.qameta.allure.restassured.AllureRestAssured;

import java.io.File;

public class PetApiUtils {

    static {
        // Attach AllureRestAssured filter to RestAssured
        RestAssured.filters(new AllureRestAssured());
    }

    public static Response addPet(Pet pet) {
        return given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet");
    }

    // Overload for numeric pet ID
    public static Response getPetById(int petId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petId);
    }

    // Overload for string pet ID (for invalid ID format case)
    public static Response getPetById(String petId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petId);
    }

    public static Response deletePet(int petId) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/" + petId);
    }

    public static Response findPetsByStatus(String status) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus");
    }

    public static Response updatePet(Pet pet) {
        return given()
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put("/pet");
    }

    public static Response uploadPetImage(int petId, File imageFile) {
        return RestAssured.given()
                .contentType("multipart/form-data")
                .multiPart("file", imageFile)
                .pathParam("petId", petId)
                .post("/pet/{petId}/uploadImage");
    }

}
