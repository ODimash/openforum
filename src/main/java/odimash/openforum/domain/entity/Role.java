package odimash.openforum.domain.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controlled_forum_id")
    private Forum controlledForum;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    @ElementCollection(targetClass = Rights.class)
    @CollectionTable(name = "role_rights", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "right_value", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Set<Rights> rights = new HashSet<Rights>();
}
