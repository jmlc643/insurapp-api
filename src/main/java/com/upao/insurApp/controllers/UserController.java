package com.upao.insurApp.controllers;

import com.upao.insurApp.dto.reserve.ReserveUserDTO;
import com.upao.insurApp.dto.user.ProfileDTO;
import com.upao.insurApp.dto.user.UpdatePasswordRequest;
import com.upao.insurApp.dto.user.UpdateUserRequest;
import com.upao.insurApp.models.Reserve;
import com.upao.insurApp.repos.ReserveRepository;
import com.upao.insurApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private ReserveRepository reserveRepository;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> profile() {
        return new ResponseEntity<>(userService.getProfile(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReserveUserDTO>> getMyReserves() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.valueOf(authentication.getName());
        List<Reserve> userReserves = reserveRepository.findByUserUserIdOrderByReserveIdDesc(userId);
        List<ReserveUserDTO> dtos = userReserves.stream()
                .map(ReserveUserDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordRequest request) {
        return new ResponseEntity<>(userService.updatePassword(request), HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateUserRequest request) {
        return new ResponseEntity<>(userService.updateProfile(request), HttpStatus.OK);
    }
}
