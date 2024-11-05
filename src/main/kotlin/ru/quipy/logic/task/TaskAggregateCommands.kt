package ru.quipy.logic.task

import ru.quipy.api.task.*
import java.util.*

fun TaskAggregateState.createTask(
        id: UUID,
        title: String,
        description: String,
        projectId: UUID,
        deadlineTimestamp: Long,
        statusId: UUID
): TaskCreatedEvent {
    return TaskCreatedEvent(
            taskId = id,
            title = title,
            description = description,
            projectId = projectId,
            deadlineTimestamp = deadlineTimestamp,
            statusId = statusId
    )
}

fun TaskAggregateState.updateTitle(id: UUID, title: String): TaskTitleUpdatedEvent {
    return TaskTitleUpdatedEvent(
            taskId = id,
            newTitle = title
    )
}

fun TaskAggregateState.updateDescription(id: UUID, description: String): TaskDescriptionUpdatedEvent {
    return TaskDescriptionUpdatedEvent(
            taskId = id,
            newDescription = description
    )
}

fun TaskAggregateState.updateStatus(id: UUID, statusId: UUID): TaskStatusUpdatedEvent {
    return TaskStatusUpdatedEvent(
            taskId = id,
            newStatusId = statusId
    )
}

fun TaskAggregateState.addExecutor(id: UUID): TaskExecutorAddedEvent {
    if (taskExecutors.values.any { it.userId == id }) {
        throw IllegalArgumentException("User already exists: $id")
    }
    return TaskExecutorAddedEvent(
            taskId = this.getId(),
            executorId = id
    )
}

fun TaskAggregateState.deleteTask(projectId: UUID, id: UUID): TaskDeletedEvent {
    return TaskDeletedEvent(
            taskId = id,
            projectId = projectId
    )
}
