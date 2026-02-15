
# 1st stage, build the app
FROM maven:3.9-eclipse-temurin-21 as build

WORKDIR /helidon

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD pom.xml .
RUN mvn package -Dmaven.test.skip -DskipTests

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn package -DskipTests

# 2nd stage, build the runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /helidon

# Copy the binary built in the 1st stage
COPY --from=build /helidon/target/taxcalculation.jar ./
COPY --from=build /helidon/target/libs ./libs

CMD ["java", "-jar", "taxcalculation.jar"]

EXPOSE 8080
