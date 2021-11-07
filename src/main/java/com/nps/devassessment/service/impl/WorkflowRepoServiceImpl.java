package com.nps.devassessment.service.impl;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.repo.WorkflowRepo;
import com.nps.devassessment.service.WorkflowRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkflowRepoServiceImpl implements WorkflowRepoService {

  private WorkflowRepo workflowRepo;

  @Autowired
  WorkflowRepoServiceImpl(WorkflowRepo workflowRepo) {
    this.workflowRepo = workflowRepo;
  }


  @Override
  public WorkflowEntity findWorkflowById(Long id) {
    return this.workflowRepo.findById(id).orElse(null);
  }

  @Override
  public List<WorkflowEntity> findWorkflowsByState(final String workflowState) {
    return this.workflowRepo.findByWorkflowState(workflowState);
  }

  @Override
  public List<WorkflowEntity> findWorkflowsByYjbYpIds(final List<Long> yjbYpIds) {
    return this.workflowRepo.findByYjbYpIds(yjbYpIds);
  }

  @Override
  public List<WorkflowEntity> findWorkflowsByCreatedDate(final Timestamp date) {
    return this.workflowRepo.findByCreatedGreaterThan(date);
  }

  @Override
  public List<WorkflowEntity> findWorkflowsByModifiedBetweenDates(final Timestamp fromDate, final Timestamp toDate) {
    return this.workflowRepo.findByModifiedBetween(fromDate, toDate);
  }

  @Override
  public List<WorkflowEntity> findWorkflowsByProcessAndTaskStatus(final String process, final String taskStatus) {
    return this.workflowRepo.findByProcessAndTaskStatusIsNot(process, taskStatus);
  }

  @Override
  public List<WorkflowEntity.BasicInfo> findIdYjbYpIdAndTaskStatusByCreatedBy(final String createdBy) {
    return this.workflowRepo.findIdYjbYpIdAndTaskStatusByCreatedBy(createdBy);
  }

  @Override
  public List<WorkflowEntity> findFirst10ByProcessOrderByIdDesc(final String process) {
    return this.workflowRepo.findFirst10ByProcessOrderByIdDesc(process);
  }

  @Override
  public Page<WorkflowEntity> findPage(final int pageNumber) {
    Pageable pageable = PageRequest.of(pageNumber, 50);
    return this.workflowRepo.findAll(pageable);
  }

  @Override
  public List<WorkflowEntity> findAll() {
    List<WorkflowEntity> workflowEntities = new ArrayList<>();
    this.workflowRepo.findAll().forEach(workflowEntity -> workflowEntities.add(workflowEntity));
    return workflowEntities;
  }
}
