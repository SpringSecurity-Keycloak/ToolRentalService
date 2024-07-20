package io.rentalapp.common;


import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * Base class that every JPA related persistent class needs to implement
 */
@Entity
public abstract class BaseEntity {

    protected static final Logger log = LoggerFactory.getLogger(BaseEntity.class);

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;
}
