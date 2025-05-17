package com.example.vet_pet.service;

import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.entity.User;
import com.example.vet_pet.model.dto.response.UserInfoResp;
import com.example.vet_pet.model.db.repository.UserRepository;
import com.example.vet_pet.model.dto.request.UserInfoReq;
import com.example.vet_pet.model.enums.Status;
import com.example.vet_pet.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j //для логирования
@Service
@RequiredArgsConstructor //созд. конструктора для инициализации бина
public class UserService {
        private final ObjectMapper mapper;
        private final UserRepository userRepository;

    public void isValidEmail(String email) {
        try{
            EmailValidator.getInstance().isValid(email);
        } catch (IllegalArgumentException e) {
            throw new CommonBackendException("Invalid email", HttpStatus.BAD_REQUEST);
        }
    }

    public User getUserFromDB(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        final String errMsg = String.format("User with email: %s not found", email);

        return optionalUser.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    public User getUserFromDB(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        final String errMsg = String.format("User with id: %s not found", id);

        return optionalUser.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    public UserInfoResp getUser(Long id) {
        User user = getUserFromDB(id);
        return mapper.convertValue(user, UserInfoResp.class);
    }

    public UserInfoResp addUser(UserInfoReq req) {

        isValidEmail(req.getEmail());

        userRepository.findByEmail(req.getEmail()).ifPresent(u -> { //проверка нет ли пользователя с таким маилом
            throw new CommonBackendException("User already exists", HttpStatus.CONFLICT);
        });

        String hashPassword = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt());

        User user = mapper.convertValue(req, User.class); //преобразуем запрос в Пользователя-сущность
        user.setStatus(Status.CREATED); //присваиваем статус
        user.setPassword(hashPassword);

        User save = userRepository.save(user); //сохранили в базу данных
        return mapper.convertValue(save, UserInfoResp.class);
    }

    public UserInfoResp checkPassword(String email, String userPassword) {

        User user = getUserFromDB(email);

        if (!BCrypt.checkpw(userPassword, user.getPassword()))
            throw new CommonBackendException("Secret is incorrect", HttpStatus.FORBIDDEN);

        return mapper.convertValue(user, UserInfoResp.class);
    }


    public UserInfoResp updateUser(Long id, UserInfoReq req) {
        User userFromDB = getUserFromDB(id);

        isValidEmail(req.getEmail());

        User userReq = mapper.convertValue(req, User.class);

        userFromDB.setEmail(userReq.getEmail() == null ? userFromDB.getEmail() : userReq.getEmail());
        userFromDB.setNumPhone(userReq.getNumPhone() == null ? userFromDB.getNumPhone() : userReq.getNumPhone());
        userFromDB.setPassword(userReq.getPassword() == null ? userFromDB.getPassword() : userReq.getPassword());
        userFromDB.setFirstName(userReq.getFirstName() == null ? userFromDB.getFirstName() : userReq.getFirstName());
        userFromDB.setLastName(userReq.getLastName() == null ? userFromDB.getLastName() : userReq.getLastName());
        userFromDB.setMiddleName(userReq.getMiddleName() == null ? userFromDB.getMiddleName() : userReq.getMiddleName());
        userFromDB.setDateOfBirth(userReq.getDateOfBirth() == null ? userFromDB.getDateOfBirth() : userReq.getDateOfBirth());
        userFromDB.setGender(userReq.getGender() == null ? userFromDB.getGender() : userReq.getGender());

        userFromDB.setStatus(Status.UPDATED);
        userFromDB = userRepository.save(userFromDB);
        return mapper.convertValue(userFromDB, UserInfoResp.class);
    }


    public void deleteUser(Long id) {
        User userFromDB = getUserFromDB(id);

        userFromDB.setStatus(Status.DELETED);
        userFromDB = userRepository.save(userFromDB);
    }

    public User updateLinkList(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    public Page<UserInfoResp> getAllUsers
            (Integer page, Integer perPage, String sort, Sort.Direction order, String filter){

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);

        Page<User> users;

        if(StringUtils.hasText(filter)){
            users = userRepository.findAllFiltered(pageRequest, filter);
        } else {
            users = userRepository.findAll(pageRequest);
        }

        List<UserInfoResp> content = users.getContent().stream()
                .map(u -> mapper.convertValue(u, UserInfoResp.class))
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageRequest, users.getTotalElements());
    }

}
