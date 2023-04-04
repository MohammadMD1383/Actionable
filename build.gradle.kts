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
version = "4.1.0"

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
				<li><b>New Feature: Download And Install Nightly Build !!!</b></li>
				<li><b>New Action</b>: Expand Collapsed Tag By SmartEnter (XML)</li>
				<li><b>New Action</b>: Switch Function Body Expression Style (Kotlin)</li>
				<li><b>New Action</b>: Create Multiple Files (with/without (nested) directories) (at once!)</li>
				<li><b>New Action</b>: Delete Current File</li>
				<li><b>New Action</b>: Select Text Between Any Quotes (feature request #44: universal select between quotes)</li>
				<li><b>New Action</b>: Backward/Forward Select Until...</li>
				<li>Added settings page for XML, HTML and Java typing actions</li>
				<li>Enhancements to Duplicate Line And Insert Contents (Don't miss it!)</li>
			</ul>
			<div>
				From now you can install the latest nightly builds directly from your IDE! Just use the action <b>Download And Install Nightly Build</b>
				<br>
				To see all new features documentation please check out the <a href='https://mohammadmd1383.github.io/Actionable/Actions%20Documetation/'>Actions Documentation</a> section.
			</div>
		""".trimIndent())
	}
	
	test {
		useJUnitPlatform()
	}
}
