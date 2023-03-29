import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_17.toString()
		freeCompilerArgs = freeCompilerArgs + listOf(
			"-Xjvm-default=all",
			"-Xcontext-receivers"
		)
	}
}

intellij {
	plugins.add("com.intellij.java")
	plugins.add("org.jetbrains.kotlin")
	
	type.set("IC")
	version.set("2023.1")
}

tasks.withType<RunIdeTask> {
	autoReloadPlugins.set(true)
}

tasks.withType<PatchPluginXmlTask> {
	version.set(project.version.toString())
	sinceBuild.set("231")
	untilBuild.set("241")
}

tasks.withType<BuildSearchableOptionsTask> {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}
