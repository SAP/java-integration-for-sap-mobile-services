package com.sap.mobile.services.client.validation.broker.web;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationFailedException;
import com.sap.mobile.services.client.validation.broker.exception.InstanceCreationTimeoutException;
import com.sap.mobile.services.client.validation.broker.exception.MaxConcurrentInstancesReachedException;
import com.sap.mobile.services.client.validation.broker.exception.NoSuchServiceInstanceException;
import com.sap.mobile.services.client.validation.broker.model.AppConfig;
import com.sap.mobile.services.client.validation.broker.model.AppConfigOpenApiModel;
import com.sap.mobile.services.client.validation.broker.model.ServiceKeyOpenApiModel;
import com.sap.mobile.services.client.validation.broker.model.ServiceKeyRequest;
import com.sap.mobile.services.client.validation.broker.service.api.BrokerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/broker/v1/apps")
@RequiredArgsConstructor
@Slf4j
public class BrokerController {

	private final BrokerService brokerService;

	@Operation(summary = "Create a new Mobile Services application")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Mobile Application created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceKeyOpenApiModel.class))
			}),
			@ApiResponse(responseCode = "500", description = "Mobile Application creation failed", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
			})
	})
	@PostMapping
	public ResponseEntity<?> createApp(@RequestParam(name = "features", required = false) final Set<String> features) {
		try {
			final Map<String, ?> result = brokerService.createMobileApplication(Optional.ofNullable(features).orElseGet(Collections::emptySet));
			return ResponseEntity.ok(result);
		} catch (MaxConcurrentInstancesReachedException | InstanceCreationTimeoutException |
				 InstanceCreationFailedException e) {
			log.error("Handle exception while creating instance", e);
			return ResponseEntity.internalServerError().body(e.getMessage());
		} catch (Exception e) {
			log.error("Handle unknown exception while creating instance", e);
			return ResponseEntity.internalServerError().body("An unknown error occurred while creating the service instance");
		}
	}

	@Operation(summary = "Create a new Mobile Services configuration for an existing app")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Configuration created", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AppConfigOpenApiModel.class))
			}),
			@ApiResponse(responseCode = "404", description = "Mobile Application not found", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
			}),
			@ApiResponse(responseCode = "500", description = "Configuration creation failed", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
			})
	})
	@PostMapping("/{appId}/configurations")
	public ResponseEntity<?> createConfigurationFile(@PathVariable("appId") final String appId, @RequestBody final ServiceKeyRequest serviceKeyRequest) {
		try {
			final AppConfig result = brokerService.createMobileServicesSettingsConfig(appId, serviceKeyRequest);
			return ResponseEntity.ok(result);
		} catch (NoSuchServiceInstanceException e) {
			log.error("Handle exception while creating configuration", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			log.error("Handle unknown exception while creating configuration", e);
			return ResponseEntity.internalServerError().body("An unknown error occurred while creating the configuration");
		}
	}

	@Operation(summary = "Delete a Mobile Services application")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Mobile Application deleted", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ServiceKeyOpenApiModel.class))
			}),
			@ApiResponse(responseCode = "404", description = "Mobile Application not found", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
			}),
			@ApiResponse(responseCode = "500", description = "Mobile Application deletion failed", content = {
					@Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
			})
	})
	@DeleteMapping("/{appId}")
	public ResponseEntity<?> deleteApp(@PathVariable("appId") final String appId) {
		try {
			brokerService.deleteMobileApplication(appId);
			return ResponseEntity.noContent().build();
		} catch (NoSuchServiceInstanceException e) {
			log.error("Handle exception while deleting instance", e);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			log.error("Handle unknown exception while deleting instance", e);
			return ResponseEntity.internalServerError().body("An unknown error occurred while deleting the service instance");
		}
	}
}
