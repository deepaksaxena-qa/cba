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
public class AddPetTest {

    private int createdPetId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");
    }

    @Test(description = "Add a new pet to the store")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add a new pet to the store and validate the response")
    public void addNewPetTest() {
        Pet pet = JsonUtils.getPetFromJson("addNewPet.json");

        Allure.step("Sending request to add a new pet with ID: " + pet.getId());
        Response response = PetApiUtils.addPet(pet);

        Allure.step("Validating the response status code and response body");
        response.then().statusCode(200);

        // Capture the created pet ID
        createdPetId = response.jsonPath().getInt("id");
        Allure.step("Successfully added pet with ID: " + createdPetId);

        Assert.assertEquals(response.jsonPath().getString("name"), pet.getName(), "Pet name does not match");
        Assert.assertEquals(response.jsonPath().getString("status"), pet.getStatus(), "Pet status does not match");
    }

    @AfterClass
    public void cleanup() {
        // Delete the pet after tests are done
        if (createdPetId > 0) {
            Response response = PetApiUtils.deletePet(createdPetId);
            Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete pet after add test");
            Allure.step("Successfully deleted pet with ID: " + createdPetId);
        }
    }
}


