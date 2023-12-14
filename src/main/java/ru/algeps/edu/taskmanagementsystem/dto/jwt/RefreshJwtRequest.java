package ru.algeps.edu.taskmanagementsystem.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record RefreshJwtRequest(String refreshToken) {}
