package com.accenture.letovit.todolist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

	
	@RequestMapping (value = "todos.json", method = RequestMethod.POST)
	public SaveResponse addTodoItem (@RequestBody TodoItem request) {
		
		TodoItemValidator.validate(request);
		
		LocalDateTime now = LocalDateTime.now();
		
		String prettyDateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(now);
		
		request.setCreatedAt(prettyDateTime);
		
		String name = UUID.randomUUID().toString();
		System.out.println("Aha, co som dostal: " + request);
	
		SaveResponse response = new SaveResponse();
		response.setName(name);
		
		DbTodoItem dbTodoItem = TodoItemConverter.jsonToDbEntity(request, name);
		repository.save(dbTodoItem);
		
		return response;
	}
	
	@RequestMapping (value = "todos.json", method = RequestMethod.GET)
	public Map<String, TodoItem> fetchAllTodoItems() {
		Iterable<DbTodoItem> dbTodoItemList = repository.findAll();
		
		Map<String, TodoItem> todoItemsMap = new HashMap<String, TodoItem> ();
		
		for (DbTodoItem dbTodoItem : dbTodoItemList) {
			TodoItem todoItem = TodoItemConverter.dbEntityToJson(dbTodoItem);
			todoItemsMap.put(dbTodoItem.getIdentifier(), todoItem);
		}
		
		return todoItemsMap;
	}
	@RequestMapping (value = "/todos/{identifier}.json", method = RequestMethod.DELETE)
	public void deleteTodoItem (@PathVariable String identifier) {	
		repository.deleteById(identifier);
	}
	
	@RequestMapping (value = "/todos/{identifier}.json", method = RequestMethod.PATCH)
	public void updateTodoItem (@PathVariable String identifier, @RequestBody UpdateRequest requestBody) {
		DbTodoItem dbTodoItem = repository.findById(identifier).get();
		dbTodoItem.setFinished(requestBody.isFinished());
		repository.save(dbTodoItem);
	}
}
