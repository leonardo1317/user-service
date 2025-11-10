package io.github.leonardofrs.user_service.infrastructure.repository.h2.entity;

import static java.util.Objects.requireNonNullElse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  private UUID id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<PhoneEntity> phones = new ArrayList<>();

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime created;

  @Column(name = "modified_at")
  private LocalDateTime modified;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<UserSessionEntity> sessions = new ArrayList<>();

  @Column(name = "is_active", nullable = false)
  private boolean active = true;

  protected UserEntity() {
  }

  public UserEntity(UUID id, String name, String email, String password, LocalDateTime created,
      LocalDateTime modified, LocalDateTime lastLogin, boolean active) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.created = created;
    this.modified = modified;
    this.lastLogin = lastLogin;
    this.active = active;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public List<PhoneEntity> getPhones() {
    return List.copyOf(phones);
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public LocalDateTime getModified() {
    return modified;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public List<UserSessionEntity> getSessions() {
    return List.copyOf(sessions);
  }

  public boolean isActive() {
    return active;
  }

  public void addPhones(List<PhoneEntity> newPhones) {
    this.phones = requireNonNullElse(phones, new ArrayList<>());
    newPhones.stream().filter(Objects::nonNull).forEach(this::addPhone);
  }

  private void addPhone(PhoneEntity phone) {
    this.phones.add(phone);
    if (phone.getUser() != this) {
      phone.setUser(this);
    }
  }

  public void addSessions(List<UserSessionEntity> newSessions) {
    this.sessions = requireNonNullElse(sessions, new ArrayList<>());
    newSessions.stream().filter(Objects::nonNull).forEach(this::addSession);
  }

  private void addSession(UserSessionEntity session) {
    this.sessions.add(session);
    if (session.getUser() != this) {
      session.setUser(this);
    }
  }
}
