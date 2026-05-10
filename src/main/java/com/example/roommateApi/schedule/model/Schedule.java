package com.example.roommateApi.schedule.model;

import com.example.roommateApi.core.model.BaseEntity;
import com.example.roommateApi.household.model.Household;
import com.example.roommateApi.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE schedules SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@EqualsAndHashCode(callSuper = true)
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "household_id", nullable = false)
    private Household household;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType resourceType;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Builder.Default
    @Column(nullable = false)
    private boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private RecurrencePattern recurrencePattern = RecurrencePattern.NONE;
}
