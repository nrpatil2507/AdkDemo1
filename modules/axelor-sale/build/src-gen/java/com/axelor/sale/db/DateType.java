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
package com.axelor.sale.db;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "SALE_DATE_TYPE")
public class DateType extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_DATE_TYPE_SEQ")
	@SequenceGenerator(name = "SALE_DATE_TYPE_SEQ", sequenceName = "SALE_DATE_TYPE_SEQ", allocationSize = 1)
	private Long id;

	private LocalDate localDate;

	private LocalTime localTime;

	private ZonedDateTime localDateTime;

	private Boolean status = Boolean.FALSE;

	private Integer numberData = 0;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public DateType() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public LocalTime getLocalTime() {
		return localTime;
	}

	public void setLocalTime(LocalTime localTime) {
		this.localTime = localTime;
	}

	public ZonedDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(ZonedDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public Boolean getStatus() {
		return status == null ? Boolean.FALSE : status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getNumberData() {
		return numberData == null ? 0 : numberData;
	}

	public void setNumberData(Integer numberData) {
		this.numberData = numberData;
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
		if (!(obj instanceof DateType)) return false;

		final DateType other = (DateType) obj;
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
			.add("localDate", getLocalDate())
			.add("localTime", getLocalTime())
			.add("localDateTime", getLocalDateTime())
			.add("status", getStatus())
			.add("numberData", getNumberData())
			.omitNullValues()
			.toString();
	}
}
