/*
 * * Axelor Business Solutions
 * 
 * Copyright (C) 2005-2020 Axelor (<http://axelor.com>).
 * 
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.contact.db;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "CONTACT_PERSON", indexes = { @Index(columnList = "title"), @Index(columnList = "fullName"), @Index(columnList = "company") })
public class Person extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_PERSON_SEQ")
	@SequenceGenerator(name = "CONTACT_PERSON_SEQ", sequenceName = "CONTACT_PERSON_SEQ", allocationSize = 1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Title title;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@Widget(search = { "firstName", "lastName" })
	@NameColumn
	private String fullName;

	private LocalDate dateOfBirth;

	@Widget(image = true, title = "Photo", help = "Max size 4MB.")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] image;

	@Widget(title = "About me")
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String notes;

	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private String email;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Email> emails;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Circle> circles;

	@Widget(massUpdate = true)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Company company;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Person() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Title getTitle() {
		return title;
	}

	public void setTitle(Title title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Max size 4MB.
	 *
	 * @return the property value
	 */
	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getEmail() {
		try {
			email = computeEmail();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getEmail()", e);
		}
		return email;
	}

	protected String computeEmail() {
		if (emails == null || emails.isEmpty()) return null;
		for (Email email : emails) if (email.getPrimary() == Boolean.TRUE) return email.getEmail();
		return emails.get(0).getEmail();
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * Add the given {@link Address} item to the {@code addresses}.
	 *
	 * <p>
	 * It sets {@code item.person = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addAddress(Address item) {
		if (getAddresses() == null) {
			setAddresses(new ArrayList<>());
		}
		getAddresses().add(item);
		item.setPerson(this);
	}

	/**
	 * Remove the given {@link Address} item from the {@code addresses}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeAddress(Address item) {
		if (getAddresses() == null) {
			return;
		}
		getAddresses().remove(item);
	}

	/**
	 * Clear the {@code addresses} collection.
	 *
	 * <p>
	 * If you have to query {@link Address} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearAddresses() {
		if (getAddresses() != null) {
			getAddresses().clear();
		}
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	/**
	 * Add the given {@link Contact} item to the {@code contacts}.
	 *
	 * <p>
	 * It sets {@code item.person = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addContact(Contact item) {
		if (getContacts() == null) {
			setContacts(new ArrayList<>());
		}
		getContacts().add(item);
		item.setPerson(this);
	}

	/**
	 * Remove the given {@link Contact} item from the {@code contacts}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeContact(Contact item) {
		if (getContacts() == null) {
			return;
		}
		getContacts().remove(item);
	}

	/**
	 * Clear the {@code contacts} collection.
	 *
	 * <p>
	 * If you have to query {@link Contact} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearContacts() {
		if (getContacts() != null) {
			getContacts().clear();
		}
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	/**
	 * Add the given {@link Email} item to the {@code emails}.
	 *
	 * <p>
	 * It sets {@code item.person = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addEmail(Email item) {
		if (getEmails() == null) {
			setEmails(new ArrayList<>());
		}
		getEmails().add(item);
		item.setPerson(this);
	}

	/**
	 * Remove the given {@link Email} item from the {@code emails}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeEmail(Email item) {
		if (getEmails() == null) {
			return;
		}
		getEmails().remove(item);
	}

	/**
	 * Clear the {@code emails} collection.
	 *
	 * <p>
	 * If you have to query {@link Email} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearEmails() {
		if (getEmails() != null) {
			getEmails().clear();
		}
	}

	public Set<Circle> getCircles() {
		return circles;
	}

	public void setCircles(Set<Circle> circles) {
		this.circles = circles;
	}

	/**
	 * Add the given {@link Circle} item to the {@code circles}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addCircle(Circle item) {
		if (getCircles() == null) {
			setCircles(new HashSet<>());
		}
		getCircles().add(item);
	}

	/**
	 * Remove the given {@link Circle} item from the {@code circles}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeCircle(Circle item) {
		if (getCircles() == null) {
			return;
		}
		getCircles().remove(item);
	}

	/**
	 * Clear the {@code circles} collection.
	 *
	 */
	public void clearCircles() {
		if (getCircles() != null) {
			getCircles().clear();
		}
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof Person)) return false;

		final Person other = (Person) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return 31;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("firstName", getFirstName())
			.add("lastName", getLastName())
			.add("fullName", getFullName())
			.add("dateOfBirth", getDateOfBirth())
			.omitNullValues()
			.toString();
	}
}
