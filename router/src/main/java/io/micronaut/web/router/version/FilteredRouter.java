/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.web.router.version;

import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.filter.HttpFilter;
import io.micronaut.web.router.RouteMatch;
import io.micronaut.web.router.Router;
import io.micronaut.web.router.UriRoute;
import io.micronaut.web.router.UriRouteMatch;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Decorated {@link Router} with filtering capabilities.
 *
 * @author Bogdan Oros
 * @since 1.1.0
 */
public class FilteredRouter implements Router {

    private final Router router;
    private final RouteMatchesFilter routeFilter;

    /**
     * Creates a decorated router for an existing router and {@link RouteMatchesFilter}.
     *
     * @param router      A {@link Router} to delegate to
     * @param routeFilter A {@link RouteMatchesFilter} to filter non matching routes
     */
    FilteredRouter(Router router,
                   RouteMatchesFilter routeFilter) {
        this.router = router;
        this.routeFilter = routeFilter;
    }

    @Override
    public <T, R> Stream<UriRouteMatch<T, R>> findAny(CharSequence uri) {
        return router.findAny(uri);
    }

    @Override
    public <T, R> Stream<UriRouteMatch<T, R>> find(HttpMethod httpMethod, CharSequence uri) {
        return router.find(httpMethod, uri);
    }

    @Override
    public Stream<UriRoute> uriRoutes() {
        return router.uriRoutes();
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> route(HttpMethod httpMethod, CharSequence uri) {
        return router.route(httpMethod, uri);
    }

    @Override
    public <R> Optional<RouteMatch<R>> route(HttpStatus status) {
        return router.route(status);
    }

    @Override
    public <R> Optional<RouteMatch<R>> route(Class originatingClass, HttpStatus status) {
        return router.route(originatingClass, status);
    }

    @Override
    public <R> Optional<RouteMatch<R>> route(Throwable error) {
        return router.route(error);
    }

    @Override
    public <R> Optional<RouteMatch<R>> route(Class originatingClass, Throwable error) {
        return router.route(originatingClass, error);
    }

    @Override
    public List<HttpFilter> findFilters(HttpRequest<?> request) {
        return router.findFilters(request);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> GET(CharSequence uri) {
        return router.GET(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> POST(CharSequence uri) {
        return router.POST(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> PUT(CharSequence uri) {
        return router.PUT(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> PATCH(CharSequence uri) {
        return router.PATCH(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> DELETE(CharSequence uri) {
        return router.DELETE(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> OPTIONS(CharSequence uri) {
        return router.OPTIONS(uri);
    }

    @Override
    public <T, R> Optional<UriRouteMatch<T, R>> HEAD(CharSequence uri) {
        return router.HEAD(uri);
    }

    @Override
    public <T, R> Stream<UriRouteMatch<T, R>> find(HttpMethod httpMethod, URI uri) {
        return router.find(httpMethod, uri);
    }

    @Override
    public <T, R> Stream<UriRouteMatch<T, R>> find(HttpRequest<?> request) {
        Stream<UriRouteMatch<T, R>> matches = router.find(request);
        return routeFilter.filter(matches.collect(Collectors.toList()), request).stream();
    }
}
