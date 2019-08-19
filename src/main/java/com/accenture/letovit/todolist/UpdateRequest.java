package com.accenture.letovit.todolist;

public class UpdateRequest {
	private boolean finished;

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	public boolean isFinished() {
		return finished;
	}
}