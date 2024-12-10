FROM node:22.3.0-bullseye
WORKDIR /data
COPY . /data
COPY .env.docker /data/.env
RUN npm install
CMD npm run dev
