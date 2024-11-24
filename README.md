# 🍋 Citronix - BACKEND Lemon Farm Management System

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
- **Containerization**: Docker
- **Code Quality**: SonarQube

## 📋 Prerequisites
- Docker and Docker Compose
- Git

## 🚀 Quick Start

### Using Docker (Recommended)
1. Clone the repository
```bash
git clone https://github.com/HamzaMeski/Citronix.git
```

2. remove old containers

3. make sure that postgresql service is inactive in the machine
```bash
sudo systemctl stop postgresql
```

4.build the project
```bash
sudo docker-compose -f docker-compose.dev.yml up --build
```

6.If citronix_db doesn't created while building make sure to create it manually

7.Enjoy the project


## 🔒 Business Rules
- Minimum field area: 0.1 hectare (1,000 m²)
- Maximum field area: 50% of farm total area
- Maximum fields per farm: 10
- Tree density: 100 trees per hectare
- Tree productivity:
  - Young (<3 years): 2.5 kg/season
  - Mature (3-10 years): 12 kg/season
  - Old (>10 years): 20 kg/season


## 📈 Performance Considerations
- Pagination for large datasets
- Caching for frequent queries
- Optimized database indexes
- Container resource optimization

## 🐳 Docker Configuration
- Multi-stage builds for optimized images
- Separate development and test environments
- Health checks for service reliability
- Volume persistence for database data
- Network isolation between services

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
For support, email meskihamza5@gmail.com

## 🙏 Acknowledgments
- Spring Boot team for the excellent framework
- PostgreSQL team for the robust database
- Docker team for containerization support
