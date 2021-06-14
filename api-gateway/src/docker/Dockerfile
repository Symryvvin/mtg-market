FROM openjdk:11

USER root
ARG APP_HOME=/opt/app/@project.name@
COPY @project.name@-@project.version@.jar /tmp/
RUN	mkdir -p ${APP_HOME} && mv /tmp/@project.name@-@project.version@.jar ${APP_HOME}
WORKDIR ${APP_HOME}

CMD ["java", "-jar", "@project.name@-@project.version@.jar"]