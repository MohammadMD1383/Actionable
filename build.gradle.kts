plugins {
	id("java")
	id("org.jetbrains.kotlin.jvm") version "2.1.21"
	id("org.jetbrains.intellij.platform") version "2.20.1"
}

group = "ir.mmd.intellijDev"
version = "4.6.0"

repositories {
	mavenCentral()
	intellijPlatform {
		defaultRepositories()
	}
}

dependencies {
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.2")

	intellijPlatform {
		// Ultimate is required for JavaScript / Go-related optional features while developing.
		intellijIdeaUltimate("2025.1.7.2")

		bundledPlugin("com.intellij.java")
		bundledPlugin("org.jetbrains.kotlin")
		bundledPlugin("JavaScript")
		plugin("org.jetbrains.plugins.go", "251.28293.39")

		pluginVerifier()
		zipSigner()
	}
}

sourceSets["main"].java.srcDirs("src/main/gen")

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

kotlin {
	jvmToolchain(21)
	compilerOptions {
		freeCompilerArgs.add("-Xjvm-default=all")
		freeCompilerArgs.add("-Xcontext-receivers")
		freeCompilerArgs.add("-Xsuppress-warning=CONTEXT_RECEIVERS_DEPRECATED")
	}
}

intellijPlatform {
	pluginConfiguration {
		version = project.version.toString()

		ideaVersion {
			// Fixed minimum: IntelliJ Platform 2025.1; unlimited maximum (open-ended).
			sinceBuild = "251"
			untilBuild = provider { null }
		}

		changeNotes = """
			<h2>v4.6.0 — Compatibility Update</h2>
			<ul>
				<li><b>Compatibility</b>: supports IntelliJ Platform 2025.1 and newer (open-ended until-build)</li>
				<li><b>Build</b>: migrated to IntelliJ Platform Gradle Plugin 2.x, Gradle 9, Kotlin 2.1, Java 21</li>
				<li><b>Fix</b>: replaced removed PreloadingActivity with a modern startup activity</li>
				<li><b>Note</b>: Kotlin-specific actions remain optional and may stay unloaded under Kotlin K2 mode until migrated to the Analysis API</li>
			</ul>
			<div>
				To install nightly builds use <b>Download And Install Nightly Build</b> Action
				<br>
				To see all new features documentation please check out the <a href='https://mohammadmd1383.github.io/Actionable/Actions%20Documetation/'>Actions Documentation</a> section.
				<br>
				<b>To see the offline docs right from your ide use the action: Open Offline Help</b>
			</div>
		""".trimIndent()
	}

	publishing {
		token = providers.environmentVariable("ACTIONABLE_PUBLISH_TOKEN")
	}

	pluginVerification {
		ides {
			recommended()
		}
	}
}

val createOpenApiSourceJar = tasks.register<Jar>("createOpenApiSourceJar") {
	from(sourceSets.main.get().java) {
		include("**/*.java")
	}

	from(kotlin.sourceSets.main.get().kotlin) {
		include("**/*.kt")
	}

	destinationDirectory.set(layout.buildDirectory.dir("libs"))
	archiveClassifier.set("src")
}

tasks.named<org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask>("prepareSandbox") {
	dependsOn(createOpenApiSourceJar)
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE

	from(createOpenApiSourceJar) {
		into(pluginName.map { "$it/lib/src" })
	}

	from("docs/site") {
		into(pluginName.map { "$it/docs" })
	}
}

tasks.test {
	useJUnitPlatform()
}
