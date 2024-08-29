package odimash.openforum.domain.entity;


import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Forum parentForum;

    @OneToMany(mappedBy = "parentForum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Forum> subCategories;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Topic> topics;
}
