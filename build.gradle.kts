plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.bibisam"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
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
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.postgresql:postgresql:42.7.2")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation ("org.hibernate.validator:hibernate-validator:7.0.0.Final")
	implementation ("org.springframework.boot:spring-boot-starter-mail:3.1.2")
	implementation ("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("io.lettuce:lettuce-core:6.5.2.RELEASE")
	implementation ("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.7.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
