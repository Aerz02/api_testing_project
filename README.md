# 🚀 API Test Automation Framework

> A robust, clean, and highly maintainable test automation engine built to validate REST APIs on the **Automation Exercise** platform.

---

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Maven-3.9+-blue?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/REST--Assured-5.5.0-green?style=for-the-badge" alt="Rest-Assured" />
  <img src="https://img.shields.io/badge/JUnit5-5.11-red?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5" />
  <img src="https://img.shields.io/badge/Mockito-5.23-blueviolet?style=for-the-badge" alt="Mockito" />
</p>

---

## 📖 Table of Contents
1. [Key Features](#-key-features)
2. [Project Architecture](#-project-architecture)
3. [Class Diagram](#-class-diagram)
4. [Getting Started](#-getting-started)
5. [CI/CD Pipeline](#-cicd-pipeline)
6. [Collaboration & Hand-over](#-collaboration--hand-over)
7. [Full Stack Analysis & Security Guidelines](#-full-stack-analysis--security-guidelines)
8. [Meet the Collaborators](#-meet-the-collaborators)

---

## ✨ Key Features

*   **POJO Response Mapping**: Uses Jackson ObjectMapper for precise serialisation and deserialisation of API responses.
*   **Decoupled ApiClient**: Isolates HTTP requests and RestAssured settings from the assertions layer.
*   **Automated Content-Type Parsing**: Registers a custom global parser to handle standard HTML-based payloads safely.
*   **Mockito Mocking**: Runs fast offline unit tests for business logic without firing real HTTP requests.
*   **Comprehensive Coverage**: Tests multiple endpoints (`/productsList`, `/brandsList`, `/searchProduct`) under both happy and sad path scenarios.

---

## 🏗️ Project Architecture

```text
api_testing_project
 ├── PROJECT_BOARD.md                 # Scrum Scrum Board representation
 ├── pom.xml                          # Project build and dependencies configuration
 └── src/test/java
      └── com.sparta.api_testing_project
           ├── client
           │    └── ApiClient.java    # Handles RestAssured requests
           ├── pojos                  # Object models mapping response JSON structures
           │    ├── Brand.java
           │    ├── BrandResponse.java
           │    ├── Category.java
           │    ├── Product.java
           │    ├── ProductResponse.java
           │    └── UserType.java
           ├── service
           │    └── ProductService.java # Business logic service utilising ApiClient
           ├── unit                   # Mockito unit tests for service class
           │    └── ProductServiceUnitTest.java
           └── integration            # RestAssured integration tests
                ├── BrandsIntegrationTest.java
                ├── ProductsIntegrationTest.java
                └── SearchIntegrationTest.java
```

---

## 📊 Class Diagram

```mermaid
classDiagram
    direction TB

    %% Model / POJO Layer
    class ProductResponse {
        -int responseCode
        -List~Product~ products
        +getResponseCode() int
        +getProducts() List~Product~
    }

    class Product {
        -int id
        -String name
        -String price
        -String brand
        -Category category
        +getPriceAsDouble() double
    }

    class Category {
        -UserType usertype
        -String category
    }

    class UserType {
        -String usertype
    }

    class BrandResponse {
        -int responseCode
        -List~Brand~ brands
    }

    class Brand {
        -int id
        -String brand
    }

    ProductResponse "1" *-- "many" Product
    Product "1" *-- "1" Category
    Category "1" *-- "1" UserType
    BrandResponse "1" *-- "many" Brand

    %% Client and Service Layer
    class ApiClient {
        -String BASE_URL
        +getAllProducts() Response
        +postAllProducts() Response
        +getAllBrands() Response
        +putAllBrands() Response
        +searchProduct(String query) Response
        +searchProductMissingParam() Response
    }

    class ProductService {
        -ApiClient apiClient
        +filterProductsByBrand(String brandName) List~Product~
        +getProductsCheaperThan(double maxPrice) List~Product~
    }

    ProductService o-- ApiClient : "uses constructor injection"

    %% Tests Layer
    class ProductServiceUnitTest {
        -ApiClient apiClient
        -Response mockResponse
        -ProductService productService
        +testFilterProductsByBrand()
        +testGetProductsCheaperThan()
    }

    class ProductsIntegrationTest {
        -ApiClient apiClient
        +testGetProductsListHappyPath()
        +testPostProductsListSadPath()
    }

    class BrandsIntegrationTest {
        -ApiClient apiClient
        +testGetBrandsListHappyPath()
        +testPutBrandsListSadPath()
    }

    class SearchIntegrationTest {
        -ApiClient apiClient
        +testSearchProductHappyPath()
        +testSearchProductSadPath()
    }

    ProductServiceUnitTest ..> ProductService : "unit tests"
    ProductsIntegrationTest ..> ApiClient : "integrates"
    BrandsIntegrationTest ..> ApiClient : "integrates"
    SearchIntegrationTest ..> ApiClient : "integrates"
```

---

## 🚀 Getting Started

### Prerequisites
Make sure **JDK 21** and **Maven** are installed on your machine.

### Run Tests
To download dependencies, compile codebase, and run the test suite:
```bash
mvn clean test
```

---

## 🔄 CI/CD Pipeline

The framework has an integrated GitHub Action workflow configured in `.github/workflows/maven.yml`. On every push and Pull Request to `main` or `dev`, it:
1. Provisions an Ubuntu environment.
2. Sets up JDK 21.
3. Caches Maven packages for fast builds.
4. Executes the full test suite (`mvn clean test`).

---

## 🤝 Collaboration & Hand-over

When extending this framework or introducing updates:
1.  **Branching Strategy**:
    *   Create branches off of the `dev` branch.
    *   Name features using `feature/description` pattern.
    *   Integrate to `main` via reviewed Pull Requests.
2.  **POJO Integrity**:
    *   Reflect any endpoint updates in the `pojos` package.
3.  **Offline Logic Testing**:
    *   Write Mockito unit tests in the `unit` package for any logic processing to avoid relying on external resources during fast test runs.

---

## 🛡️ Full Stack Analysis & Security Guidelines

### 🔑 Credentials & Secrets Management
*   **Best Practice**: Avoid hardcoding authentication credentials (e.g. passwords, API keys) inside test files.
*   **Implementation**: Retrieve values dynamically using environment variables (`System.getenv("TEST_USER_PASSWORD")`) or configure local `.properties` files that are ignored by Git. Keep `.env` and `config.properties` registered in your `.gitignore` file.

### 🚦 Rate Limiting & Transient Errors
*   **Best Practice**: Running integration tests continuously on live endpoints can trigger rate limits or web application firewalls.
*   **Implementation**: Configure test retry rules (using libraries like `junit-pioneer`) and include back-off delays if execution volume is high.

### 🧪 Soft Assertions
*   **Best Practice**: Avoid halting test execution on the first minor assertion failure if multiple data fields need to be checked.
*   **Implementation**: Utilise JUnit 5 `Assertions.assertAll()` to execute multiple checks in a single test block and receive a aggregated report of all failures.

### 🧵 Parallel Test Execution
*   **Best Practice**: Minimise build times by running independent integration tests in parallel.
*   **Implementation**: Configure `junit.jupiter.execution.parallel.enabled = true` in `src/test/resources/junit-platform.properties`.

### 📝 Structured Logging
*   **Best Practice**: Decouple test logging from raw standard console stdout.
*   **Implementation**: Direct RestAssured logs to an SLF4J logger facade using logback or log4j2 for structured JSON parsing and aggregation.

---

## 👥 Meet the Collaborators

We are a group of 7 automation and quality assurance experts working together in a Scrum sprint to design, build, and deliver this project.

<br>

<p align="center">
  <img src="https://img.shields.io/badge/MATTHEW%20CORTHORNE-Project%20Lead%20%2F%20Architect-blue?style=for-the-badge" alt="Matthew Corthorne" />
</p>

<p align="center">
  <a href="https://github.com/oanzia99">
    <img src="https://img.shields.io/badge/Oan%20Zia-CI%2FCD%20%26%20DevOps%20Lead-green?style=flat-square&logo=github&logoColor=white" alt="Oan Zia" />
  </a>
  <img src="https://img.shields.io/badge/Piravien-Test%20Case%20Designer-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Zeenia%20Haji-Senior%20QA%20Engineer-green?style=flat-square" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Aerhan%20Srirangan-API%20Integration%20Specialist-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Salah%20Ali-Scrum%20Master-green?style=flat-square" />
  <img src="https://img.shields.io/badge/Kamaron%20Daley-Automation%20Developer-green?style=flat-square" />
</p>
