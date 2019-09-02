package com.appsdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDTO;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.PasswordResetModel;
import com.appsdeveloperblog.app.ws.ui.model.request.PasswordResetRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins= {"http://localhost:8083","http://localhost:8084"})//we put it here in order to give permission to all http requests
public class UserController {
//	Logger log=LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userService;
	@Autowired
	AddressService addressesService;

	@Autowired
	AddressService addressService;
		
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	
	public  UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
//		BeanUtils.copyProperties(userDto, returnValue);
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);
		return returnValue;
	}

//	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
//			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
//
//	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
////		log.trace("Post method is executing");
//
//		UserRest returnValue = new UserRest();
////		if (userDetails.getFirstName().isEmpty())
////			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
////		if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("object is null");
////		UserDto userDto = new UserDto();//we used modelmapper insted userdto
////		BeanUtils.copyProperties(userDetails, userDto);
//		ModelMapper modelMapper = new ModelMapper();
//		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
//		 
// 		UserDto createdUser = userService.createUser(userDto);
//		//BeanUtils.copyProperties(createUser, returnValue);
//		returnValue = modelMapper.map(createdUser, UserRest.class);
//
//		return returnValue;
//	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	
	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })

	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

		UserRest returnValue = new UserRest();
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public UserRest updateteUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
//		if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
//		if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("object is null");

		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);

		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);

		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);

		returnValue.setOperationName(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> users = userService.getUsers(page, limit);
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	// http://localhost:8080/gys/users/fdgguseridl0ijl/addresses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,	MediaType.APPLICATION_JSON_VALUE,"application/hal+json"})
	public Resources<AddressesRest> getUserAddresses(@PathVariable String id) {

		List<AddressesRest> addressesListRestModel = new ArrayList<>();
		List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

		if (addressesDTO != null && !addressesDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {
			}.getType();
			addressesListRestModel = new ModelMapper().map(addressesDTO, listType);
			for (AddressesRest addressRest : addressesListRestModel) {

Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
addressRest.add(addressLink);

Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
addressRest.add(userLink);
			}
		}
		return new Resources<>(addressesListRestModel) ;
	}
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization",value="${userController.authorizationHeader.description}",paramType="header")
	})
	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE ,"application/hal+json"})
	public Resource <AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {

		AddressDTO addressDto = addressService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();

		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		Link userLink = linkTo(UserController.class).slash(userId).withRel("user");

		AddressesRest addressesRestModel = modelMapper.map(addressDto, AddressesRest.class);
		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);
		return new Resource<>( addressesRestModel);
	}
	
	
	//http://localhost:8080/mobile-app-ws/users/email-verification?token=ter12w3df42fashh
	@GetMapping(path="/email-verification", produces = { MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
//	@CrossOrigin(origins="*")//this will allow any response ajax 
//	@CrossOrigin(origins= {"http://localhost:8083","http://localhost:8084"})//this will allow from these ports 
	public OperationStatusModel verifyEmailToken(@RequestParam(value="token")String token) {
		
		OperationStatusModel returnValue=new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		boolean isVerified=userService.verifyEmailToken(token);
		
		if(isVerified) {
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		}else {
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		}
		return returnValue;
	}
	 /*
    * http://localhost:8080/mobile-app-ws/users/password-reset-request
    * */
   @PostMapping(path = "/password-reset-request", 
           produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
           consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
   )
   public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel) {
   	OperationStatusModel returnValue = new OperationStatusModel();

       boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());
       
       returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
       returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

       if(operationResult)
       {
           returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
       }

       return returnValue;
   }
   @PostMapping(path = "/password-reset",
           consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
   )
   public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel) {
   	OperationStatusModel returnValue = new OperationStatusModel();

       boolean operationResult = userService.resetPassword(
               passwordResetModel.getToken(),
               passwordResetModel.getPassword());
       
       returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
       returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

       if(operationResult)
       {
           returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
       }

       return returnValue;
   }
}