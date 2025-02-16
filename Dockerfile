FROM tomcat:10-jdk21

WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY build/libs/*.war webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]