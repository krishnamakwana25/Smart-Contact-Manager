package com.smartcontactmanager.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcontactmanager.entities.MyOrder;

public interface MyOrderRepository extends JpaRepository<MyOrder, Long>
{
	public MyOrder findByOrderid(String orderid);
}
