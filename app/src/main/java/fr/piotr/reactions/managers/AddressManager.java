package fr.piotr.reactions.managers;

import android.content.Context;
import android.location.Address;

import java.util.List;
import java.util.UUID;

import fr.piotr.reactions.daos.AddressDAO;
import fr.piotr.reactions.persistence.AddressReference;

/**
 * Created by piotr_000 on 21/01/2017.
 *
 */

public class AddressManager {

    Context context;
    private AddressDAO addressDAO;

    private List<AddressReference> addressReferences;

    public List<AddressReference> getAddressReferences() {
        if(addressReferences==null){
            addressReferences = addressDAO.loadAddresses(context);
        }
        return addressReferences;
    }

    public AddressManager(Context context){
        this.context = context;
        addressDAO = new AddressDAO();
    }

    public AddressReference add(Address address) {
        List<AddressReference> addressReferences = getAddressReferences();
        AddressReference addressReference = new AddressReference(UUID.randomUUID(), address);
        addressReferences.add(addressReference);
        addressDAO.save(context, addressReferences);
        return addressReference;
    }

    public AddressReference get(UUID uuid) {
        List<AddressReference> addressReferences = getAddressReferences();
        for (AddressReference addressReference : addressReferences) {
            if(addressReference.getId().equals(uuid)){
                return addressReference;
            }
        }
        return null;
    }

    public void remove(AddressReference addressReference) {
        List<AddressReference> addressReferences = getAddressReferences();
        addressReferences.remove(addressReference);
        addressDAO.save(context, addressReferences);
    }
}
