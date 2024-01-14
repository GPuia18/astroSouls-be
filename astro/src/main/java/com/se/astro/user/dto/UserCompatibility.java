package com.se.astro.user.dto;

import com.se.astro.user.model.AstroUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCompatibility {
    private AstroUser user;
    private int compatibility;
}
