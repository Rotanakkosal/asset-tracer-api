package com.kshrd.asset_tracer_api.repository;

import com.kshrd.asset_tracer_api.config.UuidTypeHandler;
import com.kshrd.asset_tracer_api.model.entity.EmailVerification;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.UUID;

@Mapper
public interface EmailVerificationRepository {

    @Select("""
                insert into email_verification (user_id, code)
                values (#{user_id}, #{code})
            """)
    void insertEmailVerification(UUID user_id, String code);

    @Select("""
            insert into email_verification (user_id, code, is_verified)
            values (#{user_id}, #{code}, true)
            """)
    void insertEmailVerify(UUID user_id, String code);


    @Select("""
                select * from email_verification where user_id = #{userId}
            """)
    @ResultMap("emailVerificationMap")
    EmailVerification getEmailVerificationByUserId(@Param("userId") UUID userId);

    @Results( id = "emailVerificationMap",
            value = {
                    @Result(column = "id", property = "id", jdbcType = JdbcType.OTHER, typeHandler = UuidTypeHandler.class),
                    @Result(column = "user_id", property = "userId"),
                    @Result(column = "is_verified", property = "isVerified"),
                    @Result(column = "expire_at", property = "expireAt"),
                    @Result(column = "created_at", property = "createdAT"),
            })
    @Select("""
                select * from email_verification where code = #{code}
            """)
    EmailVerification getEmailVerificationByCode(@Param("code") String code);

    @Update("""
                update email_verification set is_verified = true where code = #{code}
            """)
    void updateIsVerified(String code);

    @Select("""
            select * from email_verification
            where user_id = #{userId} and code = #{code}
            """)
    EmailVerification getEmailVerification(UUID userId, String code);

}
