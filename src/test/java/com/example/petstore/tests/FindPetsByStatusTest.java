package com.example.petstore.tests;

import com.example.petstore.models.Pet;
import com.example.petstore.utils.PetApiUtils;
import com.example.petstore.utils.ConfigReader;
import com.example.petstore.utils.JsonUtils;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Epic("Pet Store API Tests")
@Feature("Pet Endpoint Tests")
public class FindPetsByStatusTest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = ConfigReader.get("baseURI");
    }

    @DataProvider(name = "petStatusProvider")
    public Object[][] petStatusProvider() {
        return new Object[][] {
                {"available"},
                {"pending"},
                {"sold"}
        };
    }

    @Test(description = "Find pets by status", dataProvider = "petStatusProvider")
    @Severity(SeverityLevel.NORMAL)
    @Story("Find pets by their status and validate response")
    public void findPetsByStatusTest(String status) {
        // Ensure a pet with the specified status exists
        Pet pet = JsonUtils.getPetFromJson("addNewPet.json");
        pet.setStatus(status);

        Allure.step("Adding a new pet with status: " + status);
        Response addResponse = PetApiUtils.addPet(pet);
        addResponse.then().statusCode(200);

        // Find pets by status
        Allure.step("Sending request to find pets by status: " + status);
        Response response = PetApiUtils.findPetsByStatus(status);

        // Validate response
        Allure.step("Validating the response status code and content");
        response.then().statusCode(200);

        List<Pet> pets = response.jsonPath().getList("", Pet.class);

        // Verify if the returned list contains pets or is empty, but the call should not fail
        if (pets.isEmpty()) {
            Allure.step("No pets found with status: " + status);
        } else {
            Allure.step("Validating that all pets have status: " + status);
            for (Pet foundPet : pets) {
                Assert.assertEquals(foundPet.getStatus(), status, "Pet status does not match the requested status");
            }
        }
    }
}
