package com.vilardev.Daily.application.security;

import com.vilardev.Daily.infrastructury.entities.Usuario;

public interface TokenService {

    /**
     * Gera um token JWT para o usuário informado.
     * @param usuario entidade de usuário (implementa UserDetails)
     * @return token JWT compactado
     */
    String generateToken(Usuario usuario);

    /**
     * Valida o token JWT (assinatura e formato).
     * @param token token JWT bruto
     * @return true se válido, false caso contrário
     */
    boolean validateToken(String token);

    /**
     * Extrai o username (subject) do token JWT.
     * @param token token JWT
     * @return username (subject) ou null se não puder extrair
     */
    String getUsernameFromToken(String token);
}
