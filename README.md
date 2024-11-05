# Pet Store API Automation Framework

## Overview
This repository contains an API automation framework built using RestAssured and TestNG for testing the Pet Store API. The framework includes tests for various endpoints and is integrated with CI/CD for automated testing and reporting.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Running Tests Locally](#running-tests-locally)
- [CI/CD Integration](#cicd-integration)
- [Allure Reporting](#allure-reporting)

## Prerequisites
- Java 21
- Maven
- GitHub account (for accessing the CI/CD pipeline)

## Running Tests Locally
1. Clone the repository:

```bash
git clone <repository-url>
cd <project-directory>
```

2. Install project dependencies:

```bash
mvn clean install
```

3. Run the Tests

    - **To Run All Tests:**

      Run the following command to execute all tests

      ```bash
        mvn clean test
      ```

    - **To Run an Individual Test:**

      You can run a specific test class by specifying the class name. For example, to run only `AddPetTest`:

      ```bash
       mvn -Dtest=AddPetTest test
      ```

    - **To Run the Regression Test Suite Only:**

      If you have grouped your regression tests under a `regression` suite in your TestNG configuration (e.g., `regression.xml`), you can run just the regression tests with:

       ```bash
          mvn clean test -P regression-suite
       ```

## CI/CD Integration
This framework is integrated with GitHub Actions for continuous integration. Upon every push to the main or feature/* branches, the following actions occur:

- The Maven dependencies are cached to improve build times.
- The regression suite is executed automatically.
- Test results are generated and saved as artifacts.
- Allure reports are created and can be downloaded for review.

### Viewing CI/CD Results
You can view the results of the CI/CD pipeline on the GitHub Actions tab of this repository.

## Allure Reporting
Allure reporting is integrated into the project. After running tests, you can view detailed reports by downloading the report artifact from the CI/CD pipeline. To generate a report locally:

1. Run the tests to generate the target/allure-results folder.
2. Execute:
```bash
mvn allure:serve
```