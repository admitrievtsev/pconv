plugins {
    id("application")
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(group = "org.bytedeco", name = "opencv-platform", version = "4.5.1-1.5.5")
    implementation("org.testng:testng:6.14.3")
}

application {
    mainClass = "core.App"
}
tasks.run.configure {
    standardInput = System.`in`
    standardOutput = System.`out`

}

tasks.test {
    useTestNG()
}