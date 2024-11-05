import org.springframework.web.bind.annotation.*
import ru.quipy.api.task.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.task.*
import java.util.*

@RestController
@RequestMapping("/tasks")
class TaskController(
        val taskEsService: EventSourcingService<UUID, TaskAggregate, TaskAggregateState>
) {
    @PostMapping("/create")
    fun create(
            @RequestParam title: String,
            @RequestParam description: String,
            @RequestParam projectId: UUID,
            @RequestParam deadlineTimestamp: Long,
            @RequestParam statusId: UUID
    ): TaskCreatedEvent {
        val taskId = UUID.randomUUID()

        return taskEsService.create { it.createTask(
                    id = taskId,
                    title = title,
                    description = description,
                    projectId = projectId,
                    deadlineTimestamp = deadlineTimestamp,
                    statusId = statusId
            )
        }
    }

    @PostMapping("/{taskId}/updateTitle")
    fun updateTitle(
            @PathVariable taskId: UUID,
            @RequestParam newTitle: String
    ): TaskTitleUpdatedEvent {
        return taskEsService.update(taskId) { it.updateTitle(taskId, newTitle) }
    }

    @PostMapping("/{taskId}/updateDescription")
    fun updateDescription(
            @PathVariable taskId: UUID,
            @RequestParam newDescription: String
    ): TaskDescriptionUpdatedEvent {
        return taskEsService.update(taskId) {
            it.updateDescription(taskId, newDescription)
        }
    }

    @PostMapping("/{taskId}/updateStatus")
    fun updateStatus(
            @PathVariable taskId: UUID,
            @RequestParam newStatusId: UUID
    ): TaskStatusUpdatedEvent {
        return taskEsService.update(taskId) { it.updateStatus(taskId, newStatusId) }
    }
}