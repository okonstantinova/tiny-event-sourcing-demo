package ru.quipy.projection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "statuses")
data class StatusProjection(
        @Id
        val statusId: UUID,
        var title: String,
        var color: String
)
