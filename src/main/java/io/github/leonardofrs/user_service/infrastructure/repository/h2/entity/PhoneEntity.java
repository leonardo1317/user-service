package io.github.leonardofrs.user_service.infrastructure.repository.h2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "phones")
public class PhoneEntity {

  @Id
  private UUID id;

  @Column(nullable = false, length = 50)
  private String number;

  @Column(name = "city_code", length = 10)
  private String cityCode;

  @Column(name = "country_code", length = 10)
  private String countryCode;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  protected PhoneEntity() {
  }

  public PhoneEntity(
      UUID id,
      String number,
      String cityCode,
      String countryCode
  ) {
    this.id = id;
    this.number = number;
    this.cityCode = cityCode;
    this.countryCode = countryCode;
  }

  public UUID getId() {
    return id;
  }

  public UserEntity getUser() {
    return user;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public String getCityCode() {
    return cityCode;
  }

  public String getNumber() {
    return number;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }
}
