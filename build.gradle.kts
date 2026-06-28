import org.gradle.api.JavaVersion.VERSION_21
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.versions)
    alias(libs.plugins.typeflows)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
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

    implementation(platform(libs.http4k.bom))

    implementation(libs.http4k.core)
    implementation(libs.http4k.client.okhttp)
    implementation(libs.http4k.platform.core)
    implementation(libs.http4k.config)
    implementation(libs.http4k.api.openapi)
    implementation(libs.http4k.format.jackson)
    implementation(libs.http4k.security.oauth)
    implementation(libs.http4k.server.undertow)
    implementation(libs.http4k.template.handlebars)

    testImplementation(platform(libs.junit.bom))

    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.http4k.testing.hamkrest)
    testImplementation(libs.http4k.testing.chaos)
    testImplementation(libs.http4k.testing.approval)
    testImplementation(libs.http4k.testing.webdriver)

    typeflowsApi(libs.typeflows.github)
    typeflowsApi(libs.typeflows.github.marketplace)
    typeflowsApi(libs.http4k.standards)
}
