package kr.or.dining_together.search.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"3. StoreLog"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/log")
@Slf4j
public class LogServiceController {

}
