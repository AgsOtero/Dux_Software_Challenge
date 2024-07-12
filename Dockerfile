FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src
RUN rm -f ./src/main/resources/application.properties


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

ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-jar","app.jar"]
