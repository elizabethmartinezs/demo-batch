package com.example.demobatch;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.step.StepExecution;

/** Listener: se ejecuta antes y después del Job e imprime un resumen. */
public class ResumenJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("=== Inicia el job " + jobExecution.getJobInstance().getJobName() + " ===");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("=== Job terminado con estado: " + jobExecution.getStatus() + " ===");
        for (StepExecution step : jobExecution.getStepExecutions()) {
            System.out.println("Step " + step.getStepName()
                    + " | leídos: " + step.getReadCount()
                    + " | filtrados: " + step.getFilterCount()
                    + " | escritos: " + step.getWriteCount()
                    + " | commits: " + step.getCommitCount());
        }
    }
}
