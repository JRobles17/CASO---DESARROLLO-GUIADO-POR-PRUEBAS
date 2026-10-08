package pe.edu.upn.pedidos;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculadoraPedidoTest {

    private final CalculadoraPedido calc = new CalculadoraPedido();

    @Test
    void subtotalDeDosProductosSumaSusPrecios() {
        List<Producto> productos = List.of(
                new Producto("Mouse", new BigDecimal("50.00"), 1),
                new Producto("Teclado", new BigDecimal("30.00"), 1));
        assertEquals(new BigDecimal("80.00"), calc.calcularSubtotal(productos));
    }
}