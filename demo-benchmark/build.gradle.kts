plugins {
  id("java")
  // The following line allows to load io.gatling.gradle plugin and directly apply it
  id("io.gatling.gradle").version("3.11.5")
}

gatling {
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation(platform("org.junit:junit-bom:5.10.0"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
  testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.11.5")
  testImplementation("ch.qos.logback:logback-classic:1.5.17")
  compileOnly("org.projectlombok:lombok:1.18.36")
  implementation("org.slf4j:slf4j-api:2.0.16")
  implementation("org.slf4j:slf4j-ext:2.0.9")
  testImplementation("org.testcontainers:testcontainers:1.21.0")
}

tasks.test {
  useJUnitPlatform()
}
