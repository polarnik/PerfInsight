plugins {
  id("org.jetbrains.kotlin.jvm") version "2.1.10"
  id("application")
}

application {
  mainClass.set("com.jetbrains.MainKt")
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
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
  implementation("com.github.javaparser:javaparser-core:3.25.5")
  implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.10")

  testImplementation("org.jetbrains.kotlin:kotlin-test")

  // Koog dependencies
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("ai.koog:koog-agents:0.2.1-feat-275-1")
  implementation("ai.jetbrains.code.prompt:code-prompt-executor-grazie-koog-jvm:1.0.0-beta.68+0.4.71")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}
