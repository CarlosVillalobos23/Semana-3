package com.carlos.authorization.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) { }

