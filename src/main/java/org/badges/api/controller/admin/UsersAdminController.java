package org.badges.api.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.badges.api.domain.UserDto;
import org.badges.api.domain.admin.AdminUser;
import org.badges.db.User;
import org.badges.db.UserPermission;
import org.badges.db.repository.UserRepository;
import org.badges.security.annotation.RequiredPermission;
import org.badges.service.converter.UserConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
@Slf4j
public class UsersAdminController {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    @RequiredPermission(UserPermission.READ_USERS)
    @GetMapping
    public Page<UserDto> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userConverter::convertUser);
    }

    @RequiredPermission(UserPermission.READ_USERS)
    @GetMapping("/{id}")
    public AdminUser getUser(@PathVariable("id") long id) {
        User user = userRepository.getOne(id);
        return new AdminUser()
                .setId(user.getId())
                .setName(user.getName());
    }

    @PostMapping("/uploadUsers")
    @Transactional
    public Collection<Object> uploadUsers(MultipartFile file) throws IOException {
        Map<String, User> usersByEmail = userRepository.findAll().stream()
                .collect(Collectors.toMap(User::getEmail, Function.identity()));

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        sheet.forEach(row -> {
            String email = getStringCellValue(row.getCell(1));
            User user = usersByEmail.computeIfAbsent(email, key -> new User().setAddress(email));

            user.setName(getStringCellValue(row.getCell(0)))
                    .setAddress(getStringCellValue(row.getCell(2)))
                    .setTitle(getStringCellValue(row.getCell(3)))
                    .setDescription(getStringCellValue(row.getCell(4)))
                    .setImageUrl(getStringCellValue(row.getCell(5)))
                    .setEnabled(true);

            userRepository.save(user);
            usersByEmail.remove(email);
        });
        usersByEmail.values().forEach(u -> u.setEnabled(false));

        return Collections.emptyList();
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getStringCellValue();
    }
}
