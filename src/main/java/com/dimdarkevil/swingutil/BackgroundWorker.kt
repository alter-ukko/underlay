package com.dimdarkevil.swingutil

import org.slf4j.LoggerFactory
import javax.swing.SwingWorker

abstract class BackgroundWorker<T>(
	private val progressCallback: (Pair<String, Int>) -> Unit,
	private val doneCallback: (Boolean, String, T?) -> Unit,
	private val abortCallback: () -> Boolean
) : SwingWorker<Boolean, Pair<String,Int>>() {
	private val log = LoggerFactory.getLogger(BackgroundWorker::class.java)
	private var success = true
	private var aborted = false
	private var errorMsg = "Errors were encountered during the process"
	private var returnVal: T? = null

	/**
	 * Implementations should call `shouldAbort` regularly, and throw
	 * [AbortWorkerException] if it returns `true`. They should also
	 * call [publish] to post progress messages
	 */
	protected abstract fun exec(shouldAbort: () -> Boolean) : T

	override fun doInBackground(): Boolean {
		aborted = false
		try {
			returnVal = exec(abortCallback)
		} catch (ae: AbortWorkerException) {
			success = false
			aborted = true
			errorMsg = ae.localizedMessage
		} catch (e: Exception) {
			log.error(e.message, e)
			reportError(e.localizedMessage, true)
		}
		if (aborted) {
			publish(Pair("Process aborted by the user", 0))
		}
		return success
	}

	override fun done() {
		if (aborted) success = false
		val msg = if (success) "Process completed successfully" else errorMsg
		doneCallback(success, msg, returnVal)
	}

	override fun process(chunks: MutableList<Pair<String, Int>>) {
		chunks.forEach { chunk ->
			progressCallback(chunk)
		}
	}

	private fun reportError(msg: String, fatal: Boolean) {
		val mm = if (fatal) "ERROR: $msg" else msg
		publish(Pair(mm, 0))
		if (fatal) success = false
	}

}