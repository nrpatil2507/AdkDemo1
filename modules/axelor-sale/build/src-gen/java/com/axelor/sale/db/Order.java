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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Sequence;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "SALE_ORDER", indexes = { @Index(columnList = "customer"), @Index(columnList = "name") })
public class Order extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SALE_ORDER_SEQ")
	@SequenceGenerator(name = "SALE_ORDER_SEQ", sequenceName = "SALE_ORDER_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Customer customer;

	@Widget(readonly = true)
	@Sequence("sale.order.seq")
	private String name;

	@Basic
	@Type(type = "com.axelor.db.hibernate.type.ValueEnumType")
	private OrderStatus status = OrderStatus.DRAFT;

	@NotNull
	private LocalDate orderDate;

	private LocalDate confirmDate;

	private Boolean confirmed = Boolean.FALSE;

	@Digits(integer = 18, fraction = 2)
	private BigDecimal totalQty = BigDecimal.ZERO;

	private BigDecimal taxAmount = BigDecimal.ZERO;

	@Digits(integer = 18, fraction = 2)
	private BigDecimal totalAmount = BigDecimal.ZERO;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("price")
	private List<OrderLine> items;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Order() {
	}

	public Order(String name) {
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

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDate getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(LocalDate confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Boolean getConfirmed() {
		return confirmed == null ? Boolean.FALSE : confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public BigDecimal getTotalQty() {
		return totalQty == null ? BigDecimal.ZERO : totalQty;
	}

	public void setTotalQty(BigDecimal totalQty) {
		this.totalQty = totalQty;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount == null ? BigDecimal.ZERO : taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount == null ? BigDecimal.ZERO : totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<OrderLine> getItems() {
		return items;
	}

	public void setItems(List<OrderLine> items) {
		this.items = items;
	}

	/**
	 * Add the given {@link OrderLine} item to the {@code items}.
	 *
	 * <p>
	 * It sets {@code item.order = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addItem(OrderLine item) {
		if (getItems() == null) {
			setItems(new ArrayList<>());
		}
		getItems().add(item);
		item.setOrder(this);
	}

	/**
	 * Remove the given {@link OrderLine} item from the {@code items}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeItem(OrderLine item) {
		if (getItems() == null) {
			return;
		}
		getItems().remove(item);
	}

	/**
	 * Clear the {@code items} collection.
	 *
	 * <p>
	 * If you have to query {@link OrderLine} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearItems() {
		if (getItems() != null) {
			getItems().clear();
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
		if (!(obj instanceof Order)) return false;

		final Order other = (Order) obj;
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
			.add("name", getName())
			.add("status", getStatus())
			.add("orderDate", getOrderDate())
			.add("confirmDate", getConfirmDate())
			.add("confirmed", getConfirmed())
			.add("totalQty", getTotalQty())
			.add("taxAmount", getTaxAmount())
			.add("totalAmount", getTotalAmount())
			.omitNullValues()
			.toString();
	}
}
