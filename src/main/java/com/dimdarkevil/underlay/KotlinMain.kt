package com.dimdarkevil.underlay

import com.dimdarkevil.swingutil.loadConfig
import com.dimdarkevil.swingutil.saveConfig
import com.dimdarkevil.underlay.model.AppConfig
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.GraphicsEnvironment
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.UIManager

object KotlinMain {

	@JvmStatic
	fun main(args: Array<String>) {
		val config : AppConfig = loadConfig(APP_NAME, AppConfig())
		try {
			System.setProperty("apple.awt.application.name", APP_NAME)
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName())
		} catch (e: Exception) {
			// handle exception
		}
		val screenSize = Toolkit.getDefaultToolkit().screenSize
		val sds = GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices.toList()
		sds.forEach {
			println("${it.iDstring}: ${it.displayMode.width}x${it.displayMode.height} ${it.defaultConfiguration.bounds.x} ${it.defaultConfiguration.bounds.y}")
		}
		val sdMap = sds.associateBy { it.iDstring }
		val capDisp = sdMap[config.capDisplay] ?: sds.first()
		val presDisp = sdMap[config.presDisplay] ?: sds.last()
		if (config.capDisplay != capDisp.iDstring) config.capDisplay= capDisp.iDstring
		if (config.presDisplay != presDisp.iDstring) config.presDisplay = presDisp.iDstring

		val frame = JFrame(APP_NAME)
		val mainForm = PresForm(config, frame, presDisp, capDisp)
		frame.contentPane = mainForm.mainPanel
		frame.size = Dimension(screenSize.width / 2, screenSize.height / 2)

		//presDisp.fullScreenWindow = frame
		frame.isUndecorated = true
		frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		frame.addWindowListener(object: WindowAdapter() {
			override fun windowClosing(e: WindowEvent) {
				saveConfig(APP_NAME, config)
			}
		})
		EventQueue.invokeLater {frame.isVisible = true}
	}

}