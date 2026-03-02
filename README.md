# 💳 BankingAndFraudDetection
A Rule-Driven Banking and Fraud Detection System (Java OOP Project)

---

## 📌 Overview

BankingAndFraudDetection is a console-based banking simulation system designed to demonstrate advanced Object-Oriented Programming principles in Java.

The system models:

- 👤 Customers
- 🏦 Bank Accounts (Checking, Savings)
- 💳 Cards (Debit, Virtual)
- 💰 Transactions (Deposit, Withdrawal, Transfer, Card Payment)
- 📒 A Ledger for financial consistency
- 🛡️ A Rule-Based Fraud Detection Engine

The architecture emphasizes inheritance, polymorphism, encapsulation, composition, and separation of concerns.

---

## 🚀 Core Features

### 👤 Customer Management
- Create customers
- Each customer can own multiple accounts
- Each customer can hold multiple cards

### 🏦 Account System
- Open Checking or Savings accounts
- Deposit funds
- Withdraw funds
- Transfer between accounts
- Account status handling (ACTIVE, FROZEN, CLOSED)

### 💳 Card Payments
- Issue debit cards linked to accounts
- Daily spending limits
- Card authorization logic
- Merchant-based payments

---

## 💰 Transactions

Supported transaction types:

- ➕ Deposit
- ➖ Withdrawal
- 🔁 Transfer
- 🛒 CardPayment

Each transaction:

- Has a unique ID
- Has a timestamp
- Has a status (CREATED, APPROVED, REVIEW, DECLINED, POSTED)
- Is stored in a central Ledger
- Becomes immutable after posting

---

## 🛡️ Fraud Detection Engine

All risky transactions (withdrawal, transfer, card payment) are evaluated before posting.

Fraud rules return one of:

- ✅ ALLOW
- ⚠️ REVIEW
- ❌ BLOCK

Behavior:

- ✅ ALLOW → Transaction is posted and balances update.
- ⚠️ REVIEW → Transaction is stored but balances do not change.
- ❌ BLOCK → Transaction is declined and balances do not change.

Example rule types:

- 💵 Large amount threshold rule
- 📊 Daily spending limit rule
- ⏱️ High transaction frequency rule
- 🏪 New merchant rule
- 🌍 Rapid location change rule

---

## 🏗️ Architecture

Project structure:

domain/
    customer/
    account/
    card/
    transaction/

rules/
service/
exceptions/
util/

---

### 📦 Domain Layer
Contains business entities:

- Customer
- Account (abstract)
- CheckingAccount
- SavingsAccount
- Card (abstract)
- DebitCard
- Transaction hierarchy
- Ledger

### 🧠 Rules Layer
- FraudRule interface
- Rule implementations
- FraudEngine

### 🛠️ Service Layer
- BankService (application façade)
- Coordinates repositories, ledger, and fraud engine

---

## 🧩 Design Principles

- 🔒 Encapsulation: Account balances cannot be modified directly.
- 🧬 Inheritance: Account, Transaction, and Card hierarchies.
- 🔄 Polymorphism: Transaction posting and fraud rule evaluation.
- 🧱 Composition: Customer owns Accounts and Cards.
- 🎯 Strategy Pattern: FraudRule implementations.
- 🏛️ Clear separation between domain, service, and storage layers.

---

## 📏 System Invariants

- 💰 Money is immutable.
- 📒 Account balances change only through Ledger posting.
- 🔁 Transactions are atomic (no partial transfer).
- 🌐 Currency mismatch is disallowed.
- 🛑 Fraud REVIEW or BLOCK never alters balances.

---

## 🧪 Example Scenario

1. 👤 Create Customer
2. 🏦 Open Checking Account (EUR)
3. ➕ Deposit 1000 EUR
4. 💳 Issue Debit Card (daily limit: 500 EUR)
5. 🛒 Card Payment 200 EUR → ✅ ALLOW → balance becomes 800 EUR
6. 🛒 Card Payment 600 EUR → ❌ BLOCK → balance remains 800 EUR

---

## 🎯 Project Goal

This project reinforces:

- 💻 Advanced Java OOP structure
- 🧠 Domain modeling
- 🏗️ Clean layered architecture
- 🛡️ Rule-based system design
- 💼 Realistic financial system simulation

No external frameworks are required.  
The system is fully self-contained and intended for educational purposes.
