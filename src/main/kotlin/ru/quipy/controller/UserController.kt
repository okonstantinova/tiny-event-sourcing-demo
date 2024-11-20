package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.api.user.*
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.user.*
import ru.quipy.projection.UserProjection
import ru.quipy.repository.UserProjectionRepository
import ru.quipy.service.ProjectionsService
import java.util.*
import java.util.regex.Pattern

@RestController
@RequestMapping("/users")
class UserController(
        val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
        val userProjectionRepository: UserProjectionRepository,
        val projectionsService: ProjectionsService,
) {
    @PostMapping("/create")
    fun addUser(@RequestBody request: CreateUserRequest): UserCreatedEvent {
        if (!isValidEmail(request.login)) {
            throw IllegalArgumentException("Invalid email format: ${request.login}")
        }

        val existingUser = projectionsService.getUserByEmail(request.login)
        if (existingUser != null) {
            throw IllegalArgumentException("User with email ${request.login} already exists")
        }

        val event = userEsService.create {
            it.createUser(UUID.randomUUID(), request.username, request.login, request.password)
        }

        val userProjection = UserProjection(
                userId = event.userId,
                username = event.username,
                email = request.login
        )

        userProjectionRepository.save(userProjection)

        return event
    }

    @GetMapping("/getUsers")
    fun getUsers(): List<UserProjection> {
        return userProjectionRepository.findAll()
    }

    @GetMapping("/getUserById/{userId}")
    fun getUserById(@PathVariable userId: UUID): UserProjection {
        return userProjectionRepository.findById(userId)
                .orElseThrow { IllegalArgumentException("User not found") }
    }

    @GetMapping("/getUserByEmail")
    fun getUserByEmail(@RequestParam email: String): UserProjection {
        return projectionsService.getUserByEmail(email)
                ?: throw NoSuchElementException("User with email $email not found")
    }
}

data class CreateUserRequest(
        val username: String,
        val login: String,
        val password: String
)

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    val pattern = Pattern.compile(emailRegex)
    return pattern.matcher(email).matches()
}
