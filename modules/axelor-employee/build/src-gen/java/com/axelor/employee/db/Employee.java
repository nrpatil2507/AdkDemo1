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
package com.axelor.employee.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "EMPLOYEE_EMPLOYEE", indexes = { @Index(columnList = "firstName") })
public class Employee extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPLOYEE_EMPLOYEE_SEQ")
	@SequenceGenerator(name = "EMPLOYEE_EMPLOYEE_SEQ", sequenceName = "EMPLOYEE_EMPLOYEE_SEQ", allocationSize = 1)
	private Long id;

	@NameColumn
	@NotNull
	@Size(max = 255)
	private String firstName;

	@Size(max = 255)
	private String lastName;

	@Widget(multiline = true)
	private String address;

	@Widget(image = true)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] profile;

	private Boolean status = Boolean.FALSE;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Income income;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Expense> expense;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Employee() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getProfile() {
		return profile;
	}

	public void setProfile(byte[] profile) {
		this.profile = profile;
	}

	public Boolean getStatus() {
		return status == null ? Boolean.FALSE : status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Income getIncome() {
		return income;
	}

	public void setIncome(Income income) {
		this.income = income;
	}

	public List<Expense> getExpense() {
		return expense;
	}

	public void setExpense(List<Expense> expense) {
		this.expense = expense;
	}

	/**
	 * Add the given {@link Expense} item to the {@code expense}.
	 *
	 * <p>
	 * It sets {@code item.employee = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addExpense(Expense item) {
		if (getExpense() == null) {
			setExpense(new ArrayList<>());
		}
		getExpense().add(item);
		item.setEmployee(this);
	}

	/**
	 * Remove the given {@link Expense} item from the {@code expense}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeExpense(Expense item) {
		if (getExpense() == null) {
			return;
		}
		getExpense().remove(item);
	}

	/**
	 * Clear the {@code expense} collection.
	 *
	 * <p>
	 * If you have to query {@link Expense} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearExpense() {
		if (getExpense() != null) {
			getExpense().clear();
		}
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
		if (!(obj instanceof Employee)) return false;

		final Employee other = (Employee) obj;
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
			.add("address", getAddress())
			.add("status", getStatus())
			.omitNullValues()
			.toString();
	}
}
