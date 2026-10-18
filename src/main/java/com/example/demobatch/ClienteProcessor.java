package com.example.demobatch;

import org.springframework.batch.infrastructure.item.ItemProcessor;

/**
 * Processor: transforma cada registro entre la lectura y la escritura.
 * Si regresa null, el registro se descarta (filtrado) y no llega al Writer.
 */
public class ClienteProcessor implements ItemProcessor<Cliente, Cliente> {

    @Override
    public Cliente process(Cliente cliente) {
        if (cliente.correo() == null || cliente.correo().isBlank()) {
            System.out.println("     Descartado (sin correo): " + cliente.nombre());
            return null;
        }
        return new Cliente(
                cliente.id(),
                cliente.nombre().toUpperCase(),
                cliente.correo().toLowerCase());
    }
}
