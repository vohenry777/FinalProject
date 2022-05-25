package com.skilldistillery.gaminghub.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// chat_user_chat
	// chat_user_user

	private String contents;
	private LocalDateTime created;

	@ManyToOne
	@JoinColumn(name = "chat_id")
	private Chat chat;

	public Message() {
		super();
	}

}
