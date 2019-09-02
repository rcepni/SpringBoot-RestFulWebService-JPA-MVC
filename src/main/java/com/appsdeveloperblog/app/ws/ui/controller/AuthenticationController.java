package com.appsdeveloperblog.app.ws.ui.controller;
import com.appsdeveloperblog.app.ws.ui.model.request.UserLoginRequestModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@RestController
public class AuthenticationController {
	
	///we create this class because swagger didnot had the base login ,we create this class to trick the swagger
	

	@ApiOperation("User login")
    @ApiResponses(value = {
    @ApiResponse(code = 200, 
            message = "Response Headers", 
            responseHeaders = {
                @ResponseHeader(name = "authorization", 
                        description = "Bearer <JWT value here>"),
                @ResponseHeader(name = "userId", 
                        description = "<Public User Id value here>")
            })  
    })
	@PostMapping("/users/login")
	public void theFakeLogin(@RequestBody UserLoginRequestModel loginRequestModel) {
		
		throw new IllegalStateException("This method should not be called.This method is implemented by Spring Security");
	}
}
