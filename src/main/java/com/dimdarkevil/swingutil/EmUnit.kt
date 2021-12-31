package com.dimdarkevil.swingutil

import javax.swing.JTextField

object EmUnit {
	val EM_SIZE = getEmSize()
	val LINE_HEIGHT = getLineHeight()

	fun getEmSize() : Int {
		val tf = JTextField()
		val font = tf.font
		println("font is ${font.family} ${font.name}")
		val s = "\u2014"
		val fm = tf.getFontMetrics(font)
		val mwidth = fm.stringWidth(s)
		println("em dash is $s - width is $mwidth")
		return mwidth
	}

	fun getLineHeight() : Int {
		val tf = JTextField()
		val font = tf.font
		val fm = tf.getFontMetrics(font)
		println("line height is ${fm.height}")
		return fm.height
	}

}