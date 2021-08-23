plugins {
    kotlin("jvm") version "1.5.10"
}

group = "org.bar42"
version = "0.9"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.rest-assured:rest-assured:4.4.0")
}

tasks.test {
    useJUnitPlatform()
}