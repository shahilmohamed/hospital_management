package com.project.hospitalReport.dao;

import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.project.hospitalReport.dto.User;
import com.project.hospitalReport.helper.ConfigClass;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UserDao {
	
	public HashMap<String, String> signup(User user)
	{
		User u = new User();
		u.setUsername(user.getUsername());
		u.setRole(user.getRole());
		u.setEnabled(true);
		u.setPassword(user.getPassword());
		Session session = ConfigClass.getSession().openSession();
		Transaction transaction = session.beginTransaction();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Predicate condition = builder.equal(root.get("username"), user.getUsername());
		query.select(root).where(condition);
		User result = session.createQuery(query).getSingleResultOrNull();
		HashMap<String, String> value = new HashMap<>();
		if(result==null)
		{
			session.save(u);
			transaction.commit();
		}
		else
		{
			session.close();
			value.put("msg", "User present");
			return value;
		}
		session.close();
		value.put("msg", "Account created");
		return value;
	}
	
	public User login(User user)
	{
		Session session = ConfigClass.getSession().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Predicate cond1 = builder.equal(root.get("username"), user.getUsername());
		Predicate cond2 = builder.equal(root.get("password"), user.getPassword());
		Predicate cond3 = builder.equal(root.get("role"),user.getRole());
		query.select(root).where(builder.and(cond1,cond2,cond3));
		User result = session.createQuery(query).getSingleResultOrNull();
		return result;
	}

}
