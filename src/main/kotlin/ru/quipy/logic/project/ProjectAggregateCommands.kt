package ru.quipy.logic.project

import ru.quipy.api.project.ParticipantAddedEvent
import ru.quipy.api.project.ProjectCreatedEvent
import ru.quipy.projection.ParticipantProjection
import java.util.*

fun ProjectAggregateState.createProject(id: UUID, title: String, creatorId: String, participants: List<ParticipantProjection> = emptyList()): ProjectCreatedEvent {
    return ProjectCreatedEvent(
            projectId = id,
            title = title,
            creatorId = creatorId,
            participants = participants
    )
}

fun ProjectAggregateState.addParticipant(
        projectId: UUID,
        userId: String,
): ParticipantAddedEvent {
    return ParticipantAddedEvent(
            projectId = projectId,
            userId = userId,
    )
}
