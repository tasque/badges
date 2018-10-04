package org.badges.db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.badges.db.campaign.BadgeCampaignRule;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.Version;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
@Entity
@Where(clause = "deleted=false")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    private String category;

    private String imageUrl;

    private boolean enabled;

    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;


    @OneToOne
    @PrimaryKeyJoinColumn
    private BadgeCampaignRule badgeCampaignRule;


    @Version
    private int version;

}
