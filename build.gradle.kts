plugins {
    java
    jacoco
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "4.4.1.3373"
}

group = "by.innowise"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

jacoco {
    toolVersion = "0.8.12"
}

sonar {
    properties {
        property("sonar.projectKey", "innowise-app")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.login", System.getenv("SONARQUBE_TOKEN"))
        property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.liquibase:liquibase-core")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

val exclusions = listOf(
    "**/entity/**",
    "**/config/**",
    "**/dto/**",
    "**/mapper/**",
    "**/controller/**"
)

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    outputs.upToDateWhen { false }

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }

    val mainOutput = sourceSets["main"].output
    classDirectories.setFrom(
        files(mainOutput.classesDirs).asFileTree.matching {
            exclude(exclusions)
        }
    )

    doLast {
        val reportDir = layout.buildDirectory.dir("reports/jacoco/test/html").get().asFile
        val reportFile = reportDir.resolve("index.html")

        if (reportFile.exists()) {
            println("Jacoco report generated: file://${reportFile.absolutePath}")
        } else {
            println("Jacoco report not found.")
        }
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)

    classDirectories.setFrom(files(fileTree("build/classes/java/main").exclude(exclusions)))

    violationRules {
        rule {
            limit {
                minimum = 0.7.toBigDecimal()
            }
        }
    }
}
