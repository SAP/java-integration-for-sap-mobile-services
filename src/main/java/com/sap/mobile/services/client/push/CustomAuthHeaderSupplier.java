package com.sap.mobile.services.client.push;

import java.util.function.Supplier;

@FunctionalInterface
public interface CustomAuthHeaderSupplier extends Supplier<HttpHeader> {
}
