package com.vynnyk.nurseapp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName; // імʼя файлу
	private String fileType; // тип файлу (наприклад image/jpeg, application/pdf)

	@Lob
	@Column(columnDefinition = "BYTEA") // для PostgreSQL
	private byte[] data;

	@ManyToOne
	@JoinColumn(name = "task_id")
	private Task task;
}