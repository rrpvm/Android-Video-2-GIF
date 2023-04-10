package com.rrpvm.gif_loader.common

import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap

class GifRequestManager {
    private val jobMap = mutableMapOf<String, Job?>()
    private val rsMap = ConcurrentHashMap<String, SharedResource<Any>>()

    fun <T> getJobResource(jobNameId: String): SharedResource<T> {
        return if (rsMap[jobNameId] == null) {
            val resource = SharedResource<T>(null)
            rsMap[jobNameId] = resource as SharedResource<Any>
            resource
        } else rsMap[jobNameId]!! as SharedResource<T>
    }

    fun onJobStart(jobNameId: String, job: Job) {
        jobMap[jobNameId]?.cancel()
        jobMap[jobNameId] = job
    }

    fun onJobFinal(jobNameId: String) {
        jobMap[jobNameId] = null
        jobMap.remove(jobNameId)
        rsMap[jobNameId]?.clear()
        rsMap.remove(jobNameId)
    }

}