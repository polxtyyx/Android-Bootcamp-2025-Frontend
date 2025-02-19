package ru.sicampus.bootcamp2025.domain.user;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.sicampus.bootcamp2025.domain.entities.Status;

public class AddUserToCenterUseCase {

    public final UserRepository repo;

    public AddUserToCenterUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String centerId,
                        @NonNull String userId,
                        Consumer<Status<Void>> callback){
        repo.addUserToCenter(centerId, userId, callback);
    }
}
