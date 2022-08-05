package uk.co.jpmorgan.contactmanager.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "phone")
public class Phone {
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@Column(name = "international_dial_code", nullable = false, length = 3)
	private String internationalDialCode;

	@Column(name = "phone_number", nullable = false)
	private Long phoneNumber;

	@Column(name = "extension_number")
	private Integer extensionNumber;

	@Column(updatable = false)
	private LocalDate createdAt;
	private LocalDate updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDate.parse(LocalDate.now().toString(), dateFormatter);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDate.parse(LocalDate.now().toString(), dateFormatter);
	}
}
