plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "dev.samuelv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven (
        url="https://m2.dv8tion.net/releases"
    )
    maven (
        url = "https://jitpack.io"
    )
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.github.walkyst:lavaplayer-fork:1.4.2")
    implementation("net.dv8tion:JDA:5.0.0-beta.10")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}