package ru.quipy.projection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "users")
data class UserProjection(
        @Id
        val userId: UUID,
        val username: String,
        val email: String
)
