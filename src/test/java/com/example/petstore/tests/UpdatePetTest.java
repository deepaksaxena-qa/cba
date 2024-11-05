package com.example.petstore.tests;

import com.example.petstore.models.Pet;
import com.example.petstore.utils.ConfigReader;
import com.example.petstore.utils.JsonUtils;
import com.example.petstore.utils.PetApiUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Epic("Pet Store API Tests")
@Feature("Pet Endpoint Tests")
public class UpdatePetTest {

    private int petId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");

        // Create initial pet to update
        Pet initialPet = JsonUtils.getPetFromJson("addNewPet.json");
        Response response = PetApiUtils.addPet(initialPet);
        response.then().statusCode(200);

        petId = response.jsonPath().getInt("id");
        Allure.step("Setup: Created initial pet with ID: " + petId + " for update test");
    }

    @Test(description = "Update an existing pet in the store")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update an existing pet and validate the response")
    public void updatePetTest() {
        // Load the updated pet data from JSON file
        Pet updatedPet = JsonUtils.getPetFromJson("updatePet.json");
        updatedPet.setId(petId); // Ensure we use the correct pet ID for updating

        Allure.step("Sending request to update pet with ID: " + petId);
        Response response = PetApiUtils.updatePet(updatedPet);

        Allure.step("Validating the response status code and response body");
        response.then().statusCode(200);

        Assert.assertEquals(response.jsonPath().getInt("id"), petId, "Pet ID does not match");
        Assert.assertEquals(response.jsonPath().getString("name"), updatedPet.getName(), "Pet name does not match");
        Assert.assertEquals(response.jsonPath().getString("status"), updatedPet.getStatus(), "Pet status does not match");
        Allure.step("Successfully updated pet with ID: " + petId);
    }

    @AfterClass
    public void cleanup() {
        // Delete the pet after tests are done
        if (petId > 0) {
            Response response = PetApiUtils.deletePet(petId);
            Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete pet after update test");
            Allure.step("Successfully deleted pet with ID: " + petId);
        }
    }
}
