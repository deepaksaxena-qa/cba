package com.example.petstore.tests;

import com.example.petstore.utils.PetApiUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class FindPetsByStatusTest {

    @DataProvider(name = "petStatusProvider")
    public Object[][] petStatusProvider() {
        return new Object[][]{
                {"available"},
                {"pending"},
                {"sold"},
                {"notarealstatus"} // This is for the invalid status test
        };
    }

    @Test(dataProvider = "petStatusProvider", description = "Find pets by status")
    @Severity(SeverityLevel.NORMAL)
    @Story("Retrieve pets based on different statuses and validate the response")
    public void findPetsByStatusTest(String status) {
        Allure.step("Finding pets with status: " + status);
        Response response = PetApiUtils.findPetsByStatus(status);

        if ("notarealstatus".equals(status)) {
            Allure.step("Validating response for invalid status");
            response.then().statusCode(400);
            Assert.assertTrue(response.jsonPath().getString("message").contains("Invalid status value"),
                    "Expected invalid status message was not received");
        } else {
            Allure.step("Validating response for valid status: " + status);
            response.then().statusCode(200);

            List<?> pets = response.jsonPath().getList(""); // Retrieve the list of pets
            Assert.assertNotNull(pets, "Pets list should not be null");
            Assert.assertFalse(pets.isEmpty(), "Pets list should not be empty for status: " + status);

            for (Object pet : pets) {
                String petStatus = (String) ((Map<?, ?>) pet).get("status");
                Assert.assertEquals(petStatus, status, "Pet status should be '" + status + "'");
            }
        }
    }
}

