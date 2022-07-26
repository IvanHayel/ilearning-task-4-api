package by.hayel.server.service.impl;

import by.hayel.server.exception.TokenRefreshException;
import by.hayel.server.exception.UserNotFoundException;
import by.hayel.server.model.token.RefreshToken;
import by.hayel.server.model.user.User;
import by.hayel.server.repository.RefreshTokenRepository;
import by.hayel.server.repository.UserRepository;
import by.hayel.server.service.RefreshTokenService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired!";

  final RefreshTokenRepository refreshTokenRepository;
  final UserRepository userRepository;

  @Value("${jwt.refresh.expiration-time}")
  long refreshTokenDuration;

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Override
  public RefreshToken createRefreshToken(Long userId) {
    RefreshToken refreshToken = new RefreshToken();
    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    String token = UUID.randomUUID().toString();
    Instant expiryDate = Instant.now().plusMillis(refreshTokenDuration);
    refreshToken.setUser(user);
    refreshToken.setToken(token);
    refreshToken.setExpiryDate(expiryDate);
    return refreshTokenRepository.save(refreshToken);
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    Instant expiryDate = token.getExpiryDate();
    Instant now = Instant.now();
    if (expiryDate.isBefore(now)) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), REFRESH_TOKEN_EXPIRED);
    }
    return token;
  }

  @Override
  @Transactional
  public void deleteByUserId(Long userId) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    refreshTokenRepository.deleteByUser(user);
  }
}
