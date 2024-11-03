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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class GetPetByIdTest {
    private int existingPetId; // Store the ID of a dynamically created pet

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");

        // Create a new pet and store its ID for the 200 test case
        Pet pet = JsonUtils.getPetFromJson("addNewPet.json");
        Response response = PetApiUtils.addPet(pet);
        response.then().statusCode(200);
        existingPetId = response.jsonPath().getInt("id");
    }

    @Test(description = "Retrieve pet by valid ID (200 - Successful)")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Retrieve an existing pet by ID and verify response details")
    public void getPetById_Successful() {
        Allure.step("Sending request to get pet details by a valid ID: " + existingPetId);
        Response response = PetApiUtils.getPetById(existingPetId);

        Allure.step("Validating 200 response status and pet details");
        response.then().statusCode(200);

        Pet pet = response.as(Pet.class);

        // Assertions based on expected values
        Assert.assertEquals(pet.getId(), existingPetId, "Pet ID does not match");
        Assert.assertNotNull(pet.getName(), "Pet name should not be null");
        Assert.assertEquals(pet.getStatus(), "available", "Pet status does not match expected value");
    }

    @Test(description = "Invalid ID format (400 - Invalid ID request)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Attempt to retrieve a pet using an invalid ID format")
    public void getPetById_InvalidIdFormat() {
        String invalidId = "abc123"; // Use a non-numeric ID to trigger a 400 error

        Allure.step("Sending request with invalid ID format: " + invalidId);
        Response response = PetApiUtils.getPetById(invalidId);

        Allure.step("Validating 400 response status for invalid ID format");
        response.then().statusCode(400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Invalid ID"), "Error message should indicate invalid ID");
    }

    @Test(description = "Non-existent pet ID (404 - Pet Not Found)")
    @Severity(SeverityLevel.NORMAL)
    @Story("Attempt to retrieve a non-existent pet by ID")
    public void getPetById_NotFound() {
        int nonExistentId = 9999999; // Use a high ID that doesn’t exist

        Allure.step("Sending request with non-existent ID: " + nonExistentId);
        Response response = PetApiUtils.getPetById(nonExistentId);

        Allure.step("Validating 404 response status for non-existent pet");
        response.then().statusCode(404);
        Assert.assertTrue(response.jsonPath().getString("message").contains("Pet not found"), "Error message should indicate pet not found");
    }

    @AfterClass
    public void cleanup() {
        // Delete the pet after tests are done
        if (existingPetId > 0) {
            Response response = PetApiUtils.deletePet(existingPetId);
            Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete pet");
            Allure.step("Successfully deleted pet with ID: " + existingPetId);
        }
    }
}
