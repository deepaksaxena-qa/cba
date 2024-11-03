# Pet Store API Automation Framework

This project is an API automation framework for the Pet Store API, focusing on automated regression testing of pet-related endpoints. It uses RestAssured with TestNG for test execution and Allure for reporting.

# Prerequisites
- Java 8+
- Maven for dependency management
- Allure for test reporting (ensure it’s installed and added to your PATH)
- To install Allure, follow the instructions [here](https://docs.qameta.io/allure/).

# Setup
1. Clone the repository:

```bash
git clone <your-repo-url>
cd <project-directory>
```

2. Install project dependencies:

```bash
mvn clean install
```

# Running the Tests
To run the regression suite:
```bash
mvn clean test
```
To run specific tests (e.g., AddPetTest and GetPetByIdTest):
```bash
mvn -Dtest=AddPetTest test
mvn -Dtest=GetPetByIdTest test
```

# Configuration
Configuration for the base URL and other parameters can be managed in a separate properties file under src/test/resources/config.

# Reporting
## Generate Allure Report
1. After running tests, generate the Allure report:
```bash
allure serve target/allure-results
```
This command will launch a server displaying the Allure report in your default browser.

## Understanding Test Results
The Allure report provides insights into:
- **Test Results:** Overview of passed, failed, and skipped tests.
- **Test Cases:** Details of each test case with steps and assertions.
- **Logs:** Request/response logs for each API interaction.

# Customization
- **Data-Driven Tests:** JSON files in test/resources/testData supply dynamic data to tests using TestNG's data provider.
- **Cleanup Process:** Tests automatically clean up data (e.g., deleting created pets) to ensure a clean testing environment.