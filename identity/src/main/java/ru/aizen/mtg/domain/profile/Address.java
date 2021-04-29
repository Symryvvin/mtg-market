package ru.aizen.mtg.domain.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "address")
public class Address {

	@Id
	@Column(name = "profile_id")
	private Long id;
	@OneToOne
	@MapsId
	@JoinColumn(name = "profile_id")
	private Profile profile;

	@Column(name = "post_index", nullable = false)
	private Integer postIndex;
	@Column(name = "settlement", nullable = false)
	private String settlement;
	@Column(name = "street", nullable = false)
	private String street;
	@Column(name = "building", nullable = false)
	private String building;
	@Column(name = "apartment", nullable = false)
	private String apartment;

	public String inline() {
		return postIndex + "\n" + settlement + ", ул. " + street + ", д. " + building + ", " + "кв. " + apartment;
	}
}
