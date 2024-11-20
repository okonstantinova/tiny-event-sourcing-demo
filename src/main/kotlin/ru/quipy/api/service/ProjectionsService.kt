package ru.quipy.service

import org.springframework.stereotype.Service
import ru.quipy.projection.*
import ru.quipy.repository.*
import java.util.*

@Service
class ProjectionsService(
        private val userProjectionRepository: UserProjectionRepository,
        private val statusProjectionRepository: StatusProjectionRepository,
        private val projectProjectionRepository: ProjectProjectionRepository,
        private val taskProjectionRepository: TaskProjectionRepository
) {
    fun getProjectById(projectId: UUID): ProjectProjection? {
        return projectProjectionRepository.findById(projectId).orElse(null)
    }

    fun getProjectsByUserId(userId: String): List<ProjectProjection> {
        return projectProjectionRepository.findByParticipantsUserIdContaining(userId)
    }

    fun getAllProjects(): List<ProjectProjection> {
        return projectProjectionRepository.findAll()
    }

    fun getTaskById(taskId: UUID): TaskProjection? {
        return taskProjectionRepository.findById(taskId).orElse(null)
    }

    fun getTaskExecutorById(userId: UUID): UserProjection? {
        return userProjectionRepository.findById(userId).orElse(null)
    }

    fun getUserById(userId: UUID): UserProjection? {
        return userProjectionRepository.findById(userId).orElse(null)
    }

    fun getStatusById(statusId: UUID): StatusProjection? {
        return statusProjectionRepository.findById(statusId).orElse(null)
    }

    fun getAllUserProjections(): List<UserProjection> {
        return userProjectionRepository.findAll()
    }

    fun getAllStatusProjections(): List<StatusProjection> {
        return statusProjectionRepository.findAll()
    }

    fun getAllProjectProjections(): List<ProjectProjection> {
        return projectProjectionRepository.findAll()
    }

    fun getAllTaskProjections(): List<TaskProjection> {
        return taskProjectionRepository.findAll()
    }

    fun getUserByEmail(email: String): UserProjection? {
        return userProjectionRepository.findByEmail(email)
    }
}
