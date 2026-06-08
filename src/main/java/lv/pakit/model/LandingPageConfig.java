package lv.pakit.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "landing_page_config")
public class LandingPageConfig {

    @Id
    @Column(name = "config_key", length = 100)
    private String configKey;

    @Column(name = "value_lv", columnDefinition = "TEXT")
    private String valueLv;

    @Column(name = "value_en", columnDefinition = "TEXT")
    private String valueEn;

    /** "text" or "image" */
    @Column(name = "config_type", length = 10, nullable = false)
    private String configType;

    @Column(name = "section", length = 50, nullable = false)
    private String section;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
