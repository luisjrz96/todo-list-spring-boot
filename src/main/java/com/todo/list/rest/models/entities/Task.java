package com.todo.list.rest.models.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="tasks")
public class Task implements Serializable{

	private static final long serialVersionUID = 6937906245519597410L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "title is required")
	@Column(name = "title", nullable = false, length = 50)
	private String title;

	@NotEmpty(message = "description is required")
	@Column(name="description", nullable = false, length = 350)
	private String description;


	@Column(name = "done", columnDefinition = "tinyint(1) default 0")
	private boolean done;


    @NotNull
    @Temporal(TemporalType.DATE)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@DateTimeFormat(iso = ISO.DATE)
	private Date dateTodo;

    @NotNull
    @Temporal(TemporalType.TIME)
	@JsonFormat(pattern = "hh:mm:ss")
	@DateTimeFormat(iso = ISO.TIME)
	private Date timeTodo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Date getDateTodo() {
		return dateTodo;
	}

	public void setDateTodo(Date dateTodo) {
		this.dateTodo = dateTodo;
	}

	public Date getTimeTodo() {
		return timeTodo;
	}

	public void setTimeTodo(Date timeTodo) {
		this.timeTodo = timeTodo;
	}
	
	
}
