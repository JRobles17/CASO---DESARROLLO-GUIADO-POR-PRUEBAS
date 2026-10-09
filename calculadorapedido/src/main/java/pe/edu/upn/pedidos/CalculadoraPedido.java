package pe.edu.upn.pedidos;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CalculadoraPedido {

    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final BigDecimal TASA_IGV = new BigDecimal("0.18");
    private static final BigDecimal SIN_DESCUENTO = BigDecimal.ZERO;
    private static final int DECIMALES = 2;
    private static final RoundingMode REDONDEO = RoundingMode.HALF_UP;

    public BigDecimal calcularSubtotal(List<Producto> productos) {
        productos.forEach(this::validarProducto);
        BigDecimal subtotal = productos.stream()
                .map(this::importeDe)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return redondear(subtotal);
    }

    public BigDecimal aplicarDescuento(BigDecimal subtotal, BigDecimal porcentaje) {
        validarPorcentaje(porcentaje);
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

    public BigDecimal calcularTotal(List<Producto> productos) {
        return calcularTotal(productos, SIN_DESCUENTO);
    }

    private void validarProducto(Producto producto) {
        if (producto.precio().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (producto.cantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
    }

    private void validarPorcentaje(BigDecimal porcentaje) {
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(CIEN) > 0) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 0 y 100");
        }
    }

    private BigDecimal importeDe(Producto producto) {
        return producto.precio().multiply(BigDecimal.valueOf(producto.cantidad()));
    }

    private BigDecimal redondear(BigDecimal monto) {
        return monto.setScale(DECIMALES, REDONDEO);
    }
}