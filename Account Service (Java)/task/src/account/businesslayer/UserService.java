package account.businesslayer;

import account.persistance.UserRepository;
import account.presentation.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public User findUserById(Long id) {
//        return userRepository.findUserById(id);
//    }

    public User save(UserDTO dto) {
        return dto.convertDTOToUser();
    }

}
