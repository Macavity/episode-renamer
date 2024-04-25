plugins {
    kotlin("jvm") version "1.8.21"
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

detekt {
    toolVersion = "1.18.1"
    input = files("src/main/kotlin")
    parallel = true
    buildUponDefaultConfig = true
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}