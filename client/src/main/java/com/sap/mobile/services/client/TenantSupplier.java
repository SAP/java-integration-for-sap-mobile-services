package com.sap.mobile.services.client;

import java.util.Optional;
import java.util.function.Supplier;

@FunctionalInterface
public interface TenantSupplier extends Supplier<Optional<String>> {
}
