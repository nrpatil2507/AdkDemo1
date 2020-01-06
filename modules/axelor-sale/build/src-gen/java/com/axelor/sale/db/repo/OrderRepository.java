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
package com.axelor.sale.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.sale.db.Order;

public class OrderRepository extends JpaRepository<Order> {

	public OrderRepository() {
		super(Order.class);
	}

	public Order findByName(String name) {
		return Query.of(Order.class)
				.filter("self.name = :name")
				.bind("name", name)
				.fetchOne();
	}

	public Query<Order> findByCustomer(long id) {
		return Query.of(Order.class)
				.filter("self.customer.id = :id")
				.bind("id", id);
	}

	public Query<Order> findByCustomer(String email) {
		return Query.of(Order.class)
				.filter("self.customer.email = :email")
				.bind("email", email);
	}

}

