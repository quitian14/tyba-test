import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.30"
    kotlin("plugin.spring") version "1.4.30"
    kotlin("plugin.jpa") version "1.4.30"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

configurations {
    ktlint
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_14

repositories {
    mavenCentral()
    jcenter()
}

configurations.all { exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat") }

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("junit:junit:4.12")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-ui:1.5.6")
    implementation("org.springdoc:springdoc-openapi-data-rest:1.5.6")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.5.6")
    implementation("com.auth0:java-jwt:3.18.2")


    implementation("org.apache.httpcomponents:httpclient")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("redis.clients:jedis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.1")
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "14"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
