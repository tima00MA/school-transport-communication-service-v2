# School Transport Communication Service 🚍

**Microservice central de communication synchrone et asynchrone**  
Projet M2 Systèmes Distribués – Transport Scolaire  
Auteur : tima00MA  
GitHub : https://github.com/tima00MA/school-transport-communication-service  
Date : Novembre 2025

## 🎯 Rôle dans l’architecture
Ce microservice est le **point d’entrée unique** (API Gateway + Message Broker) pour toute la communication entre les 8 autres microservices du système de transport scolaire.

- **API Gateway** → tous les appels passent par lui
- **Message Broker** → centralise RabbitMQ et les événements

## ⚡ Fonctionnalités implémentées

### Communication Asynchrone (RabbitMQ)
| Endpoint                  | Description                                           |
|---------------------------|-------------------------------------------------------|
| `POST /async/publish`     | Publie un message dans n’importe quelle file          |
| `POST /async/subscribe`   | Abonne un microservice à une file (callback URL)      |
| `GET /async/logs`         | Historique complet des messages publiés               |

### Communication Synchrone (REST direct)
| Endpoint                               | Description                                                  |
|----------------------------------------|--------------------------------------------------------------|
| `POST /sync/{targetService}/{path}`    | Endpoint générique → appelle n’importe quel microservice    |
| Exemple : `/sync/location/student`     | Met à jour la localisation d’un élève immédiatement         |

### Santé & Monitoring
| Endpoint                  | Description                  |
|---------------------------|------------------------------|
| `GET /actuator/health`    | Vérifie que le service est UP|

## 🛠 Technologies
- Spring Boot 3.5.4
- Spring Cloud Eureka Client
- RabbitMQ (files dynamiques)
- PostgreSQL (logs + abonnements)
- RestTemplate + DiscoveryClient

## Schéma d’architecture
textTous les microservices (Élèves, Bus, Parents, etc.)
            ↓
http://localhost:3009 (ce microservice)
   ├─► Sync  → Eureka → appel HTTP direct
   └─► Async → RabbitMQ → files + callbacks