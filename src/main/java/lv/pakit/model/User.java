package lv.pakit.model;

import jakarta.persistence.*;
import lombok.*;
import lv.pakit.model.converter.CryptoConverter;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Table(name = "user")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(value = AccessLevel.NONE)
    private long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column
    @Convert(converter = CryptoConverter.class)
    private String totpSecret;

    @Column
    private Boolean totpConfirmed;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public boolean isTotp() {
        return totpSecret != null && Boolean.TRUE.equals(totpConfirmed);
    }
}
