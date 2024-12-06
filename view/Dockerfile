# 빌드 단계
FROM node:16 as build

WORKDIR /src
COPY . .
RUN npm install
RUN npm run build

# 배포 단계
FROM nginx:alpine
COPY --from=build /src/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
