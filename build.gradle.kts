import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") version "2.0.0"
}

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

val http4kVersion: String by project
val junitVersion: String by project

repositories {
    mavenCentral()
}

apply(plugin = "kotlin")

tasks {
    withType<KotlinJvmCompile>().configureEach {
        compilerOptions {
            allWarningsAsErrors = false
            jvmTarget.set(JVM_21)
            freeCompilerArgs.add("-Xjvm-default=all")
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    java {
        sourceCompatibility = VERSION_21
        targetCompatibility = VERSION_21
    }
}

dependencies {

    implementation(platform("org.http4k:http4k-bom:$http4kVersion"))

    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-client-okhttp")
    implementation("org.http4k:http4k-platform-core")
    implementation("org.http4k:http4k-config")
    implementation("org.http4k:http4k-api-openapi")
    implementation("org.http4k:http4k-format-jackson")
    implementation("org.http4k:http4k-security-oauth")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-template-handlebars")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.http4k:http4k-testing-hamkrest")
    testImplementation("org.http4k:http4k-testing-chaos")
    testImplementation("org.http4k:http4k-testing-approval")
    testImplementation("org.http4k:http4k-testing-webdriver")
}
