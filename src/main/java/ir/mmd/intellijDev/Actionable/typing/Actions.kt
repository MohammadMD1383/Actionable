package ir.mmd.intellijDev.Actionable.typing

import ir.mmd.intellijDev.Actionable.internal.proguard.Keep
import ir.mmd.intellijDev.Actionable.util.ext.stringBuilder
import ir.mmd.intellijDev.Actionable.util.ext.titleCase

@Keep
class PredictCamelcaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = stringBuilder {
		append(words.removeFirst())
		append(words.joinToString("") { it.titleCase })
	}
}

@Keep
class PredictPascalCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("") { it.titleCase }
}

@Keep
class PredictSnakeCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("_")
}

@Keep
class PredictKebabCaseWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString("-")
}

@Keep
class PredictSpacedWordsAction : PredictWordsAction() {
	override fun transformWords(words: MutableList<String>) = words.joinToString(" ")
}
