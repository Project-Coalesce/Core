package com.coalesce.user.base;

import java.util.UUID;

public final class User {

	private final UUID uuid;
	private double balance;

	public User(UUID uuid, double balance) {
		this.uuid = uuid;
		this.balance = balance;
	}

	public UUID getUuid() {
		return uuid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
