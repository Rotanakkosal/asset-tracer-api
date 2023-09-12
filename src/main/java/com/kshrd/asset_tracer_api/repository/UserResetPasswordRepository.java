package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.model.entity.UserResetPassword;
import com.kshrd.asset_tracer_api.model.request.UserPasswordRequest;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface UserResetPasswordRepository {

    @Select("""
            insert into user_reset_password(user_id, code)
            values(#{userId}, #{code})
            returning *
            """)
    UserResetPassword addUserResetPassword(UUID userId, String code);

    @Results(
            id = "userResetPasswordMapper",
            value = {
                    @Result(property = "userId", column = "user_id")
            }
    )
    @Select("""
            select * from user_reset_password
            where user_id = #{userId} and code = #{code}
            """)
    UserResetPassword getUserResetPassword(@Param("userId") UUID userId, String code);
}
