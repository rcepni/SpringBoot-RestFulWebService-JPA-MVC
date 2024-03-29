package com.appsdeveloperblog.app.ws.io.repositories;
import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.appsdeveloperblog.app.ws.io.entity.UserEntity;

@Repository
//public interface UserRepository extends CrudRepository<UserEntity, Long> {
	public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	UserEntity findByuserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);
	
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS='true'",
			countQuery="select count(*) from Users u where  u.EMAIL_VERIFICATION_STATUS='true'",
			nativeQuery=true
			)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);
	
	
//	POSITIONAL PARAMETERS
//	@Query(value="select * from Users u where u.first_name=?1,and u.last_name=?2",nativeQuery=true)
//	List<UserEntity> findUserByFirstName(String firstName,String lastName);
	@Query(value="select * from Users u where u.first_name=?1",nativeQuery=true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	@Query(value="select * from Users u where u.last_name=:lastName",nativeQuery=true)
	List<UserEntity> findUserByLastName(@Param("lastName")String lastName);
	
	
	//basically we are doing search with this 
	@Query(value="select * from Users u where first_name LIKE %:keyword% or last_name LIKE %:keyword%",nativeQuery=true)//we can % play with this based on what we want
	List<UserEntity> findUsersByKeyword(@Param("keyword")String keyword);
	
	@Query(value="select u.first_name,u.last_name from Users u where u.first_name LIKE %:keyword% or u.last_name LIKE %:keyword%",nativeQuery=true)//we can % play with this based on what we want
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword")String keyword);
	
	
	
	@Transactional
	@Modifying
	@Query(value="update users u set u.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where u.user_id=:userId",nativeQuery=true)
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,@Param("userId")String userId);
	
	
	// Using Java Persistence Query Language(JPQL)
	
	@Query("select user from UserEntity user where user.userId=:userId")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	
	@Query("select user.firstName,user.lastName from UserEntity user where user.userId=:userId")
	List<Object []> getUserEntityFullNameById(@Param("userId") String userId);
	
	//JPQL
	@Modifying
	@Transactional
	@Query(value="update UserEntity u set u.emailVerificationStatus=:emailVerificationStatus where u.userId=:userId")
	void updateUserEntityEmailVerificationStatus(
			@Param("emailVerificationStatus") boolean emailVerificationStatus,
			@Param("userId")String userId);
	
	
}

