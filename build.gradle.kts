import org.jetbrains.intellij.tasks.BuildSearchableOptionsTask
import org.jetbrains.intellij.tasks.PatchPluginXmlTask
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import proguard.gradle.ProGuardTask
import kotlin.text.RegexOption.MULTILINE

buildscript {
	repositories {
		mavenCentral()
	}
	
	dependencies {
		classpath("com.guardsquare:proguard-gradle:7.2.1")
	}
}

plugins {
	id("org.jetbrains.intellij") version "1.8.0"
	kotlin("jvm") version "1.7.0"
	java
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.0")
	
	classpath("/Files/jetbrains/idea/plugins/JavaScriptLanguage")
	
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

fun DependencyHandlerScope.classpath(path: String) {
	compileOnly(fileTree("include" to "**/*.jar", "dir" to path))
}

group = "ir.mmd.intellijDev"
version = "3.11.0"

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
	
	// version.set("IU-2022.1")
	version.set("2022.2")
	// version.set("2021.3.1")
	// version.set("2019.1.4")
	// version.set("IU-2018.1")
	// version.set("2017.1")
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
	sinceBuild.set("181")
	untilBuild.set("") // to be always the latest version
}

tasks.withType<BuildSearchableOptionsTask> {
	enabled = false
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<ProGuardTask>("minify") {
	dependsOn(tasks.buildPlugin)
	outputs.upToDateWhen { false }
	
	val javaModulesPath = "/usr/lib/jvm/java-17-openjdk-amd64/jmods"
	val inFile = tasks.buildPlugin.get().archiveFile.get().asFile
	val outFile = "build/minified/${inFile.name}"
	
	optimizationpasses(5)
	printmapping(outFile.replace(".zip", ".mapping"))
	
	dontnote()
	dontwarn("kotlin.**")
	
	libraryjars("$javaModulesPath/java.base.jmod")
	libraryjars("$javaModulesPath/java.desktop.jmod")
	libraryjars("$javaModulesPath/java.datatransfer.jmod")
	
	libraryjars("/Files/jetbrains/idea/lib")
	libraryjars("/Files/jetbrains/idea/plugins/java/lib")
	libraryjars("/Files/jetbrains/idea/plugins/Kotlin/lib")
	libraryjars("/Files/jetbrains/idea/plugins/JavaScriptLanguage/lib")
	
	injars(inFile)
	outjars(outFile)
	
	keepattributes("RuntimeVisibleAnnotations,Signature")
	keep("class kotlin.reflect.**")
	
	keep("@ir.mmd.intellijDev.Actionable.internal.proguard.Keep class *")
	keepclassmembers("class * { @ir.mmd.intellijDev.Actionable.internal.proguard.Keep *; }")
}

// Helpers

fun <T> ListProperty<T>.set(vararg values: T) = set(listOf(*values))
