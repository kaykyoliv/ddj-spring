package com.kayky.mapper;

import com.kayky.annotation.EncodedMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderMapper {

    private final PasswordEncoder passwordEncoder;

    @EncodedMapping
    public String encode(String rawPassword){
        return rawPassword == null ? null : passwordEncoder.encode(rawPassword);
    }
}
