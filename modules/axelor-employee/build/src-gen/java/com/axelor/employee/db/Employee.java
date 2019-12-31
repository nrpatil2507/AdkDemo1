/*
 * * Axelor Business Solutions
 * 
 * Copyright (C) 2005-2019 Axelor (<http://axelor.com>).
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
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "employee")
public class Employee extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_SEQ")
	@SequenceGenerator(name = "employee_SEQ", sequenceName = "employee_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Size(max = 255)
	private String fname;

	@Size(max = 255)
	private String mname;

	private String lname;

	@Widget(multiline = true)
	private String address;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] profile;

	private Boolean status = Boolean.FALSE;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Income income;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employees", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Income> incomes;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Employee() {
	}

	public Employee(String fname, String mname, String lname) {
		this.fname = fname;
		this.mname = mname;
		this.lname = lname;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
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

	public List<Income> getIncomes() {
		return incomes;
	}

	public void setIncomes(List<Income> incomes) {
		this.incomes = incomes;
	}

	/**
	 * Add the given {@link Income} item to the {@code incomes}.
	 *
	 * <p>
	 * It sets {@code item.employees = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addIncome(Income item) {
		if (getIncomes() == null) {
			setIncomes(new ArrayList<>());
		}
		getIncomes().add(item);
		item.setEmployees(this);
	}

	/**
	 * Remove the given {@link Income} item from the {@code incomes}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeIncome(Income item) {
		if (getIncomes() == null) {
			return;
		}
		getIncomes().remove(item);
	}

	/**
	 * Clear the {@code incomes} collection.
	 *
	 * <p>
	 * If you have to query {@link Income} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearIncomes() {
		if (getIncomes() != null) {
			getIncomes().clear();
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
			.add("fname", getFname())
			.add("mname", getMname())
			.add("lname", getLname())
			.add("address", getAddress())
			.add("status", getStatus())
			.omitNullValues()
			.toString();
	}
}
