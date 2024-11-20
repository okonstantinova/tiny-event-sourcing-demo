package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.task.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.task.*
import ru.quipy.projection.TaskProjection
import ru.quipy.projection.StatusProjection
import ru.quipy.projection.ProjectProjection
import ru.quipy.repository.TaskProjectionRepository
import ru.quipy.repository.StatusProjectionRepository
import ru.quipy.repository.ProjectProjectionRepository
import ru.quipy.service.ProjectionsService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
        val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>,
        val projectionsService: ProjectionsService,
        val taskProjectionRepository: TaskProjectionRepository,
        val statusProjectionRepository: StatusProjectionRepository,
        val projectProjectionRepository: ProjectProjectionRepository
) {

    @PostMapping("/create")
    fun create(@RequestBody request: CreateTaskRequest): TaskCreatedEvent {

        val project = projectionsService.getProjectById(request.projectId)
                ?: throw NoSuchElementException("Project with ID ${request.projectId} not found")

        val status = projectionsService.getStatusById(request.statusId)
                ?: throw NoSuchElementException("Status with ID ${request.statusId} not found")

        val deadline = try {
            LocalDateTime.parse(request.deadlineTimestamp, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid deadline format: ${request.deadlineTimestamp}")
        }

        val taskId = UUID.randomUUID()

        val event = taskEsService.create {
            it.createTask(
                    id = taskId,
                    title = request.title,
                    description = request.description,
                    projectId = request.projectId,
                    deadlineTimestamp = deadline,
                    statusId = request.statusId
            )
        }

        val taskProjection = TaskProjection(
                taskId = event.taskId,
                title = event.title,
                description = event.description,
                projectId = event.projectId,
                deadlineTimestamp = event.deadlineTimestamp,
                statusId = event.statusId
        )

        taskProjectionRepository.save(taskProjection)

        return event
    }

    @PostMapping("/updateTitle")
    fun updateTitle(@RequestBody request: UpdateTaskTitleRequest): TaskTitleUpdatedEvent {
        val taskProjection = projectionsService.getTaskById(request.taskId)
                ?: throw NoSuchElementException("Task with ID ${request.taskId} not found")

        val event = taskEsService.update(request.taskId) {
            it.updateTitle(request.taskId, request.newTitle)
        }

        taskProjection.title = request.newTitle
        taskProjectionRepository.save(taskProjection)

        return event
    }

    @PostMapping("/updateDescription")
    fun updateDescription(@RequestBody request: UpdateTaskDescriptionRequest): TaskDescriptionUpdatedEvent {
        val taskProjection = projectionsService.getTaskById(request.taskId)
                ?: throw NoSuchElementException("Task with ID ${request.taskId} not found")

        val event = taskEsService.update(request.taskId) {
            it.updateDescription(request.taskId, request.newDescription)
        }

        taskProjection.description = request.newDescription
        taskProjectionRepository.save(taskProjection)

        return event
    }

    @PostMapping("/updateStatus")
    fun updateStatus(@RequestBody request: UpdateTaskStatusRequest): TaskStatusUpdatedEvent {
        val taskProjection = projectionsService.getTaskById(request.taskId)
                ?: throw NoSuchElementException("Task with ID ${request.taskId} not found")

        val status = projectionsService.getStatusById(request.newStatusId)
                ?: throw NoSuchElementException("Status with ID ${request.newStatusId} not found")

        val event = taskEsService.update(request.taskId) {
            it.updateStatus(request.taskId, request.newStatusId)
        }

        taskProjection.statusId = request.newStatusId
        taskProjectionRepository.save(taskProjection)

        return event
    }

    @GetMapping("/getById")
    fun getTaskById(@RequestParam taskId: UUID): TaskProjection {
        return projectionsService.getTaskById(taskId)
                ?: throw NoSuchElementException("Task with ID $taskId not found")
    }

    @GetMapping("/getStatusById")
    fun getStatusById(@RequestParam statusId: UUID): StatusProjection {
        return projectionsService.getStatusById(statusId)
                ?: throw NoSuchElementException("Status with ID $statusId not found")
    }

    @GetMapping("/getProjectById")
    fun getProjectById(@RequestParam projectId: UUID): ProjectProjection {
        return projectionsService.getProjectById(projectId)
                ?: throw NoSuchElementException("Project with ID $projectId not found")
    }
}

data class CreateTaskRequest(
        val title: String,
        val description: String,
        val projectId: UUID,
        val deadlineTimestamp: String,
        val statusId: UUID
)

data class UpdateTaskTitleRequest(
        val taskId: UUID,
        val newTitle: String
)

data class UpdateTaskDescriptionRequest(
        val taskId: UUID,
        val newDescription: String
)

data class UpdateTaskStatusRequest(
        val taskId: UUID,
        val newStatusId: UUID
)
