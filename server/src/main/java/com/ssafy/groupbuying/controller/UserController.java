package com.ssafy.groupbuying.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.groupbuying.repository.UserRepository;
import com.ssafy.groupbuying.service.JwtService;
import com.ssafy.groupbuying.service.UserService;
import com.ssafy.groupbuying.vo.User;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	private JwtService jwtService;

	@GetMapping(value = "/{uMail}")
	@ApiOperation(value = "checkMail", notes = "이메일 중복체크 true=중복 이메일 존재 / false=중복 이메일 없음")
	public ResponseEntity<Boolean> checkMailaa(@PathVariable("uMail") String uMail) {
		
		
		return new ResponseEntity<Boolean>(userService.checkByMail(uMail),HttpStatus.OK) ;
		
		

	}

	// 유저 Mail로 아이디 삭제

	@DeleteMapping(value = "/{uMail}")
	@ApiOperation(value = "deleteUser", notes = "유저 삭제 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원정보 일치"), @ApiResponse(code = 409, message = "비밀번호가 틀립니다") })
	public ResponseEntity<Void> deleteUser(@RequestBody User user) {

		HttpStatus status = HttpStatus.CONFLICT;
		if (userService.checkPass(user)) {
			userService.deleteByMail(user.getMail());
			status = HttpStatus.OK;

		}

		return new ResponseEntity<Void>(status);

	}

	@PostMapping(value = "/signUp")
	@ApiOperation(value = "signUp", notes = "회원가입 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "회원가입 성공"), @ApiResponse(code = 409, message = "회원가입 실패") })
	public ResponseEntity<Void> signUp(@RequestBody User user) {

		HttpStatus status = HttpStatus.OK;
		try {
			System.out.println(user);
			
			userService.save(user);
		} catch (Exception e) {
			status = HttpStatus.CONFLICT;
		}
		return new ResponseEntity<Void>(status);

	}

	@PostMapping(value = "/signIn")
	@ApiOperation(value = "signIn", notes = "로그인 ")
	@ApiResponses({ @ApiResponse(code = 200, message = "로그인 성공"),
			@ApiResponse(code = 409, message = "아이디 또는 비밀번호가 틀립니다") })
	public ResponseEntity<Void> signIn(@RequestBody User user, HttpServletResponse res) {
		
		HttpStatus status = HttpStatus.CONFLICT;

		if (userService.checkPass(user)) {
			res.setHeader("jwt-auth-token", jwtService.create(userService.findByMail(user.getMail())));
			status = HttpStatus.OK;
		}

		return new ResponseEntity<Void>(status);

	}

	@ApiOperation(value = "checkToken", notes = "토큰 확인용 API ")
	@PostMapping(value = "/checkToken")
	public ResponseEntity<Map<String, Object>> checkToken(HttpServletRequest req) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println(jwtService.get(req.getHeader("jwt-auth-token")));

			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.ACCEPTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
