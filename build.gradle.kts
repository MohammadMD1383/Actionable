buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.jetbrains.intellij") version "1.13.3"
	kotlin("jvm") version "1.8.21"
	java
}

repositories {
	mavenCentral()
}

dependencies {
	classpath("lib")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

fun DependencyHandlerScope.classpath(path: String) {
	compileOnly(fileTree("include" to "**/*.jar", "dir" to path))
}

group = "ir.mmd.intellijDev"
version = "4.3.0"

sourceSets["main"].java.srcDirs("src/main/gen")

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

intellij {
	plugins.add("com.intellij.java")
	plugins.add("org.jetbrains.kotlin")
	
	type.set("IC")
	version.set("2023.1")
}

kotlin {
	jvmToolchain(17)
}

tasks {
	compileKotlin {
		kotlinOptions {
			freeCompilerArgs += listOf(
				"-Xjvm-default=all",
				"-Xcontext-receivers"
			)
		}
	}
	
	buildPlugin {
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		from("docs/site") {
			into("docs")
		}
	}
	
	runIde {
		autoReloadPlugins.set(true)
	}
	
	prepareSandbox {
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		from("docs/site") {
			into("${pluginName.get()}/docs")
		}
	}
	
	buildSearchableOptions {
		enabled = environment["SKIP_BSO"] == null
	}
	
	patchPluginXml {
		version.set(project.version.toString())
		sinceBuild.set("231")
		untilBuild.set("233.*")
		changeNotes.set("""
			<ul>
				<li>New Feature<b></b>: Syntax highlighting, code completion, and inspection for macro templates!</li>
				<li>New Feature<b></b>: ${'$'}LINE$ and ${'$'}NUMBER$ placeholders for macro</li>
				<li>Bug Fix<b></b>: #89 Macro save strategy</li>
				<li>Bug Fix<b></b>: #94 escapes not implemented in macro templates</li>
				<li>Bug Fix<b></b>: a bug while saving macro in settings page</li>
			</ul>
			<div>
				To install nightly builds use <b>Download And Install Nightly Build</b> Action
				<br>
				To see all new features documentation please check out the <a href='https://mohammadmd1383.github.io/Actionable/Actions%20Documetation/'>Actions Documentation</a> section.
				<br>
				<b>To see the offline docs right from your ide use the action: Open Offline Help</b>
			</div>
		""".trimIndent())
	}
	
	test {
		useJUnitPlatform()
	}
	
	publishPlugin {
		token.set(System.getenv("ACTIONABLE_PUBLISH_TOKEN"))
	}
}
