package com.dimdarkevil.underlay

import com.dimdarkevil.swingutil.saveConfig
import com.dimdarkevil.underlay.model.AppConfig
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.KeyStroke

class PresForm(private val config: AppConfig, private val frame: JFrame, private var presScreen: GraphicsDevice, private var capScreen: GraphicsDevice) {
	val mainPanel = ScreenshotPanel()

	init {
		frame.preferredSize = Dimension(presScreen.displayMode.width, presScreen.displayMode.height)
		frame.location = Point(presScreen.defaultConfiguration.bounds.x, presScreen.defaultConfiguration.bounds.y)
		println("screen dim: ${presScreen.displayMode.width} x ${presScreen.displayMode.height}")
		frame.rootPane.registerKeyboardAction(::screencap, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW)
		frame.rootPane.registerKeyboardAction(::onExit, KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW)
		frame.rootPane.registerKeyboardAction(::onPrefs, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW)
	}

	private fun screencap(e: ActionEvent) {
		println("screencap: presScreen is ${presScreen.displayMode.width} x ${presScreen.displayMode.height}")
		Robot(capScreen).createScreenCapture(Rectangle(0, 0, capScreen.displayMode.width, capScreen.displayMode.height)).let { img ->
			mainPanel.setImage(img, presScreen.displayMode.width, presScreen.displayMode.height)
		}
	}

	private fun onExit(e: ActionEvent) {
		frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
	}

	private fun onPrefs(e: ActionEvent) {
		SettingsDlg(frame, "underlay preferences", config).showModal().let { (success, displays) ->
			if (success && displays != null) {
				config.capDisplay = displays.first
				config.presDisplay = displays.second
				saveConfig(APP_NAME, config)
			}
		}
	}
}