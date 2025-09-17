plugins {
  id("java")

}


repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("ch.qos.logback:logback-classic:1.5.17")
  implementation("org.slf4j:slf4j-api:2.0.16")
  implementation("org.slf4j:slf4j-ext:2.0.9")
  // JAXB API and runtime for XML binding
  implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
  runtimeOnly("org.glassfish.jaxb:jaxb-runtime:4.0.5")
  testImplementation("org.testcontainers:testcontainers:1.21.3")
}

tasks.test {
  useJUnitPlatform()
}
