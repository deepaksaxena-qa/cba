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

import java.io.File;

@Epic("Pet Store API Tests")
@Feature("Pet Endpoint Tests")
public class UploadPetImageTest {

    private int petId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");

        // Create a pet to test the upload
        Pet pet = JsonUtils.getPetFromJson("addNewPet.json");
        Response response = PetApiUtils.addPet(pet);
        response.then().statusCode(200);

        petId = response.jsonPath().getInt("id");
        Allure.step("Setup: Created pet with ID: " + petId + " for image upload test");
    }

    @Test(description = "Upload an image for an existing pet")
    @Severity(SeverityLevel.NORMAL)
    @Story("Upload an image for an existing pet and validate the response")
    public void uploadPetImageTest() {
        File imageFile = new File("src/test/resources/testData/Fluffy.jpg");
        Assert.assertTrue(imageFile.exists(), "Image file should exist for upload");

        Allure.step("Sending request to upload image for pet with ID: " + petId);
        Response response = PetApiUtils.uploadPetImage(petId, imageFile);

        Allure.step("Validating the response status code and message");
        response.then().statusCode(200);

        String message = response.jsonPath().getString("message");
        Assert.assertTrue(message.contains("uploaded"), "Response message should indicate successful upload");
        Allure.step("Successfully uploaded image for pet with ID: " + petId);
    }

    @AfterClass
    public void cleanup() {
        // Delete the pet after tests are done
        if (petId > 0) {
            Response response = PetApiUtils.deletePet(petId);
            Assert.assertEquals(response.getStatusCode(), 200, "Failed to delete pet after image upload test");
            Allure.step("Successfully deleted pet with ID: " + petId);
        }
    }
}
