FROM node:12-alpine as builder
WORKDIR /app
COPY package.json /app/package.json
RUN npm install react-router-dom && npm install --only=prod
COPY . /app
RUN npm run build

FROM nginx:1.20.1-alpine
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]