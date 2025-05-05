package com.kayky.anime;

import com.kayky.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    {
        var ninjaKamui = Anime.builder().id(1L).name("Ninja Kamui").build();
        var kaijuu = Anime.builder().id(2L).name("Kaijuu-8gou").build();
        var kimetsuNoYaiba = Anime.builder().id(3L).name("Kimetsu No Yaiba").build();
        animes.addAll(List.of(ninjaKamui, kaijuu, kimetsuNoYaiba));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}