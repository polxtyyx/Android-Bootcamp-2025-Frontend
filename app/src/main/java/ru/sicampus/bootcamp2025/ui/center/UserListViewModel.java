package ru.sicampus.bootcamp2025.ui.center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.sicampus.bootcamp2025.data.UserRepositoryImpl;
import ru.sicampus.bootcamp2025.domain.entities.ItemUserEntity;
import ru.sicampus.bootcamp2025.domain.entities.Status;
import ru.sicampus.bootcamp2025.domain.user.GetActiveUsersInCenter;

public class UserListViewModel extends ViewModel {

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();

    public final LiveData<State> stateLiveDate = mutableStateLiveData;


    private final GetActiveUsersInCenter getActiveUsersInCenter = new GetActiveUsersInCenter(
            UserRepositoryImpl.getInstance()
    );

    public UserListViewModel(@NonNull String centerId){
        update(centerId);
    }


    public void update(@NonNull String centerId){
        mutableStateLiveData.setValue(new State(null, null, true));
        getActiveUsersInCenter.execute(centerId, status -> {
            mutableStateLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemUserEntity>> status) {
        return new State(
                status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }


    public class State {
        @Nullable
        private final String errorMessage;

        @Nullable
        private final List<ItemUserEntity> userList;

        private final boolean isLoading;

        public State(@Nullable String errorMessage,
                     @Nullable List<ItemUserEntity> userList,
                     boolean isLoading) {
            this.errorMessage = errorMessage;
            this.userList = userList;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public List<ItemUserEntity> getUserList() {
            return userList;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
