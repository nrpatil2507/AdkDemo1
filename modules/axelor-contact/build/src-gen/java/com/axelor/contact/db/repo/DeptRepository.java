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
package com.axelor.contact.db.repo;

import com.axelor.contact.db.Dept;
import com.axelor.db.JpaRepository;
import com.axelor.db.Query;

public class DeptRepository extends JpaRepository<Dept> {

	public DeptRepository() {
		super(Dept.class);
	}

	public Dept findByCode(String code) {
		return Query.of(Dept.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

	public Dept findbycode(String code) {
		return Query.of(Dept.class)
				.filter("self.code = :code")
				.bind("code", code)
				.fetchOne();
	}

}

