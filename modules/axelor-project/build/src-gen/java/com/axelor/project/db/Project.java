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
package com.axelor.project.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.User;
import com.axelor.db.annotations.HashKey;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@Table(name = "PROJECT_PROJECT", indexes = { @Index(columnList = "parent") })
public class Project extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_PROJECT_SEQ")
	@SequenceGenerator(name = "PROJECT_PROJECT_SEQ", sequenceName = "PROJECT_PROJECT_SEQ", allocationSize = 1)
	private Long id;

	@HashKey
	@Widget(translatable = true)
	@NotNull
	@Column(unique = true)
	private String name;

	@Widget(translatable = true)
	private String description;

	@Widget(multiline = true)
	private String notes;

	private Integer priority = 0;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project parent;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> members;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectTask> tasks;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public Project() {
	}

	public Project(String name) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Project getParent() {
		return parent;
	}

	public void setParent(Project parent) {
		this.parent = parent;
	}

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

	/**
	 * Add the given {@link User} item to the {@code members}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addMember(User item) {
		if (getMembers() == null) {
			setMembers(new HashSet<>());
		}
		getMembers().add(item);
	}

	/**
	 * Remove the given {@link User} item from the {@code members}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeMember(User item) {
		if (getMembers() == null) {
			return;
		}
		getMembers().remove(item);
	}

	/**
	 * Clear the {@code members} collection.
	 *
	 */
	public void clearMembers() {
		if (getMembers() != null) {
			getMembers().clear();
		}
	}

	public List<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ProjectTask> tasks) {
		this.tasks = tasks;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code tasks}.
	 *
	 * <p>
	 * It sets {@code item.project = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addTask(ProjectTask item) {
		if (getTasks() == null) {
			setTasks(new ArrayList<>());
		}
		getTasks().add(item);
		item.setProject(this);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code tasks}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeTask(ProjectTask item) {
		if (getTasks() == null) {
			return;
		}
		getTasks().remove(item);
	}

	/**
	 * Clear the {@code tasks} collection.
	 *
	 * <p>
	 * If you have to query {@link ProjectTask} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearTasks() {
		if (getTasks() != null) {
			getTasks().clear();
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
		if (!(obj instanceof Project)) return false;

		final Project other = (Project) obj;
		if (this.getId() != null || other.getId() != null) {
			return Objects.equals(this.getId(), other.getId());
		}

		if (!Objects.equals(getName(), other.getName())) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(1355342585, this.getName());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", getId())
			.add("name", getName())
			.add("description", getDescription())
			.add("notes", getNotes())
			.add("priority", getPriority())
			.omitNullValues()
			.toString();
	}
}
