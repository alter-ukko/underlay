package com.dimdarkevil.underlay

import com.dimdarkevil.swingutil.EmUnit
import com.dimdarkevil.swingutil.OkCancelModalDialog
import com.dimdarkevil.swingutil.flowPanelWithLabel
import com.dimdarkevil.underlay.model.AppConfig
import java.awt.Dimension
import java.awt.GraphicsEnvironment
import javax.swing.BoxLayout
import javax.swing.JComboBox
import javax.swing.JFrame
import javax.swing.JPanel

class SettingsDlg(owner: JFrame, title: String, val config: AppConfig) : OkCancelModalDialog<Pair<String,String>>(owner, title) {
	private val sds = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices.toList()
	private val capCombo = JComboBox<String>(sds.map { it.iDstring }.toTypedArray())
	private val presCombo = JComboBox<String>(sds.map { it.iDstring }.toTypedArray())

	override fun buildUi(): JPanel {
		val settingsPanel = JPanel()
		settingsPanel.layout = BoxLayout(settingsPanel, BoxLayout.Y_AXIS)
		preferredSize = Dimension(EmUnit.EM_SIZE * 30, capCombo.preferredSize.height * 8)

		capCombo.isEditable = false
		presCombo.isEditable = false
		capCombo.selectedItem = config.capDisplay
		presCombo.selectedItem = config.presDisplay

		settingsPanel.add(flowPanelWithLabel("capture display:", capCombo))
		settingsPanel.add(flowPanelWithLabel("presentation display:", presCombo))

		return settingsPanel
	}

	override fun getResult(): Pair<String, String> {
		return Pair(capCombo.selectedItem as String, presCombo.selectedItem as String)
	}
}