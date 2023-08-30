package com.kilanov.userswebservice.service;

import com.kilanov.userswebservice.ui.response.AlbumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "albums-ws")
public interface AlbumsServiceClient {

    @GetMapping("/users/{id}/albumsss")
    List<AlbumResponse> getAlbums(@PathVariable String id);
}
