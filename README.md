# Kita-Connecrion
This site is for parents who want to quickly and conveniently find a kindergarten for their child, and kindergarten managers who want to make it easier for themselves to admit new children to kindergarten.

## Getting Started
Because this is a static site, it consists of 2 components: client and server parts.
1. [Back-end](https://github.com/AnnaRuk/GraduateProjectAlmostBE)
2. [Front-end](https://github.com/AnnaRuk/GraduateProjectAlmostFront)

You should clone both of these repositories or fork them.
### Prerequisites

The front-end part requires Node.js to work.
For the backend part to work, you need a PostgreSQL database, active email with the ability to automatically send letters through the application, and you also need Amazon S3 - Cloud Object Storage or its alternatives from DigitalOcean.


### Installing
Before launching the front-end part, write the following commands:
```
npm install
npm i react-toastify
npm install @mui/icons-material @mui/material @emotion/styled @emotion/react
```
Use the following command to run:
```
npm start dev
```
Before launching the front-end part, set the following dependencies in environment variables:

```
spring.datasource.password
spring.datasource.url
spring.datasource.username
spring.mail.password
spring.mail.username
s3.accessKey
s3.secretKey
s3.endpoint
s3.region
```

## Tests

Integration testing has now been carried out. You can find the tests in the corresponding folder.

## Deployment

Deploying the project also requires setting the appropriate values in the environment variable of the backend part of the project, described in the "Installing" section.

## Built With
* Front-end:
  * [Vite Js](https://vitejs.dev/guide/) - A build tool that aims to provide a faster and leaner development experience for modern web projects.
  * [React TS](https://react.dev/) - The library for web and native user interfaces.
  * [Redux Toolkit](https://redux-toolkit.js.org/) - The official, opinionated, batteries-included toolset for efficient Redux development.
  * [React-toastify](https://www.npmjs.com/package/react-toastify) - Library, that allows you to add notifications to your app with ease.
  * [@mui/icons-material](https://mui.com/material-ui/material-icons/?query=faceb) - Library, that includes the 2,100+ official Material Icons converted to SvgIcon components. It depends on @mui/material, which requires Emotion packages.
  * HTML 5, Css
* Back-end:
  * [Maven](https://maven.apache.org/) - Dependency Management.
  * [Spring](https://spring.io/) - Java Framework.
  * [SpringBoot](https://spring.io/projects/spring-boot) - Popular Framework, that makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
  * [Amazon S3](https://aws.amazon.com/s3/?nc1=h_ls) - Object storage built to retrieve any amount of data from anywhere.
  * [Java MailSender](https://docs.spring.io/spring-framework/docs/3.0.x/spring-framework-reference/html/mail.html) - Helpful utility library for sending email that shields the user from the specifics of the underlying mailing system and is responsible for low level resource handling on behalf of the client.

## Authors

* **Anna Belyaeva** - *TeamLead, BE, FE* - [AnnaRuk](https://github.com/AnnaRuk)
* **Dimitri Rohleder** - *BE, FE, Design* - [GolderosDR](https://github.com/golderosDR)
* **Sedakov Sergey** - *BE* - [SSedakov](https://github.com/SSedakov)


## Acknowledgments

We would like to express our gratitude to the teachers of the [AIT-TR school](https://www.ait-tr.de/), especially
* Sidikov Marsel [Sidikov Marsel](https://github.com/MarselSidikov),
* Alisher Khamidov [Alisher Khamidov](https://github.com/AlisherKhamidov),

for their mentoring assistance in writing and deploying the project.
