package com.example.onlinemedicine.contoller;

import com.example.onlinemedicine.dto.pharmacy.request.PharmacyRequestDto;
import com.example.onlinemedicine.dto.pharmacy.response.PharmacyResponseDto;
import com.example.onlinemedicine.entity.Location;
import com.example.onlinemedicine.entity.PharmacyEntity;
import com.example.onlinemedicine.service.PharmacyService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pharmacy")
//@SecurityRequirement(name = "BearerAuthentication")
public class PharmacyController {

    private final PharmacyService service;

    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    @GetMapping("/get-all")
    public List<PharmacyResponseDto> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return service.getAll(page, pageSize);
    }
//    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
//    @GetMapping("/get-with-radius")
//    public List<PharmacyResponseDto> getAllWithRadius(){
//        Location location = new Location("41.353478541049256", "69.28678934184431");
//        return service.findPharmaciesWithinRadius(location,5);
//    }

    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    @GetMapping("/get-by-id")
    public PharmacyResponseDto getById(@RequestParam UUID id){
        PharmacyResponseDto pharmacyResponseDto = service.getById(id);
        Link link = linkTo(methodOn(PharmacyController.class).delete(id)).withRel("delete-pharmacy");
        pharmacyResponseDto.add(link);
        return pharmacyResponseDto;
    }

    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    @PostMapping("/create-pharmacy")
    public PharmacyResponseDto create(@RequestBody PharmacyRequestDto requestDto){
        PharmacyResponseDto pharmacyResponseDto = service.create(requestDto);
        Link link = linkTo(methodOn(PharmacyController.class).getById(pharmacyResponseDto.getId())).withRel("pharmacy-info");
        pharmacyResponseDto.add(link);
        return pharmacyResponseDto;
    }

    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    @PutMapping("/update-pharmacy")
    public PharmacyResponseDto update(@RequestBody PharmacyRequestDto requestDto, @RequestParam UUID id){
        PharmacyResponseDto pharmacyResponseDto = service.updateAndNotDeleted(id, requestDto);
        Link link = linkTo(methodOn(PharmacyController.class).getById(id)).withRel("pharmacy-info");
        pharmacyResponseDto.add(link);
        return pharmacyResponseDto;
    }

    @PreAuthorize(value = "hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete-pharmacy")
    public ResponseEntity<String> delete(@RequestParam UUID id){
        service.delete(id);
        Map<String, String> response = Map.of("response", "deleted");
        Gson gson = new Gson();
        String json = gson.toJson(response);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

}
