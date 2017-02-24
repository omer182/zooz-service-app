package service;

import exceptions.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

@Service
@RestController
public class UserOperationsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * retrieve user from database
     *
     * @param id desired user id
     * @return user from database
     * @throws UserNotFoundException if no such user in database
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public UserModel GetUser(@PathVariable int id) {
        UserModel um = userRepository.findOne(id);
        if (um == null)
            throw new UserNotFoundException(id);
        return um;
    }

    /**
     * Inserts a new user to the database
     *
     * @param userModel the user to be inserted
     * @return "OK" if input valid
     * @throws NullFieldException cannot accept a field that is nullable
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public
    @ResponseBody
    String InsertUser(@RequestBody UserModel userModel) {
        try {
            userRepository.save(userModel);
        } catch (ConstraintViolationException e) {
            throw new NullFieldException(e);

        } catch (DataIntegrityViolationException e) {
            throw new UniqueConstraintException(e);
        }
        return "OK";
    }

    /**
     * update a user in the database
     *
     * @param id the id of the user to be updated
     * @param um the info of the user to updated to
     * @return "OK" if update succeeded
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    String UpdateUser(@PathVariable int id, @RequestBody UserModel um) {
        try {
        /* I don't like this at all */
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE users SET ");
            if (um.getFirstname() != null) {
                sb.append(String.format("firstname = '%s', ", um.getFirstname()));
            }
            if (um.getLastname() != null) {
                sb.append(String.format("lastname = '%s', ", um.getLastname()));
            }
            if (um.getEmail() != null) {
                sb.append(String.format("email = '%s', ", um.getEmail()));
            }
            if (um.getAddress() != null) {
                sb.append(String.format("address = '%s', ", um.getAddress()));
            }
            sb.deleteCharAt(sb.length() - 2).append("WHERE id = " + id);

            Session session = sessionFactory.openSession();
            Query query = session.createSQLQuery(sb.toString());
            if (query.executeUpdate() == 0) {
                throw new UserNotFoundException(id);
            }
        } catch (ConstraintViolationException e) {
            throw new NullFieldException(e);
        } catch (Exception e) {
            throw new UniqueConstraintException(e);
        }
        return "OK";
    }

    /**
     * delete user with id {id} from the database
     *
     * @param id id of the user to be deleted
     * @return "OK" if succeeded
     * @throws UserNotFoundException if no such user in database
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    String DeleteUser(@PathVariable int id) {
        try {
            userRepository.delete(id);
        } catch (Exception e) {
            throw new UserNotFoundException(id);
        }
        return "OK";
    }

    /**
     * add credit card tokens to user with id = {id}
     *
     * @param id             the user id to add credit card tokens to
     * @param creditCardList list of tokens to be added
     * @return
     */
    @RequestMapping(value = "/creditcards/{id}", method = {RequestMethod.POST, RequestMethod.PUT})
    public
    @ResponseBody
    String AddCreditCards(@PathVariable int id, @RequestBody List<CreditCardModel> creditCardList) {
        UserModel um = userRepository.findOne(id);
        if (um == null)
            throw new UserNotFoundException(id);
        creditCardList.forEach(c -> um.addCreditCard(c));
        userRepository.save(um);
        return "OK";
    }

    /**
     * deletes the given credit card tokens from the database
     *
     * @param id             user id who's tokens will be deleted
     * @param creditCardList list of tokens to be deleted
     * @return "OK" if succeeded
     * @throws UserNotFoundException if no such user in database
     */
    @RequestMapping(value = "/creditcards/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    String DeleteCreditCards(@PathVariable int id, @RequestBody List<CreditCardModel> creditCardList) {
        if (creditCardList.size() == 0) {
            throw new NullFieldException("no token were given");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("delete from credit_cards where user_id = '" + id + "' and (");
        creditCardList.forEach(c -> sb.append("token = '" + c.getToken() + "' OR "));
        sb.setLength(sb.length() - 4);
        sb.append(")");
        Session session = sessionFactory.openSession();
        Query query = session.createSQLQuery(sb.toString());

        if (query.executeUpdate() == 0) {
            throw new UserNotFoundException(id);
        }
        return "OK";
    }
}