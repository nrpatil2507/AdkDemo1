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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
import com.axelor.auth.db.User;
import com.axelor.db.annotations.VirtualColumn;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;

@Entity
@Cacheable
@DynamicInsert
@DynamicUpdate
@Table(name = "PROJECT_TASK", indexes = { @Index(columnList = "name"), @Index(columnList = "project"), @Index(columnList = "user_id"), @Index(columnList = "parent_task") })
public class ProjectTask extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_TASK_SEQ")
	@SequenceGenerator(name = "PROJECT_TASK_SEQ", sequenceName = "PROJECT_TASK_SEQ", allocationSize = 1)
	private Long id;

	@Widget(title = "Task Summary")
	@NotNull
	private String name;

	@Widget(multiline = true)
	private String notes;

	@Widget(selection = "project.task.state.selection")
	private String state;

	private Integer priority = 0;

	@NotNull
	private LocalDateTime createDate;

	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private LocalDate deadlineDate;

	@VirtualColumn
	@Access(AccessType.PROPERTY)
	private Integer progress = 0;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Project project;

	@NotNull
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private User user;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProjectWork> workItems;

	private LocalDateTime plannedStartDate;

	private BigDecimal plannedDuration = BigDecimal.ZERO;

	private BigDecimal plannedProgress = BigDecimal.ZERO;

	@Widget(title = "Parent task")
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private ProjectTask parentTask;

	@Widget(title = "Sequence")
	private Integer sequence = 0;

	@Widget(title = "Tasks to finish before start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "PROJECT_TASK_F2S", joinColumns = @JoinColumn(name = "from_task_id"), inverseJoinColumns = @JoinColumn(name = "to_task_id"))
	private Set<ProjectTask> finishToStartTaskSet;

	@Widget(title = "Tasks to start before start")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "PROJECT_TASK_S2S", joinColumns = @JoinColumn(name = "from_task_id"), inverseJoinColumns = @JoinColumn(name = "to_task_id"))
	private Set<ProjectTask> startToStartTaskSet;

	@Widget(title = "Tasks to finish before finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "PROJECT_TASK_F2F", joinColumns = @JoinColumn(name = "from_task_id"), inverseJoinColumns = @JoinColumn(name = "to_task_id"))
	private Set<ProjectTask> finishToFinishTaskSet;

	@Widget(title = "Tasks to start before finish")
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "PROJECT_TASK_S2F", joinColumns = @JoinColumn(name = "from_task_id"), inverseJoinColumns = @JoinColumn(name = "to_task_id"))
	private Set<ProjectTask> startToFinishaskSet;

	@Widget(title = "Attributes")
	@Basic(fetch = FetchType.LAZY)
	@Type(type = "json")
	private String attrs;

	public ProjectTask() {
	}

	public ProjectTask(String name) {
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getPriority() {
		return priority == null ? 0 : priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDate getDeadlineDate() {
		return deadlineDate;
	}

	public void setDeadlineDate(LocalDate deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public Integer getProgress() {
		try {
			progress = computeProgress();
		} catch (NullPointerException e) {
			Logger logger = LoggerFactory.getLogger(getClass());
			logger.error("NPE in function field: getProgress()", e);
		}
		return progress;
	}

	protected Integer computeProgress() {
		double result = 0.0, duration = 0.0;
		if (workItems == null || startDate == null || endDate == null) {
		  return 0;
		}
		for(ProjectWork work : workItems) {
		  java.time.LocalTime time = work.getHours();
		  result += time.getHour();
		  result += time.getMinute() / 60.0;
		}
		duration = java.time.temporal.ChronoUnit.HOURS.between(startDate, endDate);

		result = Math.min(100.0, ((result / duration) * 100));
		return (int) Math.round(result);
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ProjectWork> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(List<ProjectWork> workItems) {
		this.workItems = workItems;
	}

	/**
	 * Add the given {@link ProjectWork} item to the {@code workItems}.
	 *
	 * <p>
	 * It sets {@code item.task = this} to ensure the proper relationship.
	 * </p>
	 *
	 * @param item
	 *            the item to add
	 */
	public void addWorkItem(ProjectWork item) {
		if (getWorkItems() == null) {
			setWorkItems(new ArrayList<>());
		}
		getWorkItems().add(item);
		item.setTask(this);
	}

	/**
	 * Remove the given {@link ProjectWork} item from the {@code workItems}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeWorkItem(ProjectWork item) {
		if (getWorkItems() == null) {
			return;
		}
		getWorkItems().remove(item);
	}

	/**
	 * Clear the {@code workItems} collection.
	 *
	 * <p>
	 * If you have to query {@link ProjectWork} records in same transaction, make
	 * sure to call {@link javax.persistence.EntityManager#flush() } to avoid
	 * unexpected errors.
	 * </p>
	 */
	public void clearWorkItems() {
		if (getWorkItems() != null) {
			getWorkItems().clear();
		}
	}

	public LocalDateTime getPlannedStartDate() {
		return plannedStartDate;
	}

	public void setPlannedStartDate(LocalDateTime plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}

	public BigDecimal getPlannedDuration() {
		return plannedDuration == null ? BigDecimal.ZERO : plannedDuration;
	}

	public void setPlannedDuration(BigDecimal plannedDuration) {
		this.plannedDuration = plannedDuration;
	}

	public BigDecimal getPlannedProgress() {
		return plannedProgress == null ? BigDecimal.ZERO : plannedProgress;
	}

	public void setPlannedProgress(BigDecimal plannedProgress) {
		this.plannedProgress = plannedProgress;
	}

	public ProjectTask getParentTask() {
		return parentTask;
	}

	public void setParentTask(ProjectTask parentTask) {
		this.parentTask = parentTask;
	}

	public Integer getSequence() {
		return sequence == null ? 0 : sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Set<ProjectTask> getFinishToStartTaskSet() {
		return finishToStartTaskSet;
	}

	public void setFinishToStartTaskSet(Set<ProjectTask> finishToStartTaskSet) {
		this.finishToStartTaskSet = finishToStartTaskSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code finishToStartTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToStartTaskSetItem(ProjectTask item) {
		if (getFinishToStartTaskSet() == null) {
			setFinishToStartTaskSet(new HashSet<>());
		}
		getFinishToStartTaskSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code finishToStartTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToStartTaskSetItem(ProjectTask item) {
		if (getFinishToStartTaskSet() == null) {
			return;
		}
		getFinishToStartTaskSet().remove(item);
	}

	/**
	 * Clear the {@code finishToStartTaskSet} collection.
	 *
	 */
	public void clearFinishToStartTaskSet() {
		if (getFinishToStartTaskSet() != null) {
			getFinishToStartTaskSet().clear();
		}
	}

	public Set<ProjectTask> getStartToStartTaskSet() {
		return startToStartTaskSet;
	}

	public void setStartToStartTaskSet(Set<ProjectTask> startToStartTaskSet) {
		this.startToStartTaskSet = startToStartTaskSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code startToStartTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToStartTaskSetItem(ProjectTask item) {
		if (getStartToStartTaskSet() == null) {
			setStartToStartTaskSet(new HashSet<>());
		}
		getStartToStartTaskSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code startToStartTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToStartTaskSetItem(ProjectTask item) {
		if (getStartToStartTaskSet() == null) {
			return;
		}
		getStartToStartTaskSet().remove(item);
	}

	/**
	 * Clear the {@code startToStartTaskSet} collection.
	 *
	 */
	public void clearStartToStartTaskSet() {
		if (getStartToStartTaskSet() != null) {
			getStartToStartTaskSet().clear();
		}
	}

	public Set<ProjectTask> getFinishToFinishTaskSet() {
		return finishToFinishTaskSet;
	}

	public void setFinishToFinishTaskSet(Set<ProjectTask> finishToFinishTaskSet) {
		this.finishToFinishTaskSet = finishToFinishTaskSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code finishToFinishTaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addFinishToFinishTaskSetItem(ProjectTask item) {
		if (getFinishToFinishTaskSet() == null) {
			setFinishToFinishTaskSet(new HashSet<>());
		}
		getFinishToFinishTaskSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code finishToFinishTaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeFinishToFinishTaskSetItem(ProjectTask item) {
		if (getFinishToFinishTaskSet() == null) {
			return;
		}
		getFinishToFinishTaskSet().remove(item);
	}

	/**
	 * Clear the {@code finishToFinishTaskSet} collection.
	 *
	 */
	public void clearFinishToFinishTaskSet() {
		if (getFinishToFinishTaskSet() != null) {
			getFinishToFinishTaskSet().clear();
		}
	}

	public Set<ProjectTask> getStartToFinishaskSet() {
		return startToFinishaskSet;
	}

	public void setStartToFinishaskSet(Set<ProjectTask> startToFinishaskSet) {
		this.startToFinishaskSet = startToFinishaskSet;
	}

	/**
	 * Add the given {@link ProjectTask} item to the {@code startToFinishaskSet}.
	 *
	 * @param item
	 *            the item to add
	 */
	public void addStartToFinishaskSetItem(ProjectTask item) {
		if (getStartToFinishaskSet() == null) {
			setStartToFinishaskSet(new HashSet<>());
		}
		getStartToFinishaskSet().add(item);
	}

	/**
	 * Remove the given {@link ProjectTask} item from the {@code startToFinishaskSet}.
	 *
 	 * @param item
	 *            the item to remove
	 */
	public void removeStartToFinishaskSetItem(ProjectTask item) {
		if (getStartToFinishaskSet() == null) {
			return;
		}
		getStartToFinishaskSet().remove(item);
	}

	/**
	 * Clear the {@code startToFinishaskSet} collection.
	 *
	 */
	public void clearStartToFinishaskSet() {
		if (getStartToFinishaskSet() != null) {
			getStartToFinishaskSet().clear();
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
		if (!(obj instanceof ProjectTask)) return false;

		final ProjectTask other = (ProjectTask) obj;
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
			.add("notes", getNotes())
			.add("state", getState())
			.add("priority", getPriority())
			.add("createDate", getCreateDate())
			.add("startDate", getStartDate())
			.add("endDate", getEndDate())
			.add("deadlineDate", getDeadlineDate())
			.add("plannedStartDate", getPlannedStartDate())
			.add("plannedDuration", getPlannedDuration())
			.add("plannedProgress", getPlannedProgress())
			.omitNullValues()
			.toString();
	}
}
