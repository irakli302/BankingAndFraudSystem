package bankingandfraudsystem.domain.merchant;

import bankingandfraudsystem.util.Money;

import java.util.Objects;
import java.util.UUID;

public class Merchant {
    private final UUID id;
    private String fullName;
    private String country;

    public Merchant(String fullname, String ctr) {
        if(fullname == null)
            throw new IllegalArgumentException("FullName cannot be null!");
        if(fullname.isBlank())
            throw new IllegalArgumentException(("FullName cannot be blank!"));
        if(ctr == null)
            throw new IllegalArgumentException("Country cannot be null!");
        if(ctr.isBlank())
            throw new IllegalArgumentException("Country cannot be blank!");

        this.id = UUID.randomUUID();
        this.fullName = fullname;
        this.country = ctr;
    }

    public UUID getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getCountry() {
        return this.country;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this==obj) return true;

        Merchant merchant = (Merchant) obj;
        return this.id.equals(merchant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
