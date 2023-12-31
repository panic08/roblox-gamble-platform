package ru.marthastudios.robloxcasino.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Error")
public class ErrorDto {
    private String message;
}
