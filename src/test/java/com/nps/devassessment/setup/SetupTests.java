package com.nps.devassessment.setup;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.service.WorkflowRepoService;
import org.hibernate.jdbc.Work;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SetupTests {

  private static final Logger log = LoggerFactory.getLogger(SetupTests.class);

  @Autowired
  private WorkflowRepoService workflowRepoService;


  // NOTE - This is a sample
  @Test
  public void test0_shouldProvideASampleOfAWorkingRepoCall() {

    // start test
    log.info("Starting test0 to demonstrate working repo call...");
    WorkflowEntity workflowEntity = this.workflowRepoService.findWorkflowById(66176L);

    // Assert
    assertNotNull(workflowEntity);
    assertEquals("IN PROGRESS", workflowEntity.getWorkflowState());

    // end test
    log.info("Workflow {} found.  yjb_yp_id = {}, workflow_state = {}", workflowEntity.getId(), workflowEntity.getYjbYp(), workflowEntity.getWorkflowState());
    log.info("test0 complete");
  }


  @Test
  public void test1_shouldDemonstrateRequestedRepoQueries() {
    // implement queries as per the word document
    // assert that the results of each of the query calls is not null/empty
    // write the count of each of the queries to the log

    // Select workflows by workflow_state = a given status  (e.g. “IN PROGRESS”, “CANCELLED”, “ADMITTED”)
    {
      log.info("Starting test1 to demonstrate selecting by workflow state");

      List<WorkflowEntity> actual = this.workflowRepoService.findWorkflowsByState("IN PROGRESS");

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by workflow state.", actual.size());
    }

    {
      // Select workflows by a given list of yjb_yp_id values  (e.g. 30848, 32524, 28117)
      log.info("Starting test1 to demonstrate selecting by a list of yjb_yp_id values");

      List<WorkflowEntity> actual = this.workflowRepoService.findWorkflowsByYjbYpIds(Arrays.asList(12078L, 22169L));

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by YJB_YP_ID.", actual.size());
    }

    {
      // Select workflows by 'created' column is after a given date (e.g. 01/02/2021)
      log.info("Starting test1 to demonstrate selecting by created date");

      List<WorkflowEntity> actual = this.workflowRepoService.findWorkflowsByCreatedDate(
          Timestamp.valueOf(LocalDateTime.of(2021, 3, 22, 0, 0)));

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by created date'.", actual.size());
    }

    {
      // Select workflows by 'modified' column is after a given date (e.g. 01/01/20) but before another given date (e.g. 01/03/2021)
      log.info("Starting test1 to demonstrate selecting by modified date between two dates");

      List<WorkflowEntity> actual = this.workflowRepoService.findWorkflowsByModifiedBetweenDates(
          Timestamp.valueOf(LocalDateTime.of(2021, 3, 22, 0, 0)),
          Timestamp.valueOf(LocalDateTime.of(2021, 4, 22, 0, 0)));

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by modified date between two dates'.", actual.size());
    }

    {
      // Select workflows by process = a given value (e.g. “placementProcess”) AND task_status != a given value (e.g.  “ADMITTED”)
      log.info("Starting test1 to demonstrate selecting by process and task status");

      List<WorkflowEntity> actual = this.workflowRepoService.findWorkflowsByProcessAndTaskStatus("placementProcess", "ADMITTED");

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by process and task status'.", actual.size());
    }

    {
      // Select id, yjb_yp_id and task_status columns for all workflows where created_by = a given value (e.g. “lee.everitt”)
      log.info("Starting test1 to demonstrate selecting by createdBy");

      List<WorkflowEntity.BasicInfo> actual = this.workflowRepoService.findIdYjbYpIdAndTaskStatusByCreatedBy("katherine.simmons");

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by createdBy column'.", actual.size());
    }

    {
      // Select the first 10 rows where process = a given value (e.g. “transferPlanned”).  Order the results by id in descending order
      log.info("Starting test1 to demonstrate selecting top ten by process in descending order");

      List<WorkflowEntity> actual = this.workflowRepoService.findFirst10ByProcessOrderByIdDesc("transferPlanned");

      assertNotNull(actual);
      assertTrue(0 < actual.size());

      log.info("{} workflows found querying by process column'.", actual.size());
    }

    log.info("test1 complete");
  }


  @Test
  public void test2_shouldDemonstratePageableRepoCapability() {
    // Page through the entire workflow repo using a page size of 20
    // For each page: write the count of each distinct workflow_status to the log
    // Once you have paged through the entire table, write the amount of pages to the log

    // Tom - the word doc says page size 50 and it says 20 in above comment...going with 50

    int pageNumber = 0;
    do {
      Page<WorkflowEntity> page = this.workflowRepoService.findPage(pageNumber);

      Set<String> workflowStatuses = page.get()
          .map(WorkflowEntity::getWorkflowState)
          .collect(Collectors.toSet());

      // page 20 has a null for workflow status...
      long nullWorkflowStatuses = page.get().filter(workflowEntity -> null == workflowEntity.getWorkflowState()).count();
      log.info("page {} has {} occurrences of null", page.getNumber() + 1, nullWorkflowStatuses);

      workflowStatuses.forEach(workflowStatus -> {
        if (null != workflowStatus) {
          List<WorkflowEntity> workflowEntities = page.get()
              .filter(x -> null != x.getWorkflowState() && x.getWorkflowState().equalsIgnoreCase(workflowStatus))
              .collect(Collectors.toList());
          log.info("page {} has {} occurrences of {}", page.getNumber() + 1, workflowEntities.size(), workflowStatus);
        }
      });
    } while (!this.workflowRepoService.findPage(pageNumber++).isLast());

    log.info("total pages: {}", pageNumber);
  }
}
