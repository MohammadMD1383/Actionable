buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.jetbrains.intellij") version "1.13.3"
	kotlin("jvm") version "1.8.10"
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
version = "4.0.0"

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

tasks {
	compileKotlin {
		kotlinOptions {
			jvmTarget = JavaVersion.VERSION_17.toString()
			freeCompilerArgs += listOf(
				"-Xjvm-default=all",
				"-Xcontext-receivers"
			)
		}
	}
	
	runIde {
		autoReloadPlugins.set(true)
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
				<li>New <b>FULL</b> documentation available at <a href='https://mohammadmd1383.github.io/Actionable/'>here</a></li>
				<li>Internal cleanup, refactorings and improvements</li>
				<li>Fixed a bug in Justify Carets Action</li>
				<li>Fixed a bug in Select Until... Action</li>
				<li>Removed Predict Words Actions</li>
				<li>Reordered Actions</li>
				<li>Added New Action: Create Temporary File</li>
			</ul>
		""".trimIndent())
	}
	
	test {
		useJUnitPlatform()
	}
}
