package com.flong.kotlin.modules.controller

import com.flong.kotlin.modules.service.impl.ConcurrentTasksExecutor
import com.flong.kotlin.modules.service.impl.MockTask
import com.flong.kotlin.utils.dto.ErrorResponse
import com.flong.kotlin.utils.dto.R
import com.flong.kotlin.utils.dto.TaskResponse
import org.springframework.http.HttpStatus
import org.springframework.util.StopWatch
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import java.util.stream.IntStream

@RestController
@RequestMapping("/tasks")
class TasksController {

    @GetMapping("/sequential")
    fun sequential(@RequestParam("task") taskDelaysInSeconds: IntArray): R<TaskResponse> {

        val watch = StopWatch()
        watch.start()

        IntStream.of(*taskDelaysInSeconds)
                .mapToObj{
                    MockTask(it)
                }
                .forEach{
                    it.execute()
                }

        watch.stop()
        return R(TaskResponse(watch.totalTimeSeconds))
    }

    @GetMapping("/concurrent")
    fun concurrent(@RequestParam("task") taskDelaysInSeconds: IntArray, @RequestParam("threads",required = false,defaultValue = "1") numberOfConcurrentThreads: Int): R<TaskResponse> {

        val watch = StopWatch()
        watch.start()

        val delayedTasks = IntStream.of(*taskDelaysInSeconds)
                .mapToObj{
                    MockTask(it)
                }
                .collect(Collectors.toList())

        ConcurrentTasksExecutor(numberOfConcurrentThreads, delayedTasks).execute()

        watch.stop()
        return R(TaskResponse(watch.totalTimeSeconds))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(e: IllegalArgumentException) = ErrorResponse(e.message)
}