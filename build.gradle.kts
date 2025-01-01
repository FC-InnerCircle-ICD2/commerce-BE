plugins {
	id("java")
	id("io.spring.dependency-management") version "1.1.5"
}

allprojects {
	group = "com.emotionalcart"
	version = "1.0.0"
	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "io.spring.dependency-management")

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(21)
		}
	}

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}

	tasks.withType<JavaCompile> {
		options.encoding = "UTF-8"
		options.release.set(21)
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}
