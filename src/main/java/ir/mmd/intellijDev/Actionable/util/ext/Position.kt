package ir.mmd.intellijDev.Actionable.util.ext

import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.VisualPosition

inline operator fun LogicalPosition.component1() = line
inline operator fun LogicalPosition.component2() = column

inline operator fun VisualPosition.component1() = line
inline operator fun VisualPosition.component2() = column
