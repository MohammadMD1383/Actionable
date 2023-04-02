package ir.mmd.intellijDev.Actionable.ui

import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JPanel
import kotlin.reflect.KMutableProperty1

data class FeatureDescriptor<T>(
	val name: String,
	val property: KMutableProperty1<T, Boolean>,
	val description: String? = null
)

class FeatureToggleSettingsPage<T>(
	private val instance: T,
	private val descriptors: List<FeatureDescriptor<T>>
) : JPanel() {
	private val checkboxes: List<JBCheckBox> = descriptors.mapIndexed { i, it ->
		JBCheckBox(it.name, it.property.get(instance)).apply {
			actionCommand = i.toString()
		}
	}
	
	init {
		val rows = descriptors.size + descriptors.count { it.description != null } + 1
		layout = GridLayoutManager(rows, 1)
		
		val baseConstraints = GridConstraints(
			-1, 0, 1, 1,
			GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
			GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK,
			GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_CAN_SHRINK,
			Dimension(-1, -1), Dimension(-1, -1), Dimension(-1, -1)
		)
		
		checkboxes.forEachIndexed { i, cb ->
			add(cb, baseConstraints.apply { row++; indent = 0 })
			
			descriptors[i].description?.let {
				add(JBLabel(it), baseConstraints.apply { row++; indent = 2 })
			}
		}
		
		add(
			Box.createVerticalGlue(),
			baseConstraints.apply {
				row++
				indent = 0
				vSizePolicy = GridConstraints.SIZEPOLICY_CAN_GROW or GridConstraints.SIZEPOLICY_WANT_GROW
			}
		)
	}
	
	fun isModified(): Boolean {
		checkboxes.forEachIndexed { i, it ->
			if (it.isSelected != descriptors[i].property.get(instance)) {
				return true
			}
		}
		
		return false
	}
	
	fun save() = checkboxes.forEachIndexed { i, it ->
		descriptors[i].property.set(instance, it.isSelected)
	}
	
	fun reset() = checkboxes.forEachIndexed { i, it ->
		it.isSelected = descriptors[i].property.get(instance)
	}
}
