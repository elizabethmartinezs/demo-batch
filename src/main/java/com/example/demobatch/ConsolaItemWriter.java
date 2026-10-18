package com.example.demobatch;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;

/**
 * Writer: recibe los registros de un chunk completo y los "guarda".
 * En la demo solo los imprime para que se vea cada chunk en la consola.
 */
public class ConsolaItemWriter implements ItemWriter<Cliente> {

    private int numeroChunk = 0;

    @Override
    public void write(Chunk<? extends Cliente> chunk) {
        numeroChunk++;
        System.out.println(">> Chunk " + numeroChunk + ": " + chunk.size() + " registros");
        for (Cliente cliente : chunk.getItems()) {
            System.out.println("     " + cliente);
        }
    }
}
