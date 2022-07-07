package com.sap.mobile.services.client.validation.broker.configuration.bean;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.CRC32;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BrokerTemplateConfig {

	@Setter
	private int keepNumber;
	private Set<String> features = new HashSet<>();

	@JsonIgnore
	private String id;

	public void setFeatures(final Set<String> features) {
		this.features = features;
		final CRC32 crc32 = new CRC32();
		features.stream().sorted()
				.filter(f -> !f.equals("storage"))
				.forEach(feature -> {
			crc32.update(feature.getBytes(StandardCharsets.UTF_8));
		});
		this.id = Long.toHexString(crc32.getValue());
	}
}
