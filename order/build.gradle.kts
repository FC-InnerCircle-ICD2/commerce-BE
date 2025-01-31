plugins {
    java
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.5"
}
val springCloudVersion by extra("2024.0.0")
var archunitVersion by extra("1.3.0")
val redissonVersion by extra("3.42.0")
group = "com.emotionalcart"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter")
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-mysql")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    // https://mvnrepository.com/artifact/org.redisson/redisson-spring-boot-starter
    implementation("org.redisson:redisson-spring-boot-starter:$redissonVersion")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")

    annotationProcessor("org.projectlombok:lombok")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.mysql:mysql-connector-j")

    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:mysql")
    // H2 Database Driver
    implementation("com.h2database:h2:2.2.224")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")

    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/com.tngtech.archunit/archunit-junit5
    testImplementation("com.tngtech.archunit:archunit-junit5:$archunitVersion")

}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
