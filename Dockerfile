# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 as build

# Set the working directory in the container
WORKDIR /app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy your source code, excluding the properties file
COPY src ./src
RUN rm -f ./src/main/resources/application.properties

# Create a new application.properties file with the correct encoding
RUN echo "spring.application.name=DUX_SOFTAWARE_PRUEBA_TECNICA" > ./src/main/resources/application.properties && \
    echo "spring.datasource.url=jdbc:h2:mem:testdb" >> ./src/main/resources/application.properties && \
    echo "spring.datasource.driverClassName=org.h2.Driver" >> ./src/main/resources/application.properties && \
    echo "spring.datasource.username=sa" >> ./src/main/resources/application.properties && \
    echo "spring.datasource.password=" >> ./src/main/resources/application.properties && \
    echo "spring.h2.console.enabled=true" >> ./src/main/resources/application.properties && \
    echo "spring.h2.console.path=/h2-console" >> ./src/main/resources/application.properties && \
    echo "spring.sql.init.mode=always" >> ./src/main/resources/application.properties && \
    echo "spring.sql.init.schema-locations=classpath:schema-and-data.sql" >> ./src/main/resources/application.properties && \
    echo "spring.jpa.hibernate.ddl-auto=none" >> ./src/main/resources/application.properties && \
    echo "spring.jpa.properties.hibernate.format_sql=true" >> ./src/main/resources/application.properties && \
    echo "jwt.secret=/UBNaiRgIPDvYtFeD4MNJOiQu2rC4NhEVD5xrwskdy3dTu6w/1Jvb2NIaTd3ntwBnRealvewir/5YIZIDOCPhw==" >> ./src/main/resources/application.properties && \
    echo "jwt.expiration=604800000" >> ./src/main/resources/application.properties

# Set the file.encoding property to UTF-8 and build the application
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-jar","app.jar"]
