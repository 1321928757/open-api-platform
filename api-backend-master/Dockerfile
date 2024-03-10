FROM openjdk:8
COPY *.jar /app.jar 
CMD ["--server.port=10001"]
EXPOSE 8101
ENTRYPOINT ["java","-Duser.timezone=Asia/Shanghai","-jar","/app.jar"] 
