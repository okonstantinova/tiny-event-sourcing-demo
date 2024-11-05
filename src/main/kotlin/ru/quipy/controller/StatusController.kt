package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.status.StatusCreatedEvent
import ru.quipy.api.status.StatusAggregate
import ru.quipy.api.status.StatusColorUpdatedEvent
import ru.quipy.api.status.StatusTitleUpdatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.status.StatusAggregateState
import ru.quipy.logic.status.createStatus
import ru.quipy.logic.status.updateColor
import ru.quipy.logic.status.updateTitle
import java.util.*

@RestController
@RequestMapping("/statuses")
class StatusController(
         val statusEsService: EventSourcingService<UUID, StatusAggregate, StatusAggregateState>
) {
    @PostMapping("/create")
    fun createStatus(
            @RequestParam name: String,
            @RequestParam color: String
    ): StatusCreatedEvent {
        return statusEsService.create { it.createStatus(name, color) }
    }

    @PostMapping("/{statusId}/updateTitle")
    fun updateTitle(
            @PathVariable statusId: UUID,
            @RequestParam newTitle: String
    ): StatusTitleUpdatedEvent {
        return statusEsService.update(statusId) { it.updateTitle(statusId, newTitle) }
    }

    @PostMapping("/{statusId}/updateColor")
    fun updateColor(
            @PathVariable statusId: UUID,
            @RequestParam newColor: String
    ): StatusColorUpdatedEvent {
        return statusEsService.update(statusId) { it.updateColor(statusId, newColor) }
    }
}
