plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
}

group = "com.jetbrains"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-csv:1.10.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
