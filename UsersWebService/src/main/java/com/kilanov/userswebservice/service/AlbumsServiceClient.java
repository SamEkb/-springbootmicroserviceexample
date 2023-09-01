package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.ui.response.AlbumResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albums")
    @CircuitBreaker(name = "albums-ws", fallbackMethod = "getAlbumsFallback")
    List<AlbumResponse> getAlbums(@PathVariable String id);

    default List<AlbumResponse> getAlbumsFallback(String id, Throwable exception) {
        return new ArrayList<>();
    }
}
