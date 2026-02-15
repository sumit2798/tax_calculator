# Tax Calculator API

A robust MicroProfile-based REST API for calculating tax rates for CRM and BRM systems, secured with JWT Authentication.

## 📋 Prerequisites

- **Java 21** (or higher)
- **Maven 3.8+**
- **Git** (optional, for cloning)

## 🚀 Installation & Setup

1.  **Build the project**:
    ```powershell
    mvn clean package
    ```

2.  **Run the application**:
    ```powershell
    java -jar target/taxcalculation.jar
    ```
    The server will start at `http://localhost:8080`.

## 📂 Project Structure & Functional Parts

Here is a breakdown of the key files and their responsibilities:

### 1. **Core Configuration**
- **`src/main/resources/application.yaml`**: The main configuration file.
    - Configures the server port (`8080`).
    - Sets JWT expiration (`app.jwt.expiration-seconds`).
    - Configures MicroProfile JWT authentication (Public Key location, Issuer).
- **`src/main/java/io/helidon/examples/tax/TaxApplication.java`**:
    - The JAX-RS Application entry point.
    - Enables MP-JWT authentication via `@LoginConfig`.

### 2. **Authentication & Security**
- **`src/main/java/io/helidon/examples/tax/TokenResource.java`**:
    - **Endpoint**: `GET /token`
    - **Function**: Generates signed JWTs (JSON Web Tokens) for testing.
    - **Security**: Requires Basic Authentication (`admin`/`password`).
    - **Logic**: Reads the private key (`privateKey.pem`) and signs a token with a configurable expiration.
- **`src/main/resources/privateKey.pem`** & **`publicKey.pem`**:
    - RSA Key Pair used for signing (Private) and verifying (Public) tokens.

### 3. **Tax Calculation Logic**
- **`src/main/java/io/helidon/examples/tax/TaxResource.java`**:
    - **Endpoint**: `POST /tax/calculate`
    - **Function**: The core logic controller.
    - **Logic**:
        - **Dispatch**: Checks the `Source` field ("CRM" or "BRM").
        - **Rates**: Applies dynamic rates based on account type:
            - **B2B**: 10%
            - **B2C**: 15%
        - **Format**: Returns tax rates as percentage strings (e.g., "10%").
- **`src/main/java/io/helidon/examples/tax/TaxRequest.java`**:
    - **Function**: Input DTO. Handles JSON binding for both CRM and BRM fields.
- **`src/main/java/io/helidon/examples/tax/TaxResult.java`**:
    - **Function**: Common result object for CRM line items.

### 4. **Testing**
- **`src/test/java/io/helidon/examples/tax/AuthTest.java`**:
    - **Function**: Integration test suite.
    - Verifies:
        - Token generation.
        - JWT Authentication (401 without token, 200 with token).
        - Tax Calculation logic (Rate correctness).
        - Response formatting (Percentage strings).

---

## 🔐 Authentication Usage

This API is secured. You must obtain a JWT token to access the tax calculation endpoints.

### Step 1: Generate a Token
Call the public `/token` endpoint to get a fresh JWT. Requires **Basic Authentication** (`admin`/`password`).

```bash
curl -u admin:password http://localhost:8080/token
```
**Response:**
```text
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9... (Long Token String)
```

### Step 2: Access Protected Endpoints
Use the token in the `Authorization` header: `Bearer <YOUR_TOKEN>`.

---

## 🧮 API Usage Examples

### 1. Calculate Tax (CRM Source)

**Scenario**: A B2B transaction (Expect 10% Tax).

```bash
curl -X POST http://localhost:8080/tax/calculate \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "Source": "CRM",
    "accountType": "B2B",
    "lineItems": [
        { "lineItemId": "1", "amount": 100 }
    ]
  }'
```

**Response**:
```json
{
    "taxResults": [
        {
            "lineItemId": "1",
            "taxAmount": 10.0,
            "taxRate": "10%"
        }
    ],
    "totalTax": 10.0
}
```

### 2. Calculate Tax (BRM Source)

**Scenario**: A B2C transaction (Expect 15% Tax).

```bash
curl -X POST http://localhost:8080/tax/calculate \
  -H "Authorization: Bearer <YOUR_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "Source": "BRM",
    "TaxCode": "B2C",
    "AmountTaxed": "200"
  }'
```

**Response**:
```json
{
    "CalculatedTax": "30.00",
    "TaxPercentage": "15%",
    "GrossTaxamount": "230.00",
    "TaxCode": "B2C",
    "TransactionID": ""
}
```
