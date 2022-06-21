package com.sap.mobile.services.client;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * The CurrentTenantResolver implementation resolves the current tenant context
 * when using service binding configuration that supports multi tenancy.
 */
@FunctionalInterface
public interface CurrentTenantResolver extends Supplier<Optional<String>> {

	/**
	 * Resolve the current tenant context in order to dispatch notifications to
	 * the correct tenant. For identification, the tenant's identity zone id is
	 * used.
	 *
	 * @return The current tenant-id or an empty optional if not set
	 */
	@Override
	Optional<String> get();

}