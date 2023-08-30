package com.kilanov.albumwebservice.service;

import com.kilanov.albumwebservice.entity.AlbumEntity;

import java.util.List;

public interface AlbumService {
    List<AlbumEntity> getAlbums(String userId);

}
