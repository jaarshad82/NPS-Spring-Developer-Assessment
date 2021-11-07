package com.nps.devassessment.general;

import com.nps.devassessment.entity.WorkflowEntity;
import com.nps.devassessment.service.WorkflowRepoService;
import org.hibernate.cfg.NotYetImplementedException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GeneralTests {

  private static final Logger log = LoggerFactory.getLogger(GeneralTests.class);

  @Autowired
  private WorkflowRepoService workflowRepoService;

  @Test
  public void test1_shouldDemonstrateFilteringInLambdas() {
    // Use the query you created during the Setup tests to select all workflows with a workflow state of "IN PROGRESS"
    // Filter the results by task_status  NULL
    // With the resulting set of workflows, concatenate all of the id values into a comma-separated string and
    //    write that string to the log

    List<WorkflowEntity> workflows = this.workflowRepoService.findWorkflowsByState("IN PROGRESS");
    workflows = workflows.stream()
        .filter(workflow -> null == workflow.getTaskStatus())
        .collect(Collectors.toList());
    List<String> ids = workflows.stream()
        .map(workflow -> workflow.getId().toString())
        .collect(Collectors.toList());
    String resultStr = String.join(",", ids);
    log.info("id str {}", resultStr);
  }


  @Test
  public void test2_shouldDemonstrateIdentificationOfMinAndMaxValues() {
    // Use the query you created during the Setup Tests to select workflows by status = “placementProcess” and task_status is not “admitted”
    // Given the results of the query, identify the highest and the lowest yjb_yp_id
    // Write those two values to the log

    List<WorkflowEntity> resultList = this.workflowRepoService.findWorkflowsByProcessAndTaskStatus("placementProcess", "admitted");
    List<WorkflowEntity> sortedResultList = resultList.stream()
        .sorted(Comparator.comparing(WorkflowEntity::getYjbYp))
        .collect(Collectors.toList());

    log.info("lowest yjb_yp_id {}", sortedResultList.get(0).getYjbYp());
    log.info("highest yjb_yp_id {}", sortedResultList.get(sortedResultList.size() - 1).getYjbYp());
  }


  @Test
  public void test3_shouldDemonstrateModifyingAValueFromWithinALambda() {
    // Identify the lowest yjb_yp_id in the workflow table using whatever means you deem appropriate - store in a variable
    // Using a lambda, loop through all entries in the workflow table
    // For each workflow: If the yjb_yp_id is greater than the value you have stored in the variable, update the variable with the new value
    // After you have looped through all entries in the table, outside of the lambda write the highest yjb_yp_id to the log

    List<WorkflowEntity> workflowEntities = this.workflowRepoService.findAll();
    List<WorkflowEntity> sortedResultList = workflowEntities.stream()
        .sorted(Comparator.comparing(WorkflowEntity::getYjbYp))
        .collect(Collectors.toList());

    AtomicReference<Long> lowestYjbYp = new AtomicReference<>();
    lowestYjbYp.set(sortedResultList.get(0).getYjbYp());
    log.info("lowest yjb_yp_id {}", lowestYjbYp.get());

    workflowEntities.forEach(workflowEntity -> {
      lowestYjbYp.set(lowestYjbYp.get() < workflowEntity.getYjbYp() ? workflowEntity.getYjbYp() : lowestYjbYp.get());
    });
    log.info("highest yjb_yp_id {}", lowestYjbYp.get());
  }


  @Test
  public void test4_shouldDemonstrateUsingParallelProcesses() {
    // Create an empty JSON array
    // Query the workflow table for all workflows by process = “placementProcess”
    // In parallel, query the workflow table for all workflows by task_status = “ADMITTED”
    // When both parallel processes are complete, identify workflows that exist in both sets of data
    // For each workflow that exists in both sets of data, convert the workflow to a JSON representation, and add the resulting JSON object to the JSON array
    // Assert that the JSON array is not empty or null
    // Write the resulting JSON to a file called “test4.json” so that it appears in the “resources” package of the project

    throw new NotYetImplementedException();
  }

}
