package pe.edu.upn.pedidos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculadoraPedido {

    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");

    public BigDecimal calcularSubtotal(List<Producto> productos) {
        BigDecimal subtotal = productos.stream()
                .map(this::importeDe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return redondear(subtotal);
    }

    public BigDecimal aplicarDescuento(BigDecimal subtotal, BigDecimal porcentaje) {
        BigDecimal montoDescuento = subtotal.multiply(porcentaje).divide(CIEN);
        return redondear(subtotal.subtract(montoDescuento));
    }

    public BigDecimal calcularImpuesto(BigDecimal baseImponible) {
        return redondear(baseImponible.multiply(TASA_IGV));
    }

        public BigDecimal calcularTotal(List<Producto> productos, BigDecimal porcentaje) {
        BigDecimal subtotal = calcularSubtotal(productos);
        BigDecimal baseImponible = aplicarDescuento(subtotal, porcentaje);
        BigDecimal igv = calcularImpuesto(baseImponible);
        return redondear(baseImponible.add(igv));
    }

    private BigDecimal importeDe(Producto producto) {
        return producto.precio().multiply(BigDecimal.valueOf(producto.cantidad()));
    }

    private BigDecimal redondear(BigDecimal monto) {
        return monto.setScale(2, RoundingMode.HALF_UP);    
    }
}