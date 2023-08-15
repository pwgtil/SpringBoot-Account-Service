package account.config;

import account.entity.Group;
import account.repository.GroupRepository;
import account.authorization.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final GroupRepository groupRepository;

    @Autowired
    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            groupRepository.save(new Group(UserRole.ROLE_ADMINISTRATOR.name()));
            groupRepository.save(new Group(UserRole.ROLE_USER.name()));
            groupRepository.save(new Group(UserRole.ROLE_ACCOUNTANT.name()));
            groupRepository.save(new Group(UserRole.ROLE_AUDITOR.name()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
