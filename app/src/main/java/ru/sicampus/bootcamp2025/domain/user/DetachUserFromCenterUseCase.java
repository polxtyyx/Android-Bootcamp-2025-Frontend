package ru.sicampus.bootcamp2025.domain.user;

import androidx.annotation.NonNull;

import java.util.function.Consumer;

import ru.sicampus.bootcamp2025.domain.entities.Status;

public class DetachUserFromCenterUseCase {

    public final UserRepository repo;

    public DetachUserFromCenterUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(@NonNull String userId, Consumer<Status<Void>> callback) {
        repo.detachUser(userId, callback);
    }
}
