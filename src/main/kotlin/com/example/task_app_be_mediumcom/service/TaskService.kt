package com.example.task_app_be_mediumcom.service

import com.example.task_app_be_mediumcom.data.Task
import com.example.task_app_be_mediumcom.data.model.TaskCreateRequest
import com.example.task_app_be_mediumcom.data.model.TaskDTO
import com.example.task_app_be_mediumcom.exception.TaskNotFoundException
import com.example.task_app_be_mediumcom.repository.TaskRepository
import org.springframework.stereotype.Service

@Service
class TaskService(private val repository: TaskRepository) {
    private fun convertDtoToTask(task: Task): TaskDTO {
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
}
