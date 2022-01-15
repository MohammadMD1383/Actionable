@file:Suppress("PackageName")

package ir.mmd.intellijDev.Actionable.util.ext

/**
 * This file contains extension utility functions for kotlin sources
 */

/**
 * same as [MutableList.add] but **infix style**
 */
infix fun <T> MutableList<T>.addItem(element: T) {
	add(element)
}
