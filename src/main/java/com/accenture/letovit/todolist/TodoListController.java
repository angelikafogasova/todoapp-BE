package com.accenture.letovit.todolist;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.letovit.todolist.database.DbTodoItem;
import com.accenture.letovit.todolist.database.DbTodoItemRepository;
import com.accenture.letovit.todolist.database.TodoItemConverter;
import com.accenture.letovit.todolist.validator.TodoItemValidator;

@RestController
@CrossOrigin (origins = "http://localhost:3000")
public class TodoListController {
	
	 private DbTodoItemRepository repository;

	   public TodoListController(DbTodoItemRepository repository) {
	      this.repository = repository;
	   }

	
	@RequestMapping (value = "todos", method = RequestMethod.POST)
	public String addTodoItem (@RequestBody TodoItem request) {
		
		TodoItemValidator.validate(request);
		
		LocalDateTime now = LocalDateTime.now();
		
		String prettyDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(now);
		
		request.setCreatedAt(prettyDateTime);
		
		String id = UUID.randomUUID().toString();
		System.out.println("Aha, co som dostal: " + request);
		
		DbTodoItem dbTodoItem = TodoItemConverter.jsonToDbEntity(request, id);
		repository.save(dbTodoItem);
		
		return id;
	}
	
	@RequestMapping (value = "todos", method = RequestMethod.GET)
	public List<TodoItem> fetchAllTodoItems() {
		Iterable<DbTodoItem> dbTodoItemList = repository.findAll();
		
		List<TodoItem> todoItems = new ArrayList<TodoItem> ();
		
		for (DbTodoItem dbTodoItem : dbTodoItemList) {
			todoItems.add(TodoItemConverter.dbEntityToJson(dbTodoItem));
		}
		
		return todoItems;
	}
	@RequestMapping (value = "/todos/{identifier}", method = RequestMethod.DELETE)
	public void deleteTodoItem (@PathVariable String identifier) {	
		repository.deleteById(identifier);
	}
	
	@RequestMapping (value = "/todos/{identifier}", method = RequestMethod.PATCH)
	public void updateTodoItem (@PathVariable String identifier, @RequestBody UpdateRequest requestBody) {
		DbTodoItem dbTodoItem = repository.findById(identifier).get();
		dbTodoItem.setFinished(requestBody.isFinished());
		repository.save(dbTodoItem);
	}
}
