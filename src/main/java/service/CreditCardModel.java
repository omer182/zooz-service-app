package service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Table(name = "Credit_cards")
@Entity
public class CreditCardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true)
    private String token;

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL)
    private UserModel user;

    public CreditCardModel() {
    }

    public CreditCardModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @ManyToOne(targetEntity = UserModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user")
    @JsonIgnore
    public UserModel getUserID() {
        return user;
    }

    public void setUser(UserModel um) {
        user = um;
    }
}
