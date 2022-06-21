package com.sap.mobile.services.client;

import java.util.function.Supplier;

/**
 * Supplies a custom authorization header.
 */
@FunctionalInterface
public interface CustomAuthHeaderSupplier extends Supplier<HttpHeader> {
}
