package betcore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "markets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SelectionEntity> selections = new ArrayList<>();
}
