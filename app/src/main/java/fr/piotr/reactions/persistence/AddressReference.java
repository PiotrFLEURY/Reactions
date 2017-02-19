package fr.piotr.reactions.persistence;

import android.location.Address;

import java.util.UUID;

/**
 * Created by piotr_000 on 21/01/2017.
 *
 */

public class AddressReference {

    private UUID id;
    private Address address;

    public AddressReference(UUID id, Address address){
        this.id=id;
        this.address=address;
    }

    public UUID getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }
}
