package ru.sicampus.bootcamp2025.ui.center;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import ru.sicampus.bootcamp2025.R;
import ru.sicampus.bootcamp2025.databinding.VolunteerCenterFragmentBinding;
import ru.sicampus.bootcamp2025.domain.entities.FullCenterEntity;
import ru.sicampus.bootcamp2025.ui.utils.Utils;
import ru.sicampus.bootcamp2025.ui.volunteer_profile.VolunteerProfileFragment;

public class CenterFragment extends Fragment {

    private static final String KEY_ID = "id";

    private VolunteerCenterFragmentBinding binding;

    private CenterViewModel centerViewModel;

    private UserListViewModel userListViewModel;

    public CenterFragment() {
        super(R.layout.volunteer_center_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = VolunteerCenterFragmentBinding.bind(view);
        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        userListViewModel = new ViewModelProvider(this).get(UserListViewModel.class);
        final String centerId = getArguments() != null ? getArguments().getString(KEY_ID) : null;
        if (centerId == null) throw new IllegalStateException("ID is null");

        binding.refresh.setOnRefreshListener(() -> userListViewModel.update(centerId));
        final CenterAdapter adapter = new CenterAdapter(id -> openProfile(id));
        binding.activeVolunteers.setAdapter(adapter);
        subscribe(userListViewModel, adapter);

        centerViewModel.liveDataState.observe(getViewLifecycleOwner(), state -> {
            final FullCenterEntity entity = state.getCenter();
            if (entity == null) return;
            binding.centerName.setVisibility(Utils.visibleOrGone(entity != null));
            binding.address.setVisibility(Utils.visibleOrGone(entity != null));
        });
    }

    private void openProfile(String id) {
        final View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(
                R.id.action_centerFragment_to_volunteerProfile,
                VolunteerProfileFragment.getBundle(id));
    }

    private void subscribe(final UserListViewModel userListViewModel, final CenterAdapter adapter) {
        userListViewModel.stateLiveDate.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getUserList() != null;
            binding.refresh.setEnabled(!state.isLoading());
            if (!state.isLoading()) binding.refresh.setRefreshing(false);
            binding.activeVolunteers.setVisibility(Utils.visibleOrGone(isSuccess));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.loading.setVisibility(Utils.visibleOrGone(state.isLoading()));
            binding.error.setText(state.getErrorMessage());
            if (isSuccess) {
                adapter.updateData(state.getUserList());
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
}