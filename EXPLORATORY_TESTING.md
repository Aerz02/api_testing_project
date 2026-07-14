# 🔍 Exploratory Testing Charter & Bug Log

This document records the exploratory testing sessions conducted on the `https://automationexercise.com/api` endpoints prior to automated script implementation.

---

## 📋 1. Exploratory Testing Charter

*   **Target Scope**: `/productsList`, `/brandsList`, and `/searchProduct` endpoints.
*   **Objectives**:
    *   Examine response headers, payloads, and encoding schemas under diverse request conditions.
    *   Identify deviations between standard REST API designs and actual system behaviour.
    *   Observe how the backend handles unsupported HTTP verbs and missing query parameters.
*   **Out of Scope**: Frontend web GUI testing, payment gateway validation.

---

## 🐛 2. Identified Defect Log (Bug Report)

### 🔴 BUG-001: Content-Type Header Mismatches JSON Payload
*   **Description**: When querying the `/productsList` and `/searchProduct` endpoints, the API server returns a JSON payload containing the product list. However, the HTTP response header states `Content-Type: text/html; charset=utf-8` instead of `application/json`.
*   **Impact**: Automated HTTP clients (like RestAssured or HttpClient) will fail to automatically deserialise the payload into Java POJOs unless they are configured to override the default content-type parsing strategy.
*   **Replication Steps**:
    ```bash
    curl -I "https://automationexercise.com/api/productsList"
    ```
*   **Observed Header**:
    ```text
    Content-Type: text/html; charset=utf-8
    ```
*   **Expected Header**:
    ```text
    Content-Type: application/json; charset=utf-8
    ```
*   **Severity**: Medium (Blocks standard automated deserialisation).
*   **Framework Workaround**: Registered a global parser override in `ApiClient.java`:
    ```java
    RestAssured.registerParser("text/html", Parser.JSON);
    ```

---

### 🟡 BUG-002: Sad Path Requests Return HTTP 200 OK
*   **Description**: When attempting a POST request to `/productsList` (an unsupported verb) or a POST request to `/searchProduct` without the required parameters, the backend server responds with an HTTP Status Code of `200 OK` in the header. The actual error details (e.g. `responseCode: 405` or `responseCode: 400`) are wrapped inside the JSON body.
*   **Impact**: Standard API gateways and monitoring tools will report 100% success rates, hiding client-side errors and unsupported method calls.
*   **Replication Steps**:
    ```bash
    curl -X POST "https://automationexercise.com/api/productsList"
    ```
*   **Observed Response**:
    *   HTTP Header: `HTTP/1.1 200 OK`
    *   Payload Body: `{"responseCode": 405, "message": "This request method is not supported."}`
*   **Expected Response**:
    *   HTTP Header: `HTTP/1.1 405 Method Not Allowed`
*   **Severity**: Low (Confusing REST API implementation; monitoring bypass).
*   **Framework Workaround**: Tests check the payload-level `responseCode` instead of relying strictly on the HTTP Header status code.

---

## 📊 3. Explored Scenarios Matrix

| Endpoint | Verb | Input Conditions | Observed HTTP Status | Payload Code | Result / Observations |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `/productsList` | GET | None | `200 OK` | `200` | Returns full products array. Mismatched header (`text/html`). |
| `/productsList` | POST | None | `200 OK` | `405` | Blocked successfully; returns unsupported method warning. |
| `/brandsList` | GET | None | `200 OK` | `200` | Returns full brands array. |
| `/brandsList` | PUT | None | `200 OK` | `405` | Blocked successfully; returns unsupported method warning. |
| `/searchProduct` | POST | `search_product=tshirt` | `200 OK` | `200` | Returns filtered products containing query term. |
| `/searchProduct` | POST | `search_product=` | `200 OK` | `200` | Returns empty products array. |
| `/searchProduct` | POST | Missing Parameter | `200 OK` | `400` | Returns Bad Request warning. |
