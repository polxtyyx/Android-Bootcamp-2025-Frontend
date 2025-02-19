package ru.sicampus.bootcamp2025.ui.user_profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.squareup.picasso.Picasso;

import ru.sicampus.bootcamp2025.R;
import ru.sicampus.bootcamp2025.data.UserRepositoryImpl;
import ru.sicampus.bootcamp2025.databinding.UserProfileFragmentBinding;
import ru.sicampus.bootcamp2025.domain.entities.FullUserEntity;
import ru.sicampus.bootcamp2025.domain.sign.LogoutUseCase;
import ru.sicampus.bootcamp2025.domain.user.UpdateUserProfileUseCase;

public class UserProfileFragment extends Fragment {

    private boolean isEdit = false;

    private final FullUserEntity user;

    private String url;

    private UserProfileFragmentBinding binding;

    private final UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            UserRepositoryImpl.getInstance()
    );

    public UserProfileFragment(FullUserEntity user) {
        super(R.layout.user_profile_fragment);
        this.user = user;
        this.url = user.getPhotoUrl();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = UserProfileFragmentBinding.bind(view);

        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(binding.image);
        }

        binding.etNickname.setText(user.getNickname());
        binding.etName.setText(user.getName());
        binding.etEmail.setText(user.getEmail());

        binding.etName.setFocusable(false);
        binding.etName.setFocusableInTouchMode(false);
        binding.etName.setClickable(false);

        binding.etNickname.setFocusable(false);
        binding.etNickname.setFocusableInTouchMode(false);
        binding.etNickname.setClickable(false);

        binding.etEmail.setFocusable(false);
        binding.etEmail.setFocusableInTouchMode(false);
        binding.etEmail.setClickable(false);

        binding.edit.setOnClickListener(v -> {
            if (!isEdit) {
                binding.edit.setImageDrawable(getResources().getDrawable(R.drawable.ok));

                binding.etName.setFocusable(true);
                binding.etName.setFocusableInTouchMode(true);
                binding.etName.setClickable(true);

                binding.etNickname.setFocusable(true);
                binding.etNickname.setFocusableInTouchMode(true);
                binding.etNickname.setClickable(true);

                binding.etEmail.setFocusable(true);
                binding.etEmail.setFocusableInTouchMode(true);
                binding.etEmail.setClickable(true);
            } else {
                String newName = binding.etName.getText().toString();
                String newNickname = binding.etNickname.getText().toString();
                String newEmail = binding.etEmail.getText().toString();

                if (newEmail == null || newEmail.isEmpty())
                    Toast.makeText(getActivity(), "Email cannot be null", Toast.LENGTH_SHORT).show();
                else if (newName == null || newName.isEmpty())
                    Toast.makeText(getActivity(), "Name cannot be null", Toast.LENGTH_SHORT).show();
                else if (newNickname == null || newNickname.isEmpty())
                    Toast.makeText(getActivity(), "Nickname cannot be null", Toast.LENGTH_SHORT).show();
                else {
                    updateUserProfileUseCase.execute(user.getId(),
                            newName,
                            newNickname,
                            newEmail,
                            user.getPhotoUrl(),
                            status -> {
                            });
                    binding.etName.setFocusable(false);
                    binding.etName.setFocusableInTouchMode(false);
                    binding.etName.setClickable(false);

                    binding.etNickname.setFocusable(false);
                    binding.etNickname.setFocusableInTouchMode(false);
                    binding.etNickname.setClickable(false);

                    binding.etEmail.setFocusable(false);
                    binding.etEmail.setFocusableInTouchMode(false);
                    binding.etEmail.setClickable(false);

                    binding.edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_edit));

                    isEdit = false;
                }
            }
        });

    }

    private void startCrop() {
        CropImageOptions options = new CropImageOptions();
        options.imageSourceIncludeCamera = false;
        options.imageSourceIncludeGallery = true;
        options.aspectRatioX = 1;
        options.aspectRatioY = 1;
        options.cropShape = CropImageView.CropShape.RECTANGLE;
        options.fixAspectRatio = true;
        options.showCropOverlay = true;
        options.outputCompressFormat = Bitmap.CompressFormat.PNG;

        CropImageContractOptions cropOptions = new CropImageContractOptions(null, options);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
