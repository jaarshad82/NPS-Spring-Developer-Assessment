package com.nps.devassessment.repo;

import com.nps.devassessment.entity.WorkflowEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WorkflowRepo extends PagingAndSortingRepository<WorkflowEntity, Long> {

  List<WorkflowEntity> findByWorkflowState(final String workflowState);

  @Query("SELECT w FROM WorkflowEntity w WHERE w.yjbYp IN :yjbYpIds")
  List<WorkflowEntity> findByYjbYpIds(@Param("yjbYpIds") final List<Long> yjbYpIds);

  List<WorkflowEntity> findByCreatedGreaterThan(final Timestamp date);

  List<WorkflowEntity> findByModifiedBetween(final Timestamp fromDate, final Timestamp toDate);

  List<WorkflowEntity> findByProcessAndTaskStatusIsNot(final String process, final String taskStatus);

  @Query("SELECT w.id, w.yjbYp, w.taskStatus FROM WorkflowEntity w WHERE w.createdBy = :createdBy")
  List<WorkflowEntity.BasicInfo> findIdYjbYpIdAndTaskStatusByCreatedBy(@Param("createdBy") final String createdBy);

  List<WorkflowEntity> findFirst10ByProcessOrderByIdDesc(final String process);
}
