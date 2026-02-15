
# Hosting the Tax Calculator Service

## Prerequisites
- Docker installed and running
- Kubernetes cluster (e.g., Minikube, Docker Desktop Kubernetes)
- `kubectl` configured
- **Java 21+ Required** (if running locally without Docker) - *Note: Helidon 4 does not support Java 17.*

## 1. Build and Run Locally
To run the application locally without Docker:

1.  Navigate to the project directory:
    ```bash
    cd D:\Tax_Calculater\tax-calculator
    ```
2.  Build the project:
    ```bash
    mvn clean package
    ```
3.  Run the application:
    ```bash
    java -jar target/taxcalculation.jar
   
    ```

> [!IMPORTANT]
> **Java Version Warning:** This application uses Helidon 4, which requires **Java 21 or newer**. 
> It will **NOT** run on a server with only Java 17 installed unless you use Docker/Podman.


## 2. Build the Docker Image
Run the following command in the project root directory (`D:\Tax_Calculater\tax-calculator`):

```bash
docker build -t taxcalculation:latest .
```

## 3. Deploy to Kubernetes
Apply the deployment and service configuration:

```bash
kubectl apply -f deployment.yaml
```

## 4. Verify Deployment
Check if the pod is running:

```bash
kubectl get pods
```

Check the service:

```bash
kubectl get services
```

## 5. Access the Service
The service is exposed via NodePort 30080. You can access it at:
`http://localhost:30080/tax/calculate` (or usage your Docker IP if not localhost).

## 6. Usage with Authentication
Since Basic Authentication is enabled, you must provide credentials.

**Example using curl:**

```bash
curl -X POST http://localhost:30080/tax/calculate \
     -u admin:password \
     -H "Content-Type: application/json" \
     -d '{"source": "NY", "lineItems": [{"description": "Item 1", "price": 100.0}, {"description": "Item 2", "price": 50.0}]}'
```

## 7. Stop the Application

### Running Locally (Java)
Press `Ctrl + C` in the terminal where the application is running.

### Running in Docker / Podman
To stop the container:
```bash
# Find the container ID
docker ps
# OR
podman ps

# Stop the container
docker stop <container_id>
# OR
podman stop <container_id>
```

### Running in Kubernetes
To delete the deployment and service:
```bash
kubectl delete -f deployment.yaml
```

## 8. Frequently Asked Questions

### Do I need to install Java 21 on my server/pod?
**No.** The Docker container includes its own internal Java 21 runtime.
-   **Host Server:** Can run any OS (Linux, Windows, Mac) with any Java version (or none at all).
-   **Container:** Automatically has Java 21 pre-installed because the `Dockerfile` uses `eclipse-temurin:21-jre-alpine` as the base image.

### I get a "pasta" error with Podman?
If you see an error like `Error: verified networking backend "pasta" not found`, it means your Podman installation is missing the `passt` networking tool.

To fix it, tell Podman to use the standard network backend:
```bash
podman build --network=slirp4netns -t tax-calculator .
```
OR use the host network directly:
```bash
podman build --network=host -t tax-calculator .
```

podman build --network=host -t tax-calculator .
```

*Note: You cannot skip networking entirely because Maven needs to download dependencies during the build.*

### Why is the WORKDIR `/helidon`?
This is just a convention used in Helidon examples. It tells Docker: "Create a directory named `/helidon` inside the container and run all subsequent commands there."

You could change it to `/app` or `/my-service` and it would work exactly the same, as long as you use that same path consistently in the `Dockerfile`.




## 9. Push to Container Registry

### Option A: DockerHub

1.  **Login to DockerHub**
    ```bash
    docker login
    ```
    Enter your DockerHub username and password/token when prompted.

2.  **Tag the Image**
    Replace `<your-username>` with your actual DockerHub username.
    ```bash
    docker tag taxcalculation:latest <your-username>/taxcalculation:latest
    ```

3.  **Push the Image**
    ```bash
    docker push <your-username>/taxcalculation:latest
    ```

### Option B: Oracle Container Registry (OCIR)

1.  **Prerequisites**
    - You need an Auth Token generated from your OCI User Settings.
    - Identify your Object Storage Namespace (found in OCI Tenancy details).
    - Identify your Region Key (e.g., `fra` for Frankfurt, `iad` for Ashburn).

2.  **Login to OCIR**
    ```bash
    docker login <region-key>.ocir.io
    ```
    - **Username:** `<tenancy-namespace>/<username>` (e.g., `mytenancy/alice@example.com`)
    - **Password:** Your OCI Auth Token

3.  **Tag the Image**
    Structure: `<region-key>.ocir.io/<tenancy-namespace>/<repo-name>:<tag>`
    ```bash
    docker tag taxcalculation:latest <region-key>.ocir.io/<tenancy-namespace>/taxcalculation:latest
    ```

4.  **Push the Image**
    ```bash
    docker push <region-key>.ocir.io/<tenancy-namespace>/taxcalculation:latest
    ```

## 10. Run from Container Registry

Once your image is pushed, you (or others) can run it from anywhere with Docker installed.

### From DockerHub
```bash
docker run -d -p 30080:8080 --name taxcalculation <your-username>/taxcalculation:latest
```

### From Oracle Container Registry (OCIR)
First, ensure you are logged in (see step 9).
```bash
docker run -d -p 30080:8080 --name taxcalculation <region-key>.ocir.io/<tenancy-namespace>/taxcalculation:latest
```

The application will be available at `http://localhost:30080/tax/calculate`.
