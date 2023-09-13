# Use an official OpenJDK runtime as the base image
# Example if your project are using jdk17 then change its base image to version 17
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the executable JAR file and any other necessary files
COPY target/asset_tracer_api-0.0.1-SNAPSHOT.jar /app

# Expose the port on which your Spring application will run (change as per your application)
EXPOSE 8081
ENV sring.datasource.url=jdbc:postgresql://110.74.194.124:5555/asset-tracer

# Set the command to run your Spring application when the container starts
CMD ["java", "-jar", "/app/asset_tracer_api-0.0.1-SNAPSHOT.jar"]
