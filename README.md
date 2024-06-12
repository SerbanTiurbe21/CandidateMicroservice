# Microserviciul de Candidați

Microserviciul de Candidați gestionează operațiunile legate de candidați în cadrul sistemului de microservicii. Acesta gestionează cererile HTTP pentru adăugarea, actualizarea, ștergerea și interogarea candidaților, implementând mecanisme de securitate prin JWT tokens.

## Descriere
Microserviciul de Candidați este implementat folosind Spring Boot și Spring WebFlux, oferind un layer robust de gestionare a datelor candidaților. Acesta facilitează stocarea și interogarea datelor prin integrare cu MongoDB și include mecanisme avansate pentru gestionarea situațiilor de eroare și suprasolicitare.

## Configurarea Proiectului

Candidate este configurat printr-un set de reguli definite în fișierul `application.yaml` care include:
  - Setări pentru conexiunea la baza de date MongoDB
  - Setări de securitate pentru validarea JWT tokens

## Dockerfile

Proiectul include un Dockerfile pentru containerizarea și desfășurarea ușoară a serviciului Candidate. Acesta este configurat pentru a rula pe portul 8082.

## Rularea Serviciului Candidate cu Docker

Pentru a rula serviciul Candidate într-un container Docker, urmează pașii simpli de mai jos pentru a construi și rula imaginea.

### Construirea Imaginii Docker

  - Deschide un terminal și navighează în directorul sursă al proiectului, unde se află `Dockerfile`.
  - Rulează următoarea comandă pentru a construi imaginea Docker pentru acest microserviciu. Acest pas va crea o imagine Docker locală etichetată ca candidate:

    `docker build -t candidates-service .`

### Rularea Containerului Docker

După construirea imaginii, poți rula containerul folosind imaginea creată:

`docker run -p 8082:8082 candidate-service`

Această comandă va porni un container din imaginea candidate, mapând portul 8082 al containerului pe portul 8082 al mașinii tale locale. Asta înseamnă că poți accesa microserviciul navigând la http://localhost:8082 în browserul tău.

:bangbang: Însă acest pas nu este necesar pentru că există un `Dockerfile` în repository-ul central de unde se vor porni toate containerele. :bangbang:
