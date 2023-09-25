# Online book store

Api created for providing access to data, functionality, and services that enhance the customer experience, and drive business growth

## Table of Contents
- [Introduction](#introduction)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [Challenges](#challenges-and-solutions-in-building-our-online-book-store)
- [Summary](#summary)
## Introduction


Welcome to the Online Book Store project, a robust and secure Java Spring Boot application designed to provide an exceptional online shopping experience for book enthusiasts. This project combines a wide range of features and technologies to ensure a seamless and secure e-commerce platform for users. Below, we'll introduce you to some of the key aspects and functionalities of our application.

## Features

#### 1. Shopping Cart
Our Online Book Store allows users to add books to their shopping carts. You can easily browse through our extensive collection of books, select your favorites, and add them to your cart. The cart is fully functional, enabling users to view, edit, and finalize their purchases before proceeding to checkout.

#### 2. Order Management
Once you've filled your shopping cart with the books you desire, our application provides a streamlined order management system. You can review your orders and add them.

#### 3. Book management with pagination and sorting
We understand the importance of efficient book discovery. Our application offers a sophisticated search functionality that allows users to find all books by categories, author, title, or by id. With pagination support, you can explore a vast catalog of books without overwhelming your search results.

#### 4. Security and JWT Tokens for User auth
Security is paramount in our application. We've implemented Spring Security to safeguard your data and transactions. JWT (JSON Web Tokens) are used for secure authentication and authorization, ensuring that only authorized users can access sensitive functionalities.

###  Technologies Used
Our application leverages several cutting-edge technologies, including:
- **Spring Data JPA:** For efficient data access and persistence.
- **Spring Security:** To secure the application and user data.
- **MapStruct:** For simplified and efficient mapping between DTOs and entities.
- **Docker:** Containerization for easy deployment and scalability.
- **OpenAPI**: For documenting our RESTful APIs, providing clear and detailed information about available resources, request payloads, and response formats.
  **Liquibase**: Used to manage and version the database schema, allowing for easy database schema changes and ensuring data schema consistency across different environments.


### Api Functionalities
In our Online Book Store project, we've diligently followed REST (Representational State Transfer) principles in designing our controllers. Controllers are essential components responsible for handling HTTP requests and responses.
- **BookController**: Handles book-related operations, including creation, retrieval, update, and deletion of books.
- **CategoryController**
- **AuthenticationController**
- **ShoppingCartController**
- **OrderController** <br>
In detail order about functions of controllers you can read at [Features](#features)
## Getting Started

To get started with our Online Book Store, you can follow the installation instruction provided in the [README](#) file in the project repository. This guide will walk you through setting up the application, configuring the necessary settings, and launching it in your preferred environment.
Thank you for choosing our Online Book Store project. We hope you enjoy the world of books at your fingertips, and we're committed to providing you with a safe, efficient, and delightful shopping experience. If you have any questions or feedback, please don't hesitate to reach out. Happy reading!

## Prerequisites

List the prerequisites required to run your application. Include things like:

- Java 17 (https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- Maven (https://maven.apache.org/download.cgi)
- Docker (https://www.docker.com/)

### Installation

1. **Firstly you need to clone repository** <br>
You can either open IDEA --> File --> New Project from Version Control and insert link: https://github.com/satori11111/Online-Book-Store.git. <br>
 Or you can clone from console with command:
 ```bash 
git clone https://github.com/satori11111/Online-Book-Store.git
```

2. Build project and download dependencies for Maven with command:
 ```bash 
mvn clean install
```
3. Docker Compose your project:
 ```bash 
docker compose build
```
And
 ```bash 
docker compose up
```

### Usage

**If you want test Api without installation go to link below, there you can interact with all endpoints thanks to OpenApi:** 
https://ec2-52-205-209-190.compute-1.amazonaws.com/swagger-ui/index.html#/

**Create your own user:**
Use endpoint auth/register to register user and stick to these rules:

1.firstName length between 1 and 255<br>
2.lastName length between 1 and 255<br>
3.email should be valid<br>
4.password minimum length is 4<br>
5.shippingAddress is optional at this time<br>

**For access to all endpoints use admin credentials in auth/login** <br>
**Username**: romakuch@gmail.com <br>
**Password**: romaaaa

### Contributing
We welcome contributions from the community to enhance the Online Book Store project. Whether you want to fix a bug, improve an existing feature, or propose a new feature, your contributions are valuable to us. You can follow [Installation](#installation) guide and create Pull Request to project.
If you want to contact for more information and cooperation in development: romakuchmii@gmail.com

### Challenges and Solutions in Building Our Online Book Store

Building the Online Book Store project was a fulfilling endeavor, marked by various challenges. In this article, we'll briefly explore the hurdles we encountered during development and how we addressed them to create a robust e-commerce platform.

#### Challenge 1: Data Modeling and build different views from our domain models

**Issue:** Designing a flexible data model for books, users, orders, and categories was complex and showing user only necessary data.

**Solution:** We used Spring Data JPA and Liquibase to create an adaptable schema and pattern Dto.

#### Challenge 2: Security

**Issue:** Ensuring data security and user authentication was paramount.

**Solution:** Spring Security and JWT tokens were implemented for robust protection.

#### Challenge 3: Exception Handling

**Issue:** Handling errors and exceptions systematically was crucial.

**Solution:** We developed a global handler and custom exceptions for better error reporting.

### Summary
In summary, our project overcame these challenges through adaptable data modeling, strong security measures, systematic error handling. 
It stands as a testament to our commitment to best practices in software development, offering users a secure and enjoyable shopping experience while adhering to REST principles.