package ru.sicampus.bootcamp2025.ui.center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ru.sicampus.bootcamp2025.data.CenterRepositoryImpl;
import ru.sicampus.bootcamp2025.data.UserRepositoryImpl;
import ru.sicampus.bootcamp2025.domain.center.GetCenterByIdUseCase;
import ru.sicampus.bootcamp2025.domain.entities.FullCenterEntity;
import ru.sicampus.bootcamp2025.domain.entities.ItemUserEntity;
import ru.sicampus.bootcamp2025.domain.user.GetActiveUsersInCenter;

public class CenterViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveDataState = new MutableLiveData<>();

    public final LiveData<State> liveDataState = mutableLiveDataState;

    private final GetCenterByIdUseCase getCenterByIdUseCase = new GetCenterByIdUseCase(
            CenterRepositoryImpl.getInstance()
    );

    public void load(@NonNull String centerId) {
        mutableLiveDataState.setValue(new State(null, null, true));
        getCenterByIdUseCase.execute(centerId, (status) -> {
            mutableLiveDataState.postValue(new State(
                    status.getErrors() != null ? status.getErrors().getLocalizedMessage() : null,
                    status.getValue(),
                    false
            ));
        });
    }

    public class State {
        @Nullable
        private final String errorMessage;

        @Nullable
        private final FullCenterEntity center;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable FullCenterEntity center, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.center = center;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public FullCenterEntity getCenter() {
            return center;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
