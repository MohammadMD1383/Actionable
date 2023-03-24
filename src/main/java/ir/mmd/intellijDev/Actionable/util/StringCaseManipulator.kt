package ir.mmd.intellijDev.Actionable.util

import ir.mmd.intellijDev.Actionable.util.ext.buildString

class StringCaseManipulator(private val string: String) {
	enum class CaseStyle {
		AllUpperCase,
		AllLowerCase,
		CamelCase,
		PascalCase,
		LowerSnakeCase,
		UpperSnakeCase,
		KebabCase,
		Unknown
	}
	
	/**
	 * lazy-computed [CaseStyle] of [string]
	 */
	private val caseStyle: CaseStyle by lazy { string.detectCaseStyle() }
	
	/**
	 * detects [CaseStyle] of a string
	 */
	private fun String.detectCaseStyle() = when (this[0]) {
		in 'a'..'z' -> when {
			this.any { it in 'A'..'Z' } -> CaseStyle.CamelCase
			this.any { it == '_' } -> CaseStyle.LowerSnakeCase
			this.any { it == '-' } -> CaseStyle.KebabCase
			else -> CaseStyle.AllLowerCase
		}
		
		in 'A'..'Z' -> when {
			this.any { it in 'a'..'z' } -> CaseStyle.PascalCase
			this.any { it == '_' } -> CaseStyle.UpperSnakeCase
			else -> CaseStyle.AllUpperCase
		}
		
		else -> CaseStyle.Unknown
	}
	
	/**
	 * exploded version of [string] detected by [caseStyle]
	 *
	 * all parts guaranteed to be lowercase - the final transformer is in charge of changing case of the resulting string
	 */
	private val split: List<String> by lazy {
		when (caseStyle) {
			CaseStyle.AllUpperCase,
			CaseStyle.AllLowerCase,
			CaseStyle.Unknown -> listOf(string.lowercase())
			
			CaseStyle.LowerSnakeCase,
			CaseStyle.UpperSnakeCase -> string.lowercase().split('_')
			
			CaseStyle.KebabCase -> string.lowercase().split('-')
			
			CaseStyle.CamelCase,
			CaseStyle.PascalCase -> mutableListOf<String>().apply {
				var li = 0
				var ci = 0
				
				while (ci < string.length) {
					while (ci < string.length && string[ci].isUpperCase()) {
						ci++
					}
					
					while (ci < string.length && string[ci].isLowerCase()) {
						ci++
					}
					
					add(string.substring(li, ci).lowercase())
					li = ci
				}
			}
		}
	}
	
	fun toCaseStyleOf(other: String) = when (other.detectCaseStyle()) {
		CaseStyle.AllUpperCase -> toAllUpperCase()
		CaseStyle.AllLowerCase -> toAllLowerCase()
		CaseStyle.CamelCase -> toCamelCase()
		CaseStyle.PascalCase -> toPascalCase()
		CaseStyle.LowerSnakeCase -> toLowerSnakeCase()
		CaseStyle.UpperSnakeCase -> toUpperSnakeCase()
		CaseStyle.KebabCase -> toKebabCase()
		else -> string
	}
	
	private fun toAllUpperCase(): String {
		return split.joinToString("") { it.uppercase() }
	}
	
	private fun toAllLowerCase(): String {
		return split.joinToString("")
	}
	
	private fun toCamelCase(): String {
		if (split.isEmpty()) {
			return string
		}
		
		return buildString {
			append(split[0])
			
			for (i in 1..split.lastIndex) {
				append(split[i].replaceFirstChar { c -> c.uppercase() })
			}
		}
	}
	
	private fun toPascalCase(): String {
		return split.joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
	}
	
	private fun toLowerSnakeCase(): String {
		return split.joinToString("_")
	}
	
	private fun toUpperSnakeCase(): String {
		return split.joinToString("_") { it.uppercase() }
	}
	
	private fun toKebabCase(): String {
		return split.joinToString("-")
	}
}
