package com.dimdarkevil.underlay

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.swing.JPanel

class ScreenshotPanel() : JPanel() {
	private var img: BufferedImage? = null
	private var w = 0
	private var h = 0

	init {
		this.isOpaque = true
	}

	data class Rct(
		val x1: Int,
		val y1: Int,
		val x2: Int,
		val y2: Int
	)

	override fun paintComponent(gg: Graphics) {
		super.paintComponent(gg)
		val bi = img ?: return
		val g = gg as Graphics2D

		val imgAr = bi.height.toDouble() / bi.width.toDouble()
		val panelAr = h.toDouble() / w.toDouble()

		val r = if (panelAr > imgAr) {
			// screen is skinnier - adjust y position
			val ah = (w.toDouble() * imgAr).toInt()
			val sy = (h - ah) / 2
			Rct(0, sy, w, sy + ah)
		} else {
			// image is skinnier - adjust x position
			val aw = (h.toDouble() / imgAr).toInt()
			val sx = (w - aw)
			Rct(sx, 0, sx+aw, h)
		}
		g.drawImage(bi, r.x1, r.y1, r.x2, r.y2, 0, 0, bi.width, bi.height, null)
	}

	fun setImage(bi: BufferedImage, ww: Int, hh: Int) {
		println("image size is: ${bi.width} x ${bi.height}")
		println("form size is: ${w} x ${h}")
		println("parent size is: ${parent.width} x ${parent.height}")
		img = bi
		w = ww
		h = hh
		repaint()
	}

}