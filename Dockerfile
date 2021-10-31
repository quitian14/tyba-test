FROM amazoncorretto:17

RUN mkdir /code
COPY build/libs /code

ENTRYPOINT [ "sh", "-c", "java -jar /code/*.jar" ]