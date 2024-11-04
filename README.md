# Pet Store API Automation Framework

## Overview
This repository contains an API automation framework built using RestAssured and TestNG for testing the Pet Store API. The framework includes regression tests for various endpoints and is integrated with CI/CD for automated testing and reporting.

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

```bash
mvn clean test
```
## CI/CD Integration
This framework is integrated with GitHub Actions for continuous integration and continuous deployment (CI/CD). Upon every push to the main or feature/* branches, the following actions occur:

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