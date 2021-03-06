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

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.HashKey;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "SALE_PRODUCT", indexes = { @Index(columnList = "image"), @Index(columnList = "category") })
public class Product extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_PRODUCT_SEQ")
	@SequenceGenerator(name = "SALE_PRODUCT_SEQ", sequenceName = "SALE_PRODUCT_SEQ", allocationSize = 1)
	private Long id;

	@HashKey
	@NotNull
	@Size(min = 2)
	@Column(unique = true)
	private String code;

	@HashKey
	@NotNull
	@Size(min = 2)
	@Column(unique = true)
	private String name;

	@Widget(selection = "product.color.selection")
	private String color;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private MetaFile image;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProductCategory category;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "text")
	private String notes;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Product() {
	}

	public Product(String code, String name) {
		this.code = code;
		this.name = name;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public MetaFile getImage() {
		return image;
	}

	public void setImage(MetaFile image) {
		this.image = image;
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
		if (!(obj instanceof Product)) return false;

		final Product other = (Product) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		if (!Objects.equals(getCode(), other.getCode())) return false;
		if (!Objects.equals(getName(), other.getName())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(1355179215, this.getCode(), this.getName());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("code", getCode())
			.add("name", getName())
			.add("color", getColor())
			.omitNullValues()
			.toString();
	}
}
