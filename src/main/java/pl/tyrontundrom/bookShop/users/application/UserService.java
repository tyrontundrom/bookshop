package pl.tyrontundrom.bookShop.users.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tyrontundrom.bookShop.user.db.UserEntityRepository;
import pl.tyrontundrom.bookShop.user.domain.UserEntity;
import pl.tyrontundrom.bookShop.users.application.port.UserRegistrationUseCase;

@Service
@AllArgsConstructor
class UserService implements UserRegistrationUseCase {

    private final UserEntityRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {
        if (repository.findByUsernameIgnoreCase(username).isPresent()) {
            return RegisterResponse.failure("Account already exist");
        }
        UserEntity entity = new UserEntity(username, encoder.encode(password));
        return RegisterResponse.success(repository.save(entity));
    }
}
