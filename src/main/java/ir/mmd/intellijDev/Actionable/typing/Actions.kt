package ir.mmd.intellijDev.Actionable.typing

import ir.mmd.intellijDev.Actionable.util.ext.pascalCase
import ir.mmd.intellijDev.Actionable.util.ext.stringBuilder

class PredictCamelcaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = stringBuilder {
		append(words.removeFirst())
		append(words.joinToString("") { it.pascalCase })
	}
}

class PredictPascalCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("") { it.pascalCase }
}

class PredictSnakeCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("_")
}

class PredictKebabCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("-")
}

class PredictSpacedWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString(" ")
}
