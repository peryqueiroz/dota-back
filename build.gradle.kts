plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
}

group = "com.b1house"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

//tasks.jar {
//	manifest {
//		attributes["Main-Class"] = "com.b1house.dotaslz.DotaslzApplication com.b1house.dotaslz.DotaslzApplication" // Substitua 'com.example.MyApplication' pelo pacote e nome da sua classe principal
//	}
//}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	compileOnly("org.projectlombok:lombok:1.18.20")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
