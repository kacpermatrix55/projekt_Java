plugins {
    java
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

sourceSets {
    main {
        java {
            srcDir("src/main/java")
            srcDir("build/generated-sources/jaxb")
        }
    }
}

val jaxb by configurations.creating

task("genJaxb") {
    val sourcesDir = "${buildDir}/generated-sources/jaxb"
    val schema = "src/main/resources/populationCount.xsd"

    outputs.dir(sourcesDir)

    doLast {
        ant.withGroovyBuilder {
            "taskdef"(
                    "name" to "xjc",
                    "classname" to "com.sun.tools.xjc.XJCTask",
                    "classpath" to jaxb.asPath
            )

            mkdir(sourcesDir)

            "xjc"(
                    "destdir" to sourcesDir,
                    "schema" to schema
            ) {
                "arg"("value" to "-wsdl")
                "produces"("dir" to sourcesDir, "includes" to "**/*.java")
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.4")
    implementation("org.springframework.boot:spring-boot-starter-web-services:3.0.4")
    implementation("org.flywaydb:flyway-core:9.16.0")
    implementation("org.flywaydb:flyway-mysql:9.16.0")
    implementation("org.mockito:mockito-core:5.2.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.14.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.14.2")
    implementation("org.springframework.data:spring-data-jpa:3.1.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
    implementation("io.jsonwebtoken:jjwt-root:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("wsdl4j:wsdl4j:1.6.3")
    jaxb("org.glassfish.jaxb:jaxb-xjc:4.0.2")
    compileOnly("org.projectlombok:lombok:1.18.26")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.0.4")
    // developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.1.2")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
    testImplementation("org.springframework.security:spring-security-test:6.0.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
