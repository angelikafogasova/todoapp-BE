package com.accenture.letovit.todolist.database;

import org.springframework.data.repository.CrudRepository;

public interface DbTodoItemRepository  extends CrudRepository<DbTodoItem, String>{

}
