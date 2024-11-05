package com.example.petstore.tests;

import com.example.petstore.models.Pet;
import com.example.petstore.utils.PetApiUtils;
import com.example.petstore.utils.ConfigReader;
import com.example.petstore.utils.JsonUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Pet Store API Tests")
@Feature("Pet Endpoint Tests")
public class GetPetByIdTest {
    private Pet pet;  // Store the Pet object for reference in assertions
    private int existingPetId; // Store the ID of a dynamically created pet

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");

        // Create a new pet and store its ID
        pet = JsonUtils.getPetFromJson("addNewPet.json");
        Response response = PetApiUtils.addPet(pet);
        response.then().statusCode(200);
        existingPetId = response.jsonPath().getInt("id");
    }

    @Test(description = "Retrieve an existing pet by ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Retrieve an existing pet by ID and verify response details")
    public void getPetByIdTest() {
        Allure.step("Sending request to get pet details by ID: " + existingPetId);
        Response response = PetApiUtils.getPetById(existingPetId);

        Allure.step("Validating 200 response status and pet details");
        response.then().statusCode(200);


        // Assertions based on expected values
        Assert.assertEquals(response.jsonPath().getInt("id"), existingPetId, "Pet name does not match");
        Assert.assertEquals(response.jsonPath().getString("name"), pet.getName(), "Pet name does not match");
        Assert.assertEquals(response.jsonPath().getString("status"), pet.getStatus(), "Pet status does not match");
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
