package uk.co.jpmorgan.contactmanager.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class User {
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// OneToMany with Phone
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
	private List<Phone> phoneNumbers;

	// OneToOne with Address
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
	private Address address;

	@Column(nullable = false, length = 20)
	private String firstName;

	@Column(length = 20)
	private String middleName;

	@Column(nullable = false, length = 20)
	private String lastName;

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
