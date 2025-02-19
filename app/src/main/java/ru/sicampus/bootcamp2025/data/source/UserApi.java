package ru.sicampus.bootcamp2025.data.source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.sicampus.bootcamp2025.data.dto.AccountDto;
import ru.sicampus.bootcamp2025.data.dto.UserDto;
import ru.sicampus.bootcamp2025.data.utils.Container;

public interface UserApi {

    @GET("/api/user/{id}")
    Call<UserDto> getById(@Path("id") String id);

    @PUT("/api/person/{id}")
    Call<UserDto> update(@Path("id") String id, @Body Container container);

    @DELETE("/api/person/{id}")
    Call<Void> delete(@Path("id") String id);

    @GET("/api/person/username/{username}")
    Call<Void> isExist(@Path("username") String login);

    @POST("/api/person/register")
    Call<Void> register(@Body AccountDto dto);

    @GET("/api/person/login")
    Call<Void> login();

    @GET("/api/person/inactive")
    Call<List<UserDto>> getInactive();

    @GET("/api/person/{centerId}/volunteers")
    Call<List<UserDto>> getActiveUsersInCenter(@Path("centerId") String centerId);

    @POST("/api/person/volunteers/{volunteer_id}/centers/{center_id}")
    Call<Void> addUserToCenter(@Path("center_id") String centerId, @Path("user_id") String userId);

    @DELETE("/api/person/volunteers/{volunteer_id}/centers")
    Call<Void> deleteUserFromCenter(@Path("volunteer_id") String volunteerId);
}
