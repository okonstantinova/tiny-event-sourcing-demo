package ru.quipy.projection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "tasks")
data class TaskProjection(
        @Id
        val taskId: UUID,
        val projectId: UUID,
        var title: String,
        var description: String,
        val deadlineTimestamp: LocalDateTime?,
        var statusId: UUID,
        val taskExecutors: Map<UUID, TaskExecutorProjection> = emptyMap()
)

data class TaskExecutorProjection(
        val executorId: UUID,
)
