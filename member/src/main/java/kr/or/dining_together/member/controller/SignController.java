package kr.or.dining_together.member.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.or.dining_together.member.config.security.JwtTokenProvider;
import kr.or.dining_together.member.dto.UserDto;
import kr.or.dining_together.member.jpa.repo.UserRepository;
import kr.or.dining_together.member.model.SingleResult;
import kr.or.dining_together.member.service.ResponseService;
import kr.or.dining_together.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags={"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value="/auth")
public class SignController {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @ApiOperation(value="로그인", notes = "이메일을 통해 로그인한다.")
    @PostMapping(value="/login")
    public SingleResult<String> login(@ApiParam(value="회원ID: 이메일",required = true) @RequestParam String email,
                                      @ApiParam(value="비밀번호",required = true) @RequestParam String password){
        UserDto userDto = userService.login(email, password);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(userDto.getEmail()), userDto.getRoles()));
    }



}
