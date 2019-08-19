package com.accenture.letovit.todolist.validator;

import com.accenture.letovit.todolist.*;

public class TodoItemValidator {

	public static void validate(TodoItem todoItem) {
		if (todoItem.getTitle().length() > 15) {
			throw new RuntimeException("Chyba, titulok je prilis dlhy.");
		}
		if (todoItem.getText().length() > 15) {
			throw new RuntimeException("Chyba, text je prilis dlhy.");
		}
		for (int i = 0; i < todoItem.getTitle().length(); i++) {
			Character znak = todoItem.getTitle().charAt(i);
			if (!Character.isLetterOrDigit(znak)) {
				throw new RuntimeException("Znak nie je ani cislo ani pismeno.");
			}
		}
	}

}
