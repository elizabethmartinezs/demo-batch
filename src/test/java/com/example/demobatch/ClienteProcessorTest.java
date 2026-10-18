package com.example.demobatch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Prueba unitaria del Processor: no necesita Spring ni levantar el Job. */
class ClienteProcessorTest {

    private final ClienteProcessor processor = new ClienteProcessor();

    @Test
    void convierteNombreAMayusculas() {
        Cliente resultado = processor.process(new Cliente("1", "Ana López", "Ana.Lopez@Correo.com"));

        assertThat(resultado.nombre()).isEqualTo("ANA LÓPEZ");
        assertThat(resultado.correo()).isEqualTo("ana.lopez@correo.com");
    }

    @Test
    void descartaClientesSinCorreo() {
        Cliente resultado = processor.process(new Cliente("6", "Jorge Hernández", ""));

        assertThat(resultado).isNull();
    }
}
