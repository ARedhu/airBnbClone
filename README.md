# 🏠 Airbnb Clone

A hotel booking and management backend system built with Spring Boot and PostgreSQL.

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## 🗄️ Database Structure

![Database Structure](images/DatabaseStructure.png)

## 🚀 Getting Started

### 1. Clone the repository

### 2. Configure `.env`

### 3. Run the application


## 💡 Doubt
### Room vs Inventory 

Why are we creating a separate `Inventory` entity when we could simply put the inventory-related fields inside of `Room` entity?
As we know room entity represents room-type. So, we can simply populate/multiply/create multiple room-entries for each date, for each room type, for the next one year.

### ✅ Ans

We can do so. But then for each entry of the room, for a new date, we are duplicating fields which are constants for long period.

Fields like: `photos`, `amenities`, `capacity`, `basePrice`, `type`.

| Entity | Represents |
|---|---|
| **Room** | Relatively permanent information |
| **Inventory** | Date-specific information |

So our database design is basically:

```text
Hotel
 |
 └── Room (Deluxe)
      |
      ├── Inventory (Sep 10)
      ├── Inventory (Sep 11)
      ├── Inventory (Sep 12)
      └── Inventory (Sep 13)
```
