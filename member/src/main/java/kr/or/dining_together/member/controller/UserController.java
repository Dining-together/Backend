package kr.or.dining_together.member.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags={"1. User"})
@RestController
@RequestMapping(value = "/auth")
public class UserController {
}
