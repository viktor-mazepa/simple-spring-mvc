FROM tomcat:alpine
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ./target/SimpleSpringMVC.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh","run"]