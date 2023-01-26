import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.text.RegexOption.MULTILINE

buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.jetbrains.intellij") version "1.12.0"
	kotlin("jvm") version "1.8.0"
	java
}

repositories {
	mavenCentral()
}

dependencies {
	classpath("/Files/JetBrains/idea/223.7571.4/plugins/javascript-impl")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

fun DependencyHandlerScope.classpath(path: String) {
	compileOnly(fileTree("include" to "**/*.jar", "dir" to path))
}

group = "ir.mmd.intellijDev"
version = "3.14.0"

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = JavaVersion.VERSION_1_8.toString()
		freeCompilerArgs = freeCompilerArgs + listOf(
			"-Xjvm-default=all",
			"-Xcontext-receivers"
		)
	}
}

intellij {
	plugins.set(
		"java",
		"Kotlin"
	)
	
	// localPath.set("/Files/jetbrains/rider")
	// localPath.set("/Files/jetbrains/idea")
	// localPath.set("/Files/jetbrains/clion")
	// localPath.set("/Files/jetbrains/studio")
	
	type.set("IC")
	
	version.set("2022.3")
	// version.set("2021.3.1")
	// version.set("2020.1")
}

tasks.withType<RunIdeTask> {
	autoReloadPlugins.set(true)
}

task("patchPluginXmlFeatures") {
	val xmlFiles = listOf(
		"src/main/resources/META-INF/plugin.xml",
		"src/main/resources/META-INF/plugin-java.xml",
		"src/main/resources/META-INF/plugin-rider.xml",
		"src/main/resources/META-INF/plugin-javascript.xml"
	)
	
	val features = mutableListOf<String>()
	
	xmlFiles.forEach { xmlPath ->
		val xmlFile = file(xmlPath)
		val text = xmlFile.readText()
		val featuresPattern = """<action[\s][\S\s]*?text="(.*?)"[\S\s]*?\/>"""
		features += Regex(featuresPattern, MULTILINE).findAll(text).map { it.groupValues[1] }
	}
	
	val pluginXmlFile = file(xmlFiles[0])
	val text = pluginXmlFile.readText()
	val ulPattern = """<description>[\S\s]*?<ul>([\S\s]*?)<\/ul>[\S\s]*?<\/description>"""
	val range = Regex(ulPattern, MULTILINE).find(text)!!.groups[1]!!.range
	val featuresStr = "\n${features.joinToString("\n") { "<li>$it</li>" }}".prependIndent("\t\t\t") + "\n\t\t"
	val newText = text.replaceRange(range, featuresStr)
	pluginXmlFile.writeText(newText)
}

tasks.withType<PatchPluginXmlTask> {
	dependsOn("patchPluginXmlFeatures")
	version.set(project.version.toString())
	sinceBuild.set("201")
	untilBuild.set("") // to be always the latest version
}

tasks.withType<BuildSearchableOptionsTask> {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Helpers

fun <T> ListProperty<T>.set(vararg values: T) = set(listOf(*values))
