package com.kayky.mapper;

import com.kayky.domain.Anime;
import com.kayky.request.AnimePostRequest;
import com.kayky.request.AnimePutRequest;
import com.kayky.response.AnimeGetResponse;
import com.kayky.response.AnimePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest request);

    AnimePostResponse toAnimePostResponse(Anime anime);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animes);

}