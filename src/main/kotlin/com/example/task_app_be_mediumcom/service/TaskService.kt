package com.example.task_app_be_mediumcom.service

import com.example.task_app_be_mediumcom.data.Task
import com.example.task_app_be_mediumcom.data.model.TaskCreateRequest
import com.example.task_app_be_mediumcom.data.model.TaskDTO
import com.example.task_app_be_mediumcom.data.model.TaskUpdateRequest
import com.example.task_app_be_mediumcom.exception.BadRequestException
import com.example.task_app_be_mediumcom.exception.TaskNotFoundException
import com.example.task_app_be_mediumcom.repository.TaskRepository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.util.stream.Collectors
import kotlin.reflect.full.memberProperties

@Service
class TaskService(private val repository: TaskRepository) {
    private fun convertEntityToDto(task: Task): TaskDTO {
        return TaskDTO(
            task.id,
            task.description,
            task.isReminderSet,
            task.isTaskOpen,
            task.createdOn,
            task.priority
        )
    }

    private fun assignValuesToEntity(task: Task, taskRequest: TaskCreateRequest) {
        task.priority = taskRequest.priority
        task.isTaskOpen = taskRequest.isTaskOpen
        task.isReminderSet = taskRequest.isReminderSet
        task.description = taskRequest.description
    }

    private fun checkForTaskId(id: Long) {
        if (!repository.existsById(id)) {
            throw TaskNotFoundException("Task with id: $id does not exist")
        }
    }

    fun getAllTasks(): List<TaskDTO> {
        return repository.findAll().stream()
            .map(this::convertEntityToDto).collect(Collectors.toList())
    }

    fun getAllOpenTasks(): List<TaskDTO> {
        return repository.queryAllOpenTasks().stream()
            .map(this::convertEntityToDto).collect(Collectors.toList())
    }

    fun getAllClosedTasks(): List<TaskDTO> {
        return repository.queryAllClosedTasks().stream()
            .map(this::convertEntityToDto).collect(Collectors.toList())
    }

    fun getTaskById(id: Long): TaskDTO {
        checkForTaskId(id)
        val task: Task = repository.findTaskById(id)
        return convertEntityToDto(task)
    }

    fun createTask(createRequest: TaskCreateRequest): TaskDTO {
        if (repository.doesDescriptionExist(createRequest.description))
            throw BadRequestException("Task with description: ${createRequest.description} exist!")
        val task = Task()
        assignValuesToEntity(task, createRequest)
        val savedTask: Task = repository.save(task)
        return convertEntityToDto(savedTask)
    }


    fun updateTask(id: Long, updateRequest: TaskUpdateRequest) {
        checkForTaskId(id)
        val existingTask: Task = repository.findTaskById(id)

        for (prop in TaskUpdateRequest::class.memberProperties) {
            if (prop.get(updateRequest) != null) {
                val field = ReflectionUtils.findField(Task::class.java, prop.name)
                field?.let {
//                    it.access
                }
            }
        }

    }

}
