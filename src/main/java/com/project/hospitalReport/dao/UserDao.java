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

}
