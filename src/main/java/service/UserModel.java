package service;

import org.hibernate.validator.constraints.Email;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "Users")
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Email(message = "Invalid email address!")
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @OneToMany(targetEntity = CreditCardModel.class, mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CreditCardModel> creditCardList;

    public UserModel() {
        this.creditCardList = new ArrayList<CreditCardModel>();
    }

    public UserModel(String firstName, String lastName, String address, String email, List<CreditCardModel> creditCardList) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.address = address;
        this.email = email;
        this.creditCardList = creditCardList;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    @OneToMany(targetEntity = CreditCardModel.class, mappedBy = "user", cascade = CascadeType.ALL)
    public List<CreditCardModel> getCreditTokens() {
        return creditCardList;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setCreditCardList(List<CreditCardModel> creditCardList) {
        creditCardList.forEach(c -> c.setUser(this));
        this.creditCardList = creditCardList;
    }

    public void addCreditCard(CreditCardModel i_CreditCardModel) {
        i_CreditCardModel.setUser(this);
        creditCardList.add((i_CreditCardModel));
    }

    public void setAddress(String i_Address) {
        address = i_Address;
    }

    public void setFirstName(String i_FirstName) {
        firstname = i_FirstName;
    }

    public void setLastName(String i_LastName) {
        lastname = i_LastName;
    }

    public void setEmail(String i_Email) {
        email = i_Email;
    }
}
