package com.example.todolist.mapper;

import com.example.todolist.domain.Task;
import com.example.todolist.requests.TaskPostRequestBody;
import com.example.todolist.requests.TaskPutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    public static final TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    public abstract Task toTask(TaskPostRequestBody taskPostRequestBody);
    public abstract Task toTask(TaskPutRequestBody taskPutRequestBody);
}
