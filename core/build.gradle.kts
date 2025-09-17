plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("application")
}

application {
    mainClass.set("com.jetbrains.perfinsights.core.MainKt")
}

group = "com.jetbrains"
version = "0.1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://packages.jetbrains.team/maven/p/koog/maven") }
    maven { url = uri("https://packages.jetbrains.team/maven/p/grazi/grazie-platform-public") }
}

dependencies {
    implementation(project(":snapshot-analyzer"))
    implementation(project(":adviser"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}

kotlin {
    jvmToolchain(17)
}
