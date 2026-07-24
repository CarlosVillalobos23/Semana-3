package com.carlos.authorization.services;

import com.carlos.authorization.dto.LoginRequest;
import com.carlos.authorization.dto.TokenResponse;

public interface AuthService {
    TokenResponse autenticar(LoginRequest request) throws Exception;
}
