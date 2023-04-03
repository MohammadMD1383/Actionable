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
		Spaced,
		Unknown
	}
	
	/**
	 * lazy-computed [CaseStyle] of [string]
	 */
	private val caseStyle: CaseStyle by lazy { string.detectCaseStyle() }
	
	/**
	 * detects [CaseStyle] of a string
	 */
	private fun String.detectCaseStyle() = when {
		' ' in this -> CaseStyle.Spaced
		
		this[0] in 'a'..'z' -> when {
			this.any { it in 'A'..'Z' } -> CaseStyle.CamelCase
			this.any { it == '_' } -> CaseStyle.LowerSnakeCase
			this.any { it == '-' } -> CaseStyle.KebabCase
			else -> CaseStyle.AllLowerCase
		}
		
		this[0] in 'A'..'Z' -> when {
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
			
			CaseStyle.Spaced -> string.lowercase().split(' ')
			
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
		CaseStyle.Spaced -> toSpaced()
		else -> string
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toAllUpperCase(): String {
		return split.joinToString("") { it.uppercase() }
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toAllLowerCase(): String {
		return split.joinToString("")
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toCamelCase(): String {
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
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toPascalCase(): String {
		return split.joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toLowerSnakeCase(): String {
		return split.joinToString("_")
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toUpperSnakeCase(): String {
		return split.joinToString("_") { it.uppercase() }
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toKebabCase(): String {
		return split.joinToString("-")
	}
	
	@Suppress("MemberVisibilityCanBePrivate")
	fun toSpaced(): String {
		return split.joinToString(" ")
	}
}
