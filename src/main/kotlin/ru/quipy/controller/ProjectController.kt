package ru.quipy.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.quipy.api.project.ParticipantAddedEvent
import ru.quipy.api.project.ProjectAggregate
import ru.quipy.api.project.ProjectCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.project.ProjectAggregateState
import ru.quipy.logic.project.addParticipant
import ru.quipy.logic.project.createProject
import java.util.*

@RestController
@RequestMapping("/projects")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {

    @PostMapping("/{projectTitle}")
    fun createProject(@PathVariable projectTitle: String, @RequestParam creatorId: String) : ProjectCreatedEvent {
        return projectEsService.create { it.createProject(UUID.randomUUID(), projectTitle, creatorId) }
    }

    @PostMapping("/{projectId}/participants")
    fun addParticipant(
            @PathVariable projectId: UUID,
            @RequestParam userId: UUID,
            @RequestParam userName: String
    ): ParticipantAddedEvent {
        return projectEsService.update(projectId) { it.addParticipant(projectId, userId, userName) }
    }
}



