package com.example.demobatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * Configuración del Job: CSV -> (proceso) -> consola, en chunks de 10.
 * Sin @EnableBatchProcessing: Spring Boot autoconfigura Batch
 * (JobRepository en memoria) y ejecuta el Job al arrancar.
 */
@Configuration
public class BatchConfig {

    // 1. READER: lee clientes.csv, salta el encabezado y convierte cada fila en un Cliente
    @Bean
    public FlatFileItemReader<Cliente> clienteReader() {
        return new FlatFileItemReaderBuilder<Cliente>()
                .name("clienteReader")
                .resource(new ClassPathResource("clientes.csv"))
                .linesToSkip(1)
                .delimited()
                .names("id", "nombre", "correo")
                .targetType(Cliente.class)
                .build();
    }

    // 3. WRITER: recibe cada chunk ya procesado
    @Bean
    public ConsolaItemWriter clienteWriter() {
        return new ConsolaItemWriter();
    }

    // STEP: lee, procesa y escribe en bloques (chunks) de 10 registros
    @Bean
    public Step procesarClientesStep(JobRepository jobRepository,
                                     FlatFileItemReader<Cliente> clienteReader,
                                     ConsolaItemWriter clienteWriter) {
        return new StepBuilder("procesarClientesStep", jobRepository)
                .<Cliente, Cliente>chunk(10)
                .reader(clienteReader)
                .writer(clienteWriter)
                .build();
    }

    // JOB: el proceso completo; aquí tiene un solo Step
    @Bean
    public Job importarClientesJob(JobRepository jobRepository, Step procesarClientesStep) {
        return new JobBuilder("importarClientesJob", jobRepository)
                .start(procesarClientesStep)
                .build();
    }
}
