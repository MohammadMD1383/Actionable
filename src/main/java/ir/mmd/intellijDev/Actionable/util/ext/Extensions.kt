@file:Suppress("PackageName")

package ir.mmd.intellijDev.Actionable.util.ext

infix fun <T> MutableList<T>.addItem(element: T) {
	add(element)
}
