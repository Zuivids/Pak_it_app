package lv.pakit.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lv.pakit.model.Classifier;

@Getter
@RequiredArgsConstructor
public enum UserRole implements Classifier {

    ADMIN("Admin"),
    DRIVER("Driver");

    private final String publicName;
}
