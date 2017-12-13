package com.locus.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository  extends CrudRepository<UserData, Long> {

    UserData findByUserid(@Param("username") String username );


}
