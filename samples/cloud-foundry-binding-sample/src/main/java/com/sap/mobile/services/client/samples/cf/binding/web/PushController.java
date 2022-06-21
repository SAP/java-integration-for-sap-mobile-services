package com.sap.mobile.services.client.samples.cf.binding.web;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.mobile.services.client.ClientException;
import com.sap.mobile.services.client.push.PushClient;
import com.sap.mobile.services.client.push.PushPayload;
import com.sap.mobile.services.client.push.PushResponse;

@RestController
@RequestMapping("/api/v1/push")
public class PushController {

	private final PushClient pushClient;

	public PushController(final PushClient pushClient) {
		this.pushClient = pushClient;
	}

	@PostMapping(path = "/all", produces = "text/plain")
	public ResponseEntity<?> pushToApplication() {
		try {
			final PushPayload pushPayload = PushPayload.builder()
					.alert("Example push message from my Spring application")
					.build();
			this.pushClient.pushToApplication(pushPayload);
			return ResponseEntity.ok("Push message has been sent");
		} catch (ClientException e) {
			System.err.println("Failed to send push message: " + e.getMessage());
			return ResponseEntity.internalServerError().body("Failed to send push message");
		}
	}

	@PostMapping(path = "/users/{userId}", produces = "text/plain")
	public ResponseEntity<?> pushToUser(@PathVariable("userId") final String userId) {
		try {
			final Collection<String> userIds = Collections.singletonList(userId);
			final PushPayload pushPayload = PushPayload.builder()
					.alert("Example push message from my Spring application")
					.build();
			final PushResponse response = this.pushClient.pushToUsers(userIds, pushPayload);
			if (response.getStatus() != null) {
				return ResponseEntity.ok("User does not exist");
			}
			return ResponseEntity.ok("Push message has been sent");
		} catch (ClientException e) {
			System.err.println("Failed to send push message: " + e.getMessage());
			return ResponseEntity.internalServerError().body("Failed to send push message");
		}
	}

}
