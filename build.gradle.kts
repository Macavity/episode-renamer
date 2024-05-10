plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.4.20"
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("io.mockk:mockk:1.13.+")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("org.yaml:snakeyaml:2.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.+")
    implementation("com.charleskorn.kaml:kaml:0.58.+")
}

detekt {
    toolVersion = "1.18.1"
    source = files("src/main/kotlin")
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
