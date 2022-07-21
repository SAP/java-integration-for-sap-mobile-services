package com.sap.mobile.services.client.validation.broker.service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.cloudfoundry.client.v2.Resource;
import org.cloudfoundry.client.v3.PaginatedRequest;
import org.cloudfoundry.client.v3.PaginatedResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {

	public static <R extends PaginatedRequest, T, P extends PaginatedResponse<T>> Flux<T> paginate(final Function<Integer, R> builder, Function<R, Mono<P>> mapper) {
		final AtomicInteger page = new AtomicInteger(1);
		return Flux.create((emitter) -> {
			final AtomicReference<Runnable> onNext = new AtomicReference<>();
			onNext.set(() -> {
				final R request = builder.apply(page.getAndIncrement());
				addNext(request, mapper, emitter, () -> {
					onNext.get().run();
				});
			});
			onNext.get().run();
		});
	}

	private static <R extends PaginatedRequest, T, P extends PaginatedResponse<T>> void addNext(final R request, final Function<R, Mono<P>> mapper,
			final FluxSink<T> emitter, final Runnable onNext) {
		mapper.apply(request)
				.subscribe(r -> {
					r.getResources().forEach(emitter::next);
					if (r.getPagination().getNext() != null) {
						onNext.run();
					} else {
						emitter.complete();
					}
				}, emitter::error);
	}

	public static <R extends org.cloudfoundry.client.v2.PaginatedRequest, T extends Resource<?>, P extends org.cloudfoundry.client.v2.PaginatedResponse<T>> Flux<T> paginateV2(final Function<Integer, R> builder, Function<R, Mono<P>> mapper) {
		final AtomicInteger page = new AtomicInteger(1);
		return Flux.create((emitter) -> {
			final AtomicReference<Runnable> onNext = new AtomicReference<>();
			onNext.set(() -> {
				final R request = builder.apply(page.getAndIncrement());
				addNextV2(request, mapper, emitter, () -> {
					onNext.get().run();
				});
			});
			onNext.get().run();
		});
	}

	private static <R extends org.cloudfoundry.client.v2.PaginatedRequest, T extends Resource<?>, P extends org.cloudfoundry.client.v2.PaginatedResponse<T>> void addNextV2(final R request, final Function<R, Mono<P>> mapper,
			final FluxSink<T> emitter, final Runnable onNext) {
		mapper.apply(request)
				.subscribe(r -> {
					r.getResources().forEach(emitter::next);
					if (r.getNextUrl() != null) {
						onNext.run();
					} else {
						emitter.complete();
					}
				}, emitter::error);
	}

}
