package com.nps.devassessment.service;

import com.nps.devassessment.entity.WorkflowEntity;

import java.sql.Timestamp;
import java.util.List;

public interface WorkflowRepoService {

  // fetch an individual workflow by its 'id'
  WorkflowEntity findWorkflowById(Long id);

  //fetch workflows by workflow state
  List<WorkflowEntity> findWorkflowsByState(final String workflowState);

  // fetch workflows by YJB_YP_ID
  List<WorkflowEntity> findWorkflowsByYjbYpIds(final List<Long> yjbYpIds);

  // fetch workflows by its 'created' date greater than given date
  List<WorkflowEntity> findWorkflowsByCreatedDate(final Timestamp date);

  // fetch workflows by its 'modified' column comparing between two dates
  List<WorkflowEntity> findWorkflowsByModifiedBetweenDates(final Timestamp fromDate, final Timestamp toDate);

  // fetch workflows where 'process' is equal to given value and 'taskStatus' NOT equal to given value
  List<WorkflowEntity> findWorkflowsByProcessAndTaskStatus(final String process, final String taskStatus);

  // fetch id, yjbYp and taskStatus by createdBy
  List<WorkflowEntity.BasicInfo> findIdYjbYpIdAndTaskStatusByCreatedBy(final String createdBy);

  // fetch top ten workflows by 'process' in id decending order
  List<WorkflowEntity> findFirst10ByProcessOrderByIdDesc(final String process);
}
