package com.skilldistillery.gaminghub.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "location")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String street;
	private String city;

	private String state;

	private Integer zip;
	private String country;
	private LocalDateTime created;
	private LocalDateTime updated;

	@ManyToMany
	@JoinTable(name = "user_location", 
	joinColumns = @JoinColumn(name = "location_id"), 
	inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> users;

	@ManyToMany
	@JoinTable(name = "meetup_location", 
	joinColumns = @JoinColumn(name = "location_id"), 
	inverseJoinColumns = @JoinColumn(name = "meetup_id"))
	private List<Meetup> meetups;
	
	@ManyToOne
	@JoinColumn(name="timezone_id")
	private Timezone timezone;

	public Location() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getZip() {
		return zip;
	}

	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}


	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Meetup> getMeetups() {
		return meetups;
	}

	public void setMeetups(List<Meetup> meetups) {
		this.meetups = meetups;
	}

	public Timezone getTimezone() {
		return timezone;
	}

	public void setTimezone(Timezone timezone) {
		this.timezone = timezone;
	}

	public void addUser(User user) {
		if (users == null) {
			users = new ArrayList<User>();
		}
		if (user != null) {
			users.add(user);
			user.addLocation(this);
		}
	}

	public void removeUser(User user) {
		if (user != null) {
			users.remove(user);
			user.removeLocation(this);
		}

	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", name=" + name + ", street=" + street + ", city=" + city
				+ ", state=" + state + ", zip=" + zip + ", country=" + country + ", timezone=" + timezone + ", created="
				+ created + ", updated=" + updated + "]";
	}
	
	
}
