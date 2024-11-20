package ru.quipy.api.project

import ru.quipy.core.annotations.DomainEvent
import ru.quipy.domain.Event
import ru.quipy.projection.ParticipantProjection
import java.util.*

const val PROJECT_CREATED_EVENT = "PROJECT_CREATED_EVENT"
const val PARTICIPANT_ADDED_EVENT = "PARTICIPANT_ADDED_EVENT"

@DomainEvent(name = PROJECT_CREATED_EVENT)
class ProjectCreatedEvent(
        val projectId: UUID,
        val title: String,
        val creatorId: String,
        val participants: List<ParticipantProjection>,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = PROJECT_CREATED_EVENT,
        createdAt = createdAt,
)

@DomainEvent(name = PARTICIPANT_ADDED_EVENT)
class ParticipantAddedEvent(
        val projectId: UUID,
        val userId: String,
        createdAt: Long = System.currentTimeMillis(),
) : Event<ProjectAggregate>(
        name = PARTICIPANT_ADDED_EVENT,
        createdAt = createdAt
)
