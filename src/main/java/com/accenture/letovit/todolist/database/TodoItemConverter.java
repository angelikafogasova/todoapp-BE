package com.accenture.letovit.todolist.database;

import com.accenture.letovit.todolist.TodoItem;

public class TodoItemConverter {

	public static DbTodoItem jsonToDbEntity(TodoItem input, String identifier) {
		DbTodoItem output = new DbTodoItem();

		output.setCreatedAt(input.getCreatedAt());
		output.setFinished(input.isFinished());
		output.setText(input.getText());
		output.setTitle(input.getTitle());
		output.setIdentifier(identifier);

		return output;
	}

	public static TodoItem dbEntityToJson(DbTodoItem input) {
		TodoItem output = new TodoItem();

		output.setCreatedAt(input.getCreatedAt());
		output.setFinished(input.isFinished());
		output.setText(input.getText());
		output.setTitle(input.getTitle());

		return output;
	}
}