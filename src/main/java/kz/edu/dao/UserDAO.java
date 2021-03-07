package kz.edu.dao;

import kz.edu.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDAO
{
    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAO(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public User findByUserName(String username)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user;
        try
        {
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<User> q1 = builder1.createQuery(User.class);
            Root<User> root1 = q1.from(User.class);

            Predicate predicateUsername = builder1.equal(root1.get("login"), username);
            user = session.createQuery(q1.where(predicateUsername)).getSingleResult();
            //System.out.println("available authorities:"+user.getRole().getAuthorities());
            //System.out.println("USER DAO. Email:"+user.getEmail()+". Role: "+user.getRole().getId()+". Password: "+user.getPassword());
        }
        catch (NoResultException noResultException)
        {
            user = null;
        }
        finally
        {
            //session.close();
        }
        return user;
    }
    public void change_password(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try{
            session.merge(user);
            session.getTransaction().commit();
        }finally {
            session.close();
        }
    }
    public void addUser(User user)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try
        {
            CriteriaBuilder builder1 = session.getCriteriaBuilder();
            CriteriaQuery<Role> q1 = builder1.createQuery(Role.class);
            Root<Role> root1 = q1.from(Role.class);

            Predicate predicateRole = builder1.equal(root1.get("name"), "ROLE_USER");
            //Predicate predicateRole = builder1.equal(root1.get("name"), "ROLE_ADMIN");
            Role role = session.createQuery(q1.where(predicateRole)).getSingleResult();
            user.setRole(role);

            session.persist(user);
            session.getTransaction().commit();
        }
        finally
        {
            session.close();
        }
    }

    public Votes getLastVote(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Votes> votes;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Votes> query = builder.createQuery(Votes.class);
            Root<Votes> root = query.from(Votes.class);

            Predicate predicate = builder.equal(root.get("user_id"), user.getId());
            votes = session.createQuery(query.where(predicate)).getResultList();
        }  catch (NoResultException noResultException) {
            votes = null;
        }finally {
            session.close();
        }
        try {
            return votes.get(votes.size()-1);
        }catch (Exception e){
            return null;
        }
    }

    public void editRole(User user, String name){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user.setRole(find_role_by_id(name));
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Role find_role_by_id(String name){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Role role;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Role> query = builder.createQuery(Role.class);
            Root<Role> root = query.from(Role.class);

            Predicate predicate = builder.equal(root.get("name"), name);
            role = session.createQuery(query.where(predicate)).getSingleResult();
        }  catch (NoResultException noResultException) {
            role = null;
        }finally {
            session.close();
        }
        return role;
    }

    public void deleteRole(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            user.setRole(null);
            session.merge(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public Authority find_authority_by_id(int id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Authority authority;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Authority> query = builder.createQuery(Authority.class);
            Root<Authority> root = query.from(Authority.class);

            Predicate predicate = builder.equal(root.get("authority_id"), id);
            authority = session.createQuery(query.where(predicate)).getSingleResult();
        }  catch (NoResultException noResultException) {
            authority = null;
        }finally {
            session.close();
        }
        return authority;
    }

    public void addAuthority(int authority_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Role role = find_role_by_id("ROLE_USER");
            role.getAuthorities().add(find_authority_by_id(authority_id));
            session.merge(role);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void deleteAuthority(int authority_id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            Authority authority = find_authority_by_id(authority_id);
            authority.setRoles(null);
            session.merge(authority);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public List<User> getAllUsers(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> userList;
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> root = criteria.from(User.class);

            criteria.select(root);
            Query<User> query = session.createQuery(criteria);
            userList = query.getResultList();
            session.getTransaction().commit();
        }finally {
            session.close();
        }
        return userList;
    }
}