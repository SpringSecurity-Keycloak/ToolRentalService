package io.rentalapp.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Base class that every JPA related persistent class needs to implement
 */
@Entity
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;
}
