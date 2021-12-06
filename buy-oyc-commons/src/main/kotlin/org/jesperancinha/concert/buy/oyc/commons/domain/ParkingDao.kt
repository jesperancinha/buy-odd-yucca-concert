package org.jesperancinha.concert.buy.oyc.commons.domain

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Introspected
data class ParkingReservation(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     val id: Long? = null,
     val parkingNumber: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ParkingReservation

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , parkingNumber = $parkingNumber )"
    }
}

@Repository
interface ParkingReservationRepository : CrudRepository<ParkingReservation, Long>