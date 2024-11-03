package com.example.petstore.tests;

import com.example.petstore.models.Pet;
import com.example.petstore.utils.ConfigReader;
import com.example.petstore.utils.JsonUtils;
import com.example.petstore.utils.PetApiUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DeletePetTest {

    private int petId;  // Store the ID of the dynamically created pet

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");

        // Create a new pet and store its ID for deletion test
        Pet pet = JsonUtils.getPetFromJson("addNewPet.json");
        Response response = PetApiUtils.addPet(pet);
        response.then().statusCode(200);
        petId = response.jsonPath().getInt("id");
    }

    @Test(description = "Delete an existing pet by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Delete a pet using its ID and validate deletion")
    public void deletePetByIdTest() {
        Allure.step("Deleting pet with ID: " + petId);
        Response response = PetApiUtils.deletePet(petId);

        Allure.step("Validating 200 response status for deletion");
        response.then().statusCode(200);
        Assert.assertTrue(response.jsonPath().getString("message").contains(String.valueOf(petId)), "Deletion message not found");

        // Attempt to retrieve the deleted pet to validate that it no longer exists
        Allure.step("Attempting to retrieve deleted pet by ID: " + petId);
        Response getResponse = PetApiUtils.getPetById(petId);
        getResponse.then().statusCode(404); // Expecting 404 since the pet should no longer exist
        Assert.assertTrue(getResponse.jsonPath().getString("message").contains("Pet not found"), "Expected pet not found message was not received");
    }

    @Test(description = "Attempt to delete a non-existent pet by ID (404 - Not Found)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Attempt to delete a pet using a non-existent ID")
    public void deleteNonExistentPetTest() {
        int nonExistentId = 9999999; // A high ID that should not exist

        Allure.step("Attempting to delete pet with non-existent ID: " + nonExistentId);
        Response response = PetApiUtils.deletePet(nonExistentId);

        Allure.step("Validating 404 response status for non-existent pet deletion");
        response.then().statusCode(404);
    }
}

