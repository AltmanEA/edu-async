plugins {
    kotlin("js") version "1.5.31"
}

group = "ru.altmanea.edu-async"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js:1.5.31")
}

kotlin {
    js(LEGACY) {
        binaries.executable()
        browser { }
    }
}