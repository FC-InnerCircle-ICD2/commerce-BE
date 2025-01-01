plugins {
    id("java")
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.emotionalcart"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Spring Context
    implementation("org.springframework:spring-context:6.2.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
