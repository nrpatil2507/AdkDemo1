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

import com.axelor.db.ValueEnum;

public enum OrderStatus implements ValueEnum<String> {

	DRAFT("draft"),

	OPEN("open"),

	CLOSED("closed"),

	CANCELED("canceled");

	private final String value;

	private OrderStatus(String value) {
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public String getValue() {
		return value;
	}
}
