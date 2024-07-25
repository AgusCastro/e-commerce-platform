FROM docker.io/eclipse-temurin:21_35-jdk-alpine as builder

# Create app directory
WORKDIR /app

# Copy the base build scripts and gradle dependency cache
COPY /gradle ./gradle
COPY /build.gradle /settings.gradle /gradlew ./

# Copy only the source code we need to minimize our Docker cache-invalidation vector
COPY /src ./src

RUN ./gradlew build

FROM docker.io/eclipse-temurin:21_35-jre-alpine

COPY --from=builder /app/build/libs/*.jar /app.jar

CMD java -jar /app.jar
