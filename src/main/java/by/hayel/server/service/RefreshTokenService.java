package by.hayel.server.service;

import by.hayel.server.model.token.RefreshToken;
import java.util.Optional;

public interface RefreshTokenService {
  Optional<RefreshToken> findByToken(String token);

  RefreshToken createRefreshToken(Long userId);

  RefreshToken verifyExpiration(RefreshToken token);

  void deleteByUserId(Long userId);
}
