package com.gflopes.bookstoremanager.users.builder;

import com.gflopes.bookstoremanager.enums.Gender;
import com.gflopes.bookstoremanager.enums.Role;
import com.gflopes.bookstoremanager.users.dto.UserDTO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Gustavo Lopes";

    @Builder.Default
    private int age = 48;

    @Builder.Default
    private Gender gender = Gender.MALE;

    @Builder.Default
    private String email = "gflopes22@gmail.com";

    @Builder.Default
    private String username = "gflopes22";

    @Builder.Default
    private String password = "123456";

    @Builder.Default
    private LocalDate birthDate = LocalDate.of(1973, 9, 22);

    @Builder.Default
    private Role role = Role.USER;

    public UserDTO buildUserDTO() {
            return new UserDTO(id,
                    name,
                    age,
                    gender,
                    email,
                    username,
                    password,
                    birthDate,
                    role);
    }

}
