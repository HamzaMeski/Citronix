# 🍋 Citronix - Lemon Farm Management System

## Overview
Citronix is a sophisticated farm management system designed specifically for lemon orchards. It provides comprehensive tools for managing farms, fields, trees, harvests, and sales, with a focus on optimizing productivity and tracking growth cycles.

## 🌟 Key Features
- **Farm Management**
  - Multi-criteria search capabilities
  - Area and resource optimization
  - Detailed farm analytics

- **Field Operations**
  - Automated area calculations
  - Field capacity monitoring
  - Resource allocation tracking

- **Tree Lifecycle Management**
  - Age-based productivity tracking
  - Seasonal yield predictions
  - Health monitoring system

- **Harvest Planning**
  - Seasonal harvest scheduling
  - Yield tracking per tree
  - Automated productivity calculations

- **Sales Operations**
  - Revenue tracking
  - Client management
  - Harvest-to-sale mapping

## 🔧 Technical Stack
- **Backend**: Spring Boot 3.x
- **Database**: PostgreSQL
- **API Documentation**: OpenAPI (Swagger)
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven
- **Code Quality**: SonarQube

## 📋 Prerequisites
- JDK 17 or higher
- Maven 3.8+
- PostgreSQL 15+

## 🚀 Quick Start
1. Clone the repository

bash
git clone https://github.com/HamzaMeski/citronix.git

2. Configure database
  properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/citronix
  spring.datasource.username=your_username
  spring.datasource.password=your_password

3. Build the project
  bash
  mvn clean install

4. Run the application
  bash
  mvn spring-boot:run

## 🏗 Architecture
com.citronix.backend
├── controller # REST API endpoints
├── service # Business logic
├── repository # Data access
├── entity # Domain models
├── dto # Data transfer objects
├── mapper # Object mappers
├── exception # Exception handling
└── util # Utilities

## 🔒 Business Rules
- Minimum field area: 0.1 hectare (1,000 m²)
- Maximum field area: 50% of farm total area
- Maximum fields per farm: 10
- Tree density: 100 trees per hectare
- Tree productivity:
  - Young (<3 years): 2.5 kg/season
  - Mature (3-10 years): 12 kg/season
  - Old (>10 years): 20 kg/season

## 📊 API Documentation
Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

## 🧪 Testing
Run tests with:
bash
mvn test

## 🔄 Database Schema
sql
-- Core tables
Farm (id, name, location, total_area, creation_date)
Field (id, farm_id, area)
Tree (id, field_id, planting_date)
Harvest (id, season, date, total_quantity)
Sale (id, harvest_id, client, unit_price, quantity)


## 📈 Performance Considerations
- Pagination for large datasets
- Caching for frequent queries
- Optimized database indexes

## 🤝 Contributing
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📝 License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## 👥 Developer
- [Hamza Meski] - Project Developer

## 📞 Support
For support, email meskihamza5@gmail.com.

## 🙏 Acknowledgments
- Spring Boot team for the excellent framework
- PostgreSQL team for the robust database
