import org.gradle.internal.declarativedsl.parsing.main

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.freefair.lombok") version "8.11"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.postgresql:postgresql")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-security") // Para el auth
    implementation("org.springframework.boot:spring-boot-starter-web") // Para el mapeo de metodos HTTP
    //implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Para la persistencia de datos/entidades
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive") // Para persistencia en mongo
    implementation("org.springframework.boot:spring-boot-starter-validation") // Para validar parametros
    implementation("org.springframework.boot:spring-boot-starter-webflux") // Para validar parametros

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter") // Testing con JUnit
    testImplementation("org.springframework.boot:spring-boot-starter-test") // Para pruebas unitarias
    testImplementation("org.springframework.security:spring-security-test") // Para security en pruebas
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    testImplementation("io.projectreactor:reactor-test")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}