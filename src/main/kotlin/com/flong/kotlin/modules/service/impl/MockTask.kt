package com.flong.kotlin.modules.service.impl

import com.flong.kotlin.modules.service.ITask
import java.util.concurrent.TimeUnit

class MockTask(private val delayInSeconds: Int) : ITask {

    /**
     * Stores information if task was started.
     */
    var started: Boolean = false

    /**
     * Stores information if task was successfully finished.
     */
    var finishedSuccessfully: Boolean = false

    /**
     * Stores information if the task was interrupted.
     * It can happen if the thread that is running this task was killed.
     */
    var interrupted: Boolean = false

    /**
     * Stores the thread identifier in which the task was executed.
     */
    var threadId: Long = 0

    override fun execute() {
        try {
            this.threadId = Thread.currentThread().id
            this.started = true
            TimeUnit.SECONDS.sleep(delayInSeconds.toLong())
            this.finishedSuccessfully = true
        } catch (e: InterruptedException) {
            this.interrupted = true
        }

    }
}