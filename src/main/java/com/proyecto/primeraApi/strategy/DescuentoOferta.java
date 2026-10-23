
package com.proyecto.primeraApi.strategy;

public class DescuentoOferta implements EstrategiaDescuento {

    @Override
    public double aplicarDescuento(double precio) {
        return precio * 0.90;
    }
}
