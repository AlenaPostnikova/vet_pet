package com.example.vet_pet.service;

import com.example.vet_pet.exeption.CommonBackendException;
import com.example.vet_pet.model.db.entity.Pet;
import com.example.vet_pet.model.db.entity.User;
import com.example.vet_pet.model.db.repository.PetRepository;
import com.example.vet_pet.model.dto.request.PetInfoReq;
import com.example.vet_pet.model.dto.response.PetInfoResp;
import com.example.vet_pet.model.dto.response.UserInfoResp;
import com.example.vet_pet.model.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {
    private final ObjectMapper mapper; //для преобразования объектов java в JSON
    private final PetRepository petRepository;
    private final UserService userService;

    public PetInfoResp addPet(PetInfoReq PetInfoReq){

        Pet pet = mapper.convertValue(PetInfoReq, Pet.class);
        pet.setStatus(Status.CREATED);

        Pet petSave = petRepository.save(pet);
        return mapper.convertValue(petSave, PetInfoResp.class);
    }

    public PetInfoResp getPet(Long id) {
        Pet pet = getPetFromDB(id);
        return mapper.convertValue(pet, PetInfoResp.class);
    }

    public Pet getPetFromDB(Long id) {
        Optional<Pet> optionalPet = petRepository.findById(id);

        final String errMsg = String.format("Pet with id: %s not found", id);

        return optionalPet.orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    public PetInfoResp updatePet(Long id, PetInfoReq req) {
        Pet petFromDB = getPetFromDB(id);

        Pet petReq = mapper.convertValue(req, Pet.class);

        petFromDB.setNamePet(petReq.getNamePet() == null ? petFromDB.getNamePet() : petReq.getNamePet());
        petFromDB.setDateOfBirthPet(petReq.getDateOfBirthPet() == null ? petFromDB.getDateOfBirthPet() : petReq.getDateOfBirthPet());
        petFromDB.setTypePet(petReq.getTypePet() == null ? petFromDB.getTypePet() : petReq.getTypePet());
        petFromDB.setBreedPet(petReq.getBreedPet() == null ? petFromDB.getBreedPet() : petReq.getBreedPet());
        petFromDB.setColorPet(petReq.getColorPet() == null ? petFromDB.getColorPet() : petReq.getColorPet());
        petFromDB.setGenderPet(petReq.getGenderPet() == null ? petFromDB.getGenderPet() : petReq.getGenderPet());

        petFromDB.setStatus(Status.UPDATED);
        petFromDB = petRepository.save(petFromDB);
        return mapper.convertValue(petFromDB, PetInfoResp.class);
    }

    public void deletePet(Long id){
        Pet pet = getPetFromDB(id);
        pet.setStatus(Status.DELETED);
        pet = petRepository.save(pet);
    }

    public PetInfoResp linkPetAndUser(Long petId, Long userId){ //привязка питомца к пользователю
        Pet petFromDB = getPetFromDB(petId);
        User userFromDB = userService.getUserFromDB(userId);

        if (petFromDB == null || userFromDB == null){
            return PetInfoResp.builder().build();
        }
        List<Pet> pets = userFromDB.getPets();

        Pet existingPet = pets.stream().filter(pet -> pet.getId().equals(petId)).findFirst().orElse(null);

        if (existingPet != null){
            return mapper.convertValue(existingPet, PetInfoResp.class);
        }

        pets.add(petFromDB); //доб питомца в список питомцев пользователя
        User user = userService.updateLinkList(userFromDB); // обновили список питомцев

        PetInfoResp petInfoResp = mapper.convertValue(petFromDB, PetInfoResp.class);
        UserInfoResp userInfoResp = mapper.convertValue(userFromDB, UserInfoResp.class);

        petInfoResp.setUserInfoResp(userInfoResp);

        return petInfoResp;
    }

    public List<PetInfoResp> getUserPets(Long userId){

        User user = userService.getUserFromDB(userId); //чтобы выбросить исключение, если пользователь не найден

        return petRepository.getUserPets(userId).stream()
                .map(pet -> mapper.convertValue(pet, PetInfoResp.class))
                .collect(Collectors.toList());
    }

    public Pet updateLinkList(Pet updatedPet) {
        return petRepository.save(updatedPet);
    }


}
