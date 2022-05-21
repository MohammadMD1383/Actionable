package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.LogicalPosition

inline operator fun LogicalPosition.component1(): Int = line
inline operator fun LogicalPosition.component2(): Int = column
