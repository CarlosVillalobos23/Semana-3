package com.carlos.authorization.services;

import com.carlos.authorization.dto.UsuarioRequest;
import com.carlos.authorization.dto.UsuarioResponse;

import java.util.Set;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
