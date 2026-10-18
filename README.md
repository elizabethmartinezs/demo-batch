# demo-batch

Demo de **Spring Batch** del curso Java + Spring (Axity).
Spring Boot 4.0.5 (Spring Batch 6), Java 17+, Gradle con Gradle Wrapper.

## Qué hace

Un Job que lee `src/main/resources/clientes.csv` (25 clientes), los procesa y los
escribe en la consola en **chunks de 10**:

| Componente | Clase | Qué hace |
|---|---|---|
| Reader | `FlatFileItemReader` (en `BatchConfig`) | Lee el CSV y convierte cada fila en un `Cliente` |
| Processor | `ClienteProcessor` | Nombre a mayúsculas, correo a minúsculas; descarta clientes sin correo |
| Writer | `ConsolaItemWriter` | Imprime cada chunk recibido |
| Step | `procesarClientesStep` | Reader + Processor + Writer con `chunk(10)` |
| Job | `importarClientesJob` | Ejecuta el Step y el listener |
| Listener | `ResumenJobListener` | Resumen al final: leídos, filtrados, escritos, commits |

No hay base de datos: Spring Boot usa un JobRepository en memoria y ejecuta el Job al arrancar.

## Cómo ejecutarlo

```bash
./gradlew bootRun        # ejecuta el Job (o la flecha verde en DemoBatchApplication)
./gradlew test           # prueba unitaria del Processor + arranque del contexto
```

En Windows se usa `gradlew.bat` en lugar de `./gradlew`.

## Resultado esperado

El CSV tiene 3 clientes sin correo (ids 6, 14 y 22), uno en cada chunk:

```
=== Inicia el job importarClientesJob ===
     Descartado (sin correo): Jorge Hernández
>> Chunk 1: 9 registros
     Cliente[id=1, nombre=ANA LÓPEZ, correo=ana.lopez@correo.com]
     ...
>> Chunk 2: 9 registros
>> Chunk 3: 4 registros
=== Job terminado con estado: COMPLETED ===
Step procesarClientesStep | leídos: 25 | filtrados: 3 | escritos: 22 | commits: 3
```

## Experimentos para la clase

1. **Cambiar el tamaño del chunk:** en `BatchConfig`, `chunk(10)` → `chunk(5)` y vuelve a ejecutar: aparecen más chunks y más commits.
2. **Filtrar más registros:** borra el correo de otra fila del CSV y observa el contador de filtrados.
3. **Quitar el Processor:** comenta `.processor(...)`; los nombres salen sin transformar y ya no se filtra nada.

## Un commit por paso

```bash
git checkout paso-1-reader-writer   # solo Reader + Writer
git checkout main                   # versión completa
```

| Etiqueta | Qué agrega |
|---|---|
| `paso-0-proyecto-base` | Proyecto de Initializr con la dependencia Spring Batch |
| `paso-1-reader-writer` | Reader (CSV) + Writer (consola), Step con `chunk(10)` y Job |
| `paso-2-processor` | `ClienteProcessor`: transforma y descarta |
| `paso-3-listener` | `ResumenJobListener` con el resumen del Job |
| `paso-4-prueba` | Prueba unitaria del Processor |

## Notas

* Sin `@EnableBatchProcessing`: en Spring Boot esa anotación desactiva la autoconfiguración.
* Para guardar el historial de ejecuciones en tablas (`BATCH_JOB_EXECUTION`, etc.) se usa
  `spring-boot-starter-batch-jdbc` con una base de datos (por ejemplo H2).
* Imports de Spring Batch 6: `Job` y `JobExecution` en `org.springframework.batch.core.job`,
  `Step` en `org.springframework.batch.core.step`, y Reader/Processor/Writer en
  `org.springframework.batch.infrastructure.item`.
